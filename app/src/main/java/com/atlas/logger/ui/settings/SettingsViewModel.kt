package com.atlas.logger.ui.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atlas.logger.data.backup.BackupManager
import com.atlas.logger.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val locale: AppLocale = AppLocale.ENGLISH,
    val weightUnit: WeightUnit = WeightUnit.KG,
    val snackbarMessage: String? = null
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val workoutRepository: com.atlas.logger.domain.repository.WorkoutRepository,
    private val backupManager: BackupManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                preferencesManager.language,
                preferencesManager.weightUnit
            ) { locale, unit ->
                SettingsUiState(locale = locale, weightUnit = unit)
            }.collect { state ->
                _uiState.update { state }
            }
        }
    }

    fun setLocale(locale: AppLocale) {
        viewModelScope.launch { preferencesManager.setLanguage(locale) }
    }

    fun setWeightUnit(unit: WeightUnit) {
        viewModelScope.launch { preferencesManager.setWeightUnit(unit) }
    }

    fun exportData(locale: AppLocale) {
        viewModelScope.launch {
            backupManager.exportData().fold(
                onSuccess = {
                    _uiState.update { it.copy(snackbarMessage = AtlasStrings.exportSuccess(locale)) }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(snackbarMessage = "${AtlasStrings.error(locale)}: ${e.message}") }
                }
            )
        }
    }

    fun importData(uri: Uri, locale: AppLocale) {
        viewModelScope.launch {
            backupManager.importData(uri).fold(
                onSuccess = {
                    _uiState.update { it.copy(snackbarMessage = AtlasStrings.importSuccess(locale)) }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(snackbarMessage = "${AtlasStrings.error(locale)}: ${e.message}") }
                }
            )
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            workoutRepository.deleteAllSessions()
        }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }
}
