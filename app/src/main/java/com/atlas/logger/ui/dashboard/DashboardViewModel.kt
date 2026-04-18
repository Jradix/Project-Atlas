package com.atlas.logger.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atlas.logger.domain.model.WeeklyVolume
import com.atlas.logger.domain.model.WorkoutSession
import com.atlas.logger.domain.repository.ExerciseRepository
import com.atlas.logger.domain.repository.WorkoutRepository
import com.atlas.logger.util.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val recentSessions: List<WorkoutSession> = emptyList(),
    val weeklyVolumes: List<WeeklyVolume> = emptyList(),
    val currentWeekVolume: Double = 0.0,
    val volumeChangePercent: Int = 0,
    val workoutsThisWeek: Int = 0,
    val hasActiveSession: Boolean = false,
    val isLoading: Boolean = true
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val exerciseRepository: ExerciseRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        // Preload exercises on first launch
        viewModelScope.launch {
            exerciseRepository.preloadIfEmpty()
        }

        // Observe recent sessions
        viewModelScope.launch {
            workoutRepository.getRecentSessions(5).collect { sessions ->
                _uiState.update { it.copy(recentSessions = sessions) }
            }
        }

        // Load stats
        viewModelScope.launch {
            loadStats()
            checkActiveSession()
        }
    }

    private suspend fun checkActiveSession() {
        val active = workoutRepository.getActiveSession()
        _uiState.update { it.copy(hasActiveSession = active != null) }
    }

    fun refresh() {
        viewModelScope.launch { 
            loadStats()
            checkActiveSession()
        }
    }

    fun repeatSession(sessionId: Long, onComplete: () -> Unit) {
        viewModelScope.launch {
            workoutRepository.repeatSession(sessionId)
            onComplete()
        }
    }

    fun deleteSession(sessionId: Long) {
        viewModelScope.launch {
            workoutRepository.deleteSession(sessionId)
            refresh() // Refresh stats after deletion
        }
    }

    private suspend fun loadStats() = coroutineScope {
        _uiState.update { it.copy(isLoading = true) }
        
        // Run queries in parallel for rocket speed
        val volumesDeferred = async { workoutRepository.getWeeklyVolumes(4) }
        val countDeferred = async { workoutRepository.getWorkoutCountThisWeek() }
        
        val weeklyVolumes = volumesDeferred.await()
        val workoutsThisWeek = countDeferred.await()

        val currentWeek = weeklyVolumes.lastOrNull()?.totalVolume ?: 0.0
        val previousWeek = if (weeklyVolumes.size >= 2) weeklyVolumes[weeklyVolumes.size - 2].totalVolume else 0.0
        val changePercent = if (previousWeek > 0) {
            ((currentWeek - previousWeek) / previousWeek * 100).toInt()
        } else 0

        _uiState.update {
            it.copy(
                weeklyVolumes = weeklyVolumes,
                currentWeekVolume = currentWeek,
                volumeChangePercent = changePercent,
                workoutsThisWeek = workoutsThisWeek,
                isLoading = false
            )
        }
    }
}
