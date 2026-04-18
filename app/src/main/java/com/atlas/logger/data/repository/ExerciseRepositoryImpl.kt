package com.atlas.logger.data.repository

import com.atlas.logger.data.local.ExercisePreloader
import com.atlas.logger.data.local.dao.ExerciseDao
import com.atlas.logger.data.local.entity.ExerciseEntity
import com.atlas.logger.domain.model.Exercise
import com.atlas.logger.domain.repository.ExerciseRepository
import com.atlas.logger.util.DebugLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseRepositoryImpl @Inject constructor(
    private val exerciseDao: ExerciseDao
) : ExerciseRepository {

    override fun getAllExercises(): Flow<List<Exercise>> =
        exerciseDao.getAllExercises().map { list -> list.map { it.toDomain() } }

    override fun searchExercises(query: String): Flow<List<Exercise>> =
        exerciseDao.searchByName(query).map { list -> list.map { it.toDomain() } }

    override fun getByMuscleGroup(group: String): Flow<List<Exercise>> =
        exerciseDao.getByMuscleGroup(group).map { list -> list.map { it.toDomain() } }

    override suspend fun getById(id: Long): Exercise? =
        exerciseDao.getById(id)?.toDomain()

    override suspend fun insert(exercise: Exercise): Long {
        val id = exerciseDao.insert(exercise.toEntity())
        DebugLogger.log("Inserted exercise: ${exercise.name} (id=$id)")
        return id
    }

    override suspend fun insertExercise(exercise: Exercise): Long {
        val id = exerciseDao.insert(exercise.toEntity())
        DebugLogger.log("Created custom exercise: ${exercise.name} (id=$id)")
        return id
    }

    override suspend fun update(exercise: Exercise) {
        exerciseDao.update(exercise.toEntity())
        DebugLogger.log("Updated exercise: ${exercise.name}")
    }

    override suspend fun updateExerciseImage(exerciseId: Long, imageUri: String) {
        val entity = exerciseDao.getById(exerciseId) ?: return
        exerciseDao.update(entity.copy(imageUri = imageUri))
        DebugLogger.log("Updated exercise image: id=$exerciseId")
    }

    override suspend fun delete(exercise: Exercise) {
        exerciseDao.delete(exercise.toEntity())
        DebugLogger.log("Deleted exercise: ${exercise.name}")
    }

    override suspend fun getCount(): Int = exerciseDao.getCount()

    override suspend fun preloadIfEmpty() {
        if (exerciseDao.getCount() == 0) {
            val exercises = ExercisePreloader.getExercises()
            exerciseDao.insertAll(exercises)
            DebugLogger.log("Preloaded ${exercises.size} exercises into database")
        }
    }

    // ═══ Mappers ═══

    private fun ExerciseEntity.toDomain() = Exercise(
        id = id,
        name = name,
        nameAr = nameAr,
        muscleGroup = muscleGroup,
        muscleGroupAr = muscleGroupAr,
        imageUri = imageUri,
        isCustom = isCustom
    )

    private fun Exercise.toEntity() = ExerciseEntity(
        id = id,
        name = name,
        nameAr = nameAr,
        muscleGroup = muscleGroup,
        muscleGroupAr = muscleGroupAr,
        imageUri = imageUri,
        isCustom = isCustom
    )
}
