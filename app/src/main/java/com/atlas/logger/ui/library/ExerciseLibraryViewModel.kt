package com.atlas.logger.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atlas.logger.domain.model.Exercise
import com.atlas.logger.domain.repository.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LibraryUiState(
    val exercises: List<Exercise> = emptyList(),
    val searchQuery: String = "",
    val selectedMuscleGroup: String? = null,
    val isLoading: Boolean = true,
    val imagePickExercise: Exercise? = null,
    val excludedIds: List<Long> = emptyList()
)

@HiltViewModel
class ExerciseLibraryViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LibraryUiState(selectedMuscleGroup = lastSelectedGroup))
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    private val _selectedGroup = MutableStateFlow<String?>(lastSelectedGroup)

    companion object {
        private var lastSelectedGroup: String? = null
    }

    init {
        viewModelScope.launch {
            combine(_searchQuery, _selectedGroup, _uiState.map { it.excludedIds }.distinctUntilChanged()) { query, group, excluded ->
                Triple(query, group, excluded)
            }.collectLatest { (query, group, excluded) ->
                // Ensure UI state matches the selected group for the chips
                _uiState.update { it.copy(selectedMuscleGroup = group) }
                
                val flow = when {
                    query.isNotBlank() -> exerciseRepository.searchExercises(query)
                    group != null -> exerciseRepository.getByMuscleGroup(group)
                    else -> exerciseRepository.getAllExercises()
                }
                flow.collect { exercises ->
                    val filtered = if (excluded.isEmpty()) exercises else exercises.filter { it.id !in excluded }
                    _uiState.update {
                        it.copy(exercises = filtered, isLoading = false)
                    }
                }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onMuscleGroupSelected(group: String?) {
        lastSelectedGroup = group
        _selectedGroup.value = group
        _uiState.update { it.copy(selectedMuscleGroup = group) }
    }

    fun createExercise(name: String, nameAr: String, muscleGroup: String, muscleGroupAr: String, imageUri: String?) {
        viewModelScope.launch {
            exerciseRepository.insertExercise(
                Exercise(
                    name = name,
                    nameAr = nameAr,
                    muscleGroup = muscleGroup,
                    muscleGroupAr = muscleGroupAr,
                    imageUri = imageUri,
                    isCustom = true
                )
            )
        }
    }

    fun onExerciseImageClick(exercise: Exercise) {
        _uiState.update { it.copy(imagePickExercise = exercise) }
    }

    fun clearImagePick() {
        _uiState.update { it.copy(imagePickExercise = null) }
    }

    fun updateExerciseImage(exerciseId: Long, imageUri: String) {
        viewModelScope.launch {
            exerciseRepository.updateExerciseImage(exerciseId, imageUri)
        }
    }

    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch {
            exerciseRepository.delete(exercise)
        }
    }

    fun setExcludedIds(ids: List<Long>) {
        _uiState.update { it.copy(excludedIds = ids) }
    }

    private fun filterExercises(exercises: List<Exercise>): List<Exercise> {
        val excluded = _uiState.value.excludedIds
        return if (excluded.isEmpty()) exercises else exercises.filter { it.id !in excluded }
    }

    val muscleGroups = listOf("Chest", "Back", "Legs", "Shoulders", "Arms", "Core", "Cardio")
}
