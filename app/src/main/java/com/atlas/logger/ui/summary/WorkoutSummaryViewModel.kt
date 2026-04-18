package com.atlas.logger.ui.summary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atlas.logger.domain.model.PersonalRecord
import com.atlas.logger.domain.model.WorkoutSession
import com.atlas.logger.domain.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.atlas.logger.domain.model.WorkoutExercise
import com.atlas.logger.domain.model.Exercise
import com.atlas.logger.domain.repository.ExerciseRepository

data class SummaryUiState(
    val session: WorkoutSession? = null,
    val exercises: List<WorkoutExercise> = emptyList(),
    val personalRecords: List<PersonalRecord> = emptyList(),
    val estimatedCalories: Int = 0,
    val isLoading: Boolean = true
)

@HiltViewModel
class WorkoutSummaryViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val exerciseRepository: ExerciseRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val sessionId: Long = savedStateHandle.get<Long>("sessionId") ?: 0L

    private val _uiState = MutableStateFlow(SummaryUiState())
    val uiState: StateFlow<SummaryUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            loadSummary()
        }
    }

    private suspend fun loadSummary() {
        val session = workoutRepository.getSessionById(sessionId) ?: return
        val prs = workoutRepository.checkPersonalRecords(sessionId)

        // Simple calorie estimation: ~5 calories per minute of weight training
        val estimatedCals = (session.durationSeconds / 60.0 * 5).toInt()

        // Fetch sets and build WorkoutExercise list
        workoutRepository.getSetsForWorkout(sessionId).collect { sets ->
            val grouped = sets.groupBy { it.exerciseId }
            val workoutExercises = grouped.map { (exerciseId, exerciseSets) ->
                val exercise = exerciseRepository.getById(exerciseId)
                WorkoutExercise(
                    exercise = exercise ?: Exercise(id = exerciseId, name = "Unknown", nameAr = "غير معروف", muscleGroup = "", muscleGroupAr = ""),
                    sets = exerciseSets.sortedBy { it.setOrder }
                )
            }
            
            _uiState.update {
                SummaryUiState(
                    session = session,
                    exercises = workoutExercises,
                    personalRecords = prs,
                    estimatedCalories = estimatedCals,
                    isLoading = false
                )
            }
        }
    }

    fun repeatSession(onComplete: () -> Unit) {
        viewModelScope.launch {
            workoutRepository.repeatSession(sessionId)
            onComplete()
        }
    }
}
