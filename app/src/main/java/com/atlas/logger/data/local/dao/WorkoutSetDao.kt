package com.atlas.logger.data.local.dao

import androidx.room.*
import com.atlas.logger.data.local.entity.WorkoutSetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutSetDao {

    @Query("SELECT * FROM workout_sets WHERE workoutId = :workoutId ORDER BY exerciseId, setOrder ASC")
    fun getSetsForWorkout(workoutId: Long): Flow<List<WorkoutSetEntity>>

    @Query("SELECT * FROM workout_sets WHERE workoutId = :workoutId ORDER BY exerciseId, setOrder ASC")
    suspend fun getSetsForWorkoutOnce(workoutId: Long): List<WorkoutSetEntity>

    @Query("""
        SELECT ws.* FROM workout_sets ws
        INNER JOIN workout_sessions wse ON ws.workoutId = wse.id
        WHERE ws.exerciseId = :exerciseId 
        AND wse.isCompleted = 1 
        AND ws.isCompleted = 1
        ORDER BY wse.date DESC
        LIMIT 3
    """)
    suspend fun getLastPerformance(exerciseId: Long): List<WorkoutSetEntity>

    @Query("""
        SELECT MAX(weight) FROM workout_sets ws
        INNER JOIN workout_sessions wse ON ws.workoutId = wse.id
        WHERE ws.exerciseId = :exerciseId 
        AND wse.isCompleted = 1 
        AND ws.isCompleted = 1
    """)
    suspend fun getPersonalBestWeight(exerciseId: Long): Double?

    @Query("""
        SELECT MAX(weight * reps) FROM workout_sets ws
        INNER JOIN workout_sessions wse ON ws.workoutId = wse.id
        WHERE ws.exerciseId = :exerciseId 
        AND wse.isCompleted = 1 
        AND ws.isCompleted = 1
    """)
    suspend fun getPersonalBestVolume(exerciseId: Long): Double?

    @Query("SELECT * FROM workout_sets ORDER BY id ASC")
    suspend fun getAllSetsOnce(): List<WorkoutSetEntity>

    @Query("SELECT DISTINCT exerciseId FROM workout_sets WHERE workoutId = :workoutId")
    suspend fun getExerciseIdsForWorkout(workoutId: Long): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(set: WorkoutSetEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(sets: List<WorkoutSetEntity>)

    @Update
    suspend fun update(set: WorkoutSetEntity)

    @Delete
    suspend fun delete(set: WorkoutSetEntity)

    @Query("DELETE FROM workout_sets WHERE workoutId = :workoutId AND exerciseId = :exerciseId")
    suspend fun deleteSetsForExercise(workoutId: Long, exerciseId: Long)

    @Query("DELETE FROM workout_sets")
    suspend fun deleteAll()
}
