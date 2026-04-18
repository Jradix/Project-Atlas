package com.atlas.logger.ui.history

import androidx.lifecycle.ViewModel
import com.atlas.logger.domain.model.WorkoutSession
import com.atlas.logger.domain.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

data class HistoryUiState(
    val sessions: List<WorkoutSession> = emptyList()
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            workoutRepository.getCompletedSessions().collect { sessions ->
                _uiState.value = HistoryUiState(sessions = sessions)
            }
        }
    }

    fun repeatSession(sessionId: Long, onComplete: () -> Unit) {
        viewModelScope.launch {
            workoutRepository.repeatSession(sessionId)
            onComplete()
        }
    }
}
