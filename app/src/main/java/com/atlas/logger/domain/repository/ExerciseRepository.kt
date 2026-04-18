package com.atlas.logger.domain.repository

import com.atlas.logger.domain.model.Exercise
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
    fun getAllExercises(): Flow<List<Exercise>>
    fun searchExercises(query: String): Flow<List<Exercise>>
    fun getByMuscleGroup(group: String): Flow<List<Exercise>>
    suspend fun getById(id: Long): Exercise?
    suspend fun insert(exercise: Exercise): Long
    suspend fun insertExercise(exercise: Exercise): Long
    suspend fun update(exercise: Exercise)
    suspend fun updateExerciseImage(exerciseId: Long, imageUri: String)
    suspend fun delete(exercise: Exercise)
    suspend fun getCount(): Int
    suspend fun preloadIfEmpty()
}
