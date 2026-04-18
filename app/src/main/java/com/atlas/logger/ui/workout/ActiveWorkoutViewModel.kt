package com.atlas.logger.ui.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atlas.logger.domain.model.Exercise
import com.atlas.logger.domain.model.WorkoutExercise
import com.atlas.logger.domain.model.WorkoutSet
import com.atlas.logger.domain.repository.ExerciseRepository
import com.atlas.logger.domain.repository.WorkoutRepository
import com.atlas.logger.util.DebugLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ActiveWorkoutUiState(
    val sessionId: Long = 0,
    val isActive: Boolean = false,
    val isTimerRunning: Boolean = false,
    val exercises: List<WorkoutExercise> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class ActiveWorkoutViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ActiveWorkoutUiState())
    val uiState: StateFlow<ActiveWorkoutUiState> = _uiState.asStateFlow()

    private val _elapsedSeconds = MutableStateFlow(0)
    val elapsedSeconds: StateFlow<Int> = _elapsedSeconds.asStateFlow()

    private var timerJob: Job? = null
    private var setsFlowJob: Job? = null

    init {
        viewModelScope.launch {
            workoutRepository.getActiveSessionFlow().collect { session ->
                if (session != null) {
                    // ONLY update if it's a BRAND NEW session being loaded
                    if (_uiState.value.sessionId != session.id) {
                        _elapsedSeconds.value = session.currentDurationSeconds
                        _uiState.update { it.copy(
                            sessionId = session.id,
                            isActive = true,
                            isTimerRunning = false
                        ) }
                        observeSets(session.id)
                        DebugLogger.log("ActiveWorkoutViewModel: Initial Load session ${session.id}")
                    }
                } else {
                    _uiState.update { it.copy(sessionId = 0, isActive = false, isTimerRunning = false, exercises = emptyList()) }
                    setsFlowJob?.cancel()
                    timerJob?.cancel()
                }
            }
        }
    }

    fun toggleWorkoutTimer() {
        val currentlyRunning = _uiState.value.isTimerRunning
        val sessionId = _uiState.value.sessionId
        if (sessionId == 0L) return
        
        if (currentlyRunning) {
            timerJob?.cancel()
            _uiState.update { it.copy(isTimerRunning = false) }
            viewModelScope.launch { workoutRepository.updateTimer(sessionId, _elapsedSeconds.value) }
        } else {
            _uiState.update { it.copy(isTimerRunning = true) }
            timerJob = viewModelScope.launch {
                var tick = 0
                while (true) {
                    delay(1000)
                    _elapsedSeconds.value += 1
                    tick++
                    if (tick >= 5) {
                        workoutRepository.updateTimer(sessionId, _elapsedSeconds.value)
                        tick = 0
                    }
                }
            }
        }
    }

    private fun observeSets(sessionId: Long) {
        setsFlowJob?.cancel()
        setsFlowJob = viewModelScope.launch {
            workoutRepository.getSetsForWorkout(sessionId).collect { sets ->
                if (sets.isEmpty()) {
                    _uiState.update { it.copy(exercises = emptyList()) }
                    return@collect
                }

                // Optimization: Fetch all needed exercises in ONE go instead of inside the loop
                val exerciseIds = sets.map { it.exerciseId }.distinct()
                val exercisesMap = exerciseIds.mapNotNull { id ->
                    exerciseRepository.getById(id)?.let { it.id to it }
                }.toMap()

                val grouped = sets.groupBy { it.exerciseId }
                val workoutExercises = grouped.map { (exerciseId, exerciseSets) ->
                    val exercise = exercisesMap[exerciseId]
                    val sortedSets = exerciseSets.sortedBy { it.setOrder }
                    WorkoutExercise(
                        exercise = exercise ?: Exercise(id = exerciseId, name = "Unknown", nameAr = "غير معروف", muscleGroup = "", muscleGroupAr = ""),
                        sets = sortedSets,
                        firstSetId = sortedSets.firstOrNull()?.id ?: 0L
                    )
                }.sortedBy { it.firstSetId }
                
                _uiState.update { it.copy(exercises = workoutExercises) }
            }
        }
    }

    fun addExerciseToWorkout(exerciseId: Long) {
        viewModelScope.launch {
            var sessionId = _uiState.value.sessionId
            if (sessionId == 0L) {
                sessionId = workoutRepository.startNewSession()
                _uiState.update { it.copy(sessionId = sessionId, isActive = true) }
                observeSets(sessionId)
            }

            val alreadyExists = _uiState.value.exercises.any { it.exercise.id == exerciseId }
            if (alreadyExists) return@launch

            val lastPerf = workoutRepository.getLastPerformance(exerciseId)
            val prefillWeight = lastPerf.firstOrNull()?.weight ?: 0.0
            val prefillReps = lastPerf.firstOrNull()?.reps ?: 0

            workoutRepository.addSet(
                WorkoutSet(
                    workoutId = sessionId,
                    exerciseId = exerciseId,
                    weight = prefillWeight,
                    reps = prefillReps,
                    setOrder = 0
                )
            )
        }
    }

    fun addSetToExercise(exerciseId: Long) {
        val sessionId = _uiState.value.sessionId
        viewModelScope.launch {
            val currentSets = _uiState.value.exercises
                .firstOrNull { it.exercise.id == exerciseId }?.sets ?: emptyList()
            val lastSet = currentSets.lastOrNull()
            val newOrder = (currentSets.maxOfOrNull { it.setOrder } ?: -1) + 1

            workoutRepository.addSet(
                WorkoutSet(
                    workoutId = sessionId,
                    exerciseId = exerciseId,
                    weight = lastSet?.weight ?: 0.0,
                    reps = lastSet?.reps ?: 0,
                    setOrder = newOrder
                )
            )
        }
    }

    fun updateSet(set: WorkoutSet) {
        viewModelScope.launch {
            workoutRepository.updateSet(set)
        }
    }

    fun completeSet(set: WorkoutSet) {
        viewModelScope.launch {
            workoutRepository.updateSet(set.copy(isCompleted = true))
        }
    }

    fun uncompleteSet(set: WorkoutSet) {
        viewModelScope.launch {
            workoutRepository.updateSet(set.copy(isCompleted = false))
        }
    }

    fun deleteSet(set: WorkoutSet) {
        viewModelScope.launch {
            workoutRepository.deleteSet(set)
        }
    }

    suspend fun discardWorkout() {
        val sessionId = _uiState.value.sessionId
        if (sessionId != 0L) {
            timerJob?.cancel()
            workoutRepository.deleteSession(sessionId)
            _uiState.update { it.copy(isActive = false, sessionId = 0, exercises = emptyList()) }
        }
    }

    suspend fun finishWorkout(): Long {
        val sessionId = _uiState.value.sessionId
        val exercises = _uiState.value.exercises
        timerJob?.cancel()

        // Auto-generate name from muscle groups
        val muscleGroups = exercises.map { it.exercise.muscleGroup }.distinct().take(3)
        val nameEn = if (muscleGroups.isEmpty()) "Workout"
        else muscleGroups.joinToString(" & ") + " Day"
        val nameAr = if (muscleGroups.isEmpty()) "تمرين"
        else {
            val arGroups = exercises.map { it.exercise.muscleGroupAr }.distinct().take(3)
            "يوم " + arGroups.joinToString(" و ")
        }

        // Calculate total volume (ignore empty sets)
        val totalVolume = exercises.flatMap { it.sets }
            .filter { it.reps > 0 }
            .sumOf { it.weight * it.reps }

        // Call directly and wait (suspend)
        workoutRepository.finishSession(
            sessionId = sessionId,
            name = nameEn,
            nameAr = nameAr,
            totalVolume = totalVolume,
            durationSeconds = _elapsedSeconds.value
        )
        
        _uiState.update { it.copy(isActive = false) }
        return sessionId
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        setsFlowJob?.cancel()
    }
}
