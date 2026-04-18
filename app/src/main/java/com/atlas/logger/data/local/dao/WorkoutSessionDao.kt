package com.atlas.logger.data.local.dao

import androidx.room.*
import com.atlas.logger.data.local.entity.WorkoutSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutSessionDao {

    @Query("SELECT * FROM workout_sessions WHERE isCompleted = 1 ORDER BY date DESC")
    fun getCompletedSessions(): Flow<List<WorkoutSessionEntity>>

    @Query("SELECT * FROM workout_sessions WHERE isCompleted = 1 ORDER BY date DESC LIMIT :limit")
    fun getRecentSessions(limit: Int = 10): Flow<List<WorkoutSessionEntity>>

    @Query("SELECT * FROM workout_sessions WHERE isCompleted = 0 ORDER BY id DESC LIMIT 1")
    fun getActiveSessionFlow(): Flow<WorkoutSessionEntity?>

    @Query("SELECT * FROM workout_sessions WHERE isCompleted = 0 ORDER BY id DESC LIMIT 1")
    suspend fun getActiveSession(): WorkoutSessionEntity?

    @Query("SELECT * FROM workout_sessions WHERE id = :id")
    suspend fun getById(id: Long): WorkoutSessionEntity?

    @Query("""
        SELECT SUM(totalVolume) FROM workout_sessions 
        WHERE isCompleted = 1 
        AND date >= :startOfWeek
    """)
    suspend fun getWeeklyVolume(startOfWeek: Long): Double?

    @Query("""
        SELECT SUM(totalVolume) FROM workout_sessions 
        WHERE isCompleted = 1 
        AND date >= :startOfWeek AND date < :endOfWeek
    """)
    suspend fun getVolumeForWeek(startOfWeek: Long, endOfWeek: Long): Double?

    @Query("""
        SELECT COUNT(*) FROM workout_sessions 
        WHERE isCompleted = 1 
        AND date >= :startOfWeek
    """)
    suspend fun getWorkoutCountThisWeek(startOfWeek: Long): Int

    @Query("SELECT * FROM workout_sessions ORDER BY id ASC")
    suspend fun getAllSessionsOnce(): List<WorkoutSessionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: WorkoutSessionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(sessions: List<WorkoutSessionEntity>)

    @Update
    suspend fun update(session: WorkoutSessionEntity)

    @Query("UPDATE workout_sessions SET currentDurationSeconds = :seconds WHERE id = :id")
    suspend fun updateTimer(id: Long, seconds: Int)

    @Delete
    suspend fun delete(session: WorkoutSessionEntity)

    @Query("DELETE FROM workout_sessions")
    suspend fun deleteAll()
}
