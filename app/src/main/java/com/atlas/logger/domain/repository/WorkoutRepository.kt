package com.atlas.logger.domain.repository

import com.atlas.logger.domain.model.*
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    // Sessions
    fun getCompletedSessions(): Flow<List<WorkoutSession>>
    fun getRecentSessions(limit: Int = 10): Flow<List<WorkoutSession>>
    suspend fun getActiveSession(): WorkoutSession?
    fun getActiveSessionFlow(): Flow<WorkoutSession?>
    suspend fun getSessionById(id: Long): WorkoutSession?
    suspend fun startNewSession(name: String = "", nameAr: String = ""): Long
    suspend fun repeatSession(sessionId: Long): Long
    suspend fun finishSession(sessionId: Long, name: String = "", nameAr: String = "", totalVolume: Double = 0.0, durationSeconds: Int = 0)
    suspend fun deleteSession(sessionId: Long)
    suspend fun deleteAllSessions()

    // Sets
    fun getSetsForWorkout(workoutId: Long): Flow<List<WorkoutSet>>
    suspend fun addSet(set: WorkoutSet): Long
    suspend fun updateSet(set: WorkoutSet)
    suspend fun deleteSet(set: WorkoutSet)
    suspend fun getLastPerformance(exerciseId: Long): List<WorkoutSet>

    // Stats
    suspend fun getWeeklyVolumes(weeks: Int = 4): List<WeeklyVolume>
    suspend fun checkPersonalRecords(sessionId: Long): List<PersonalRecord>
    suspend fun getWorkoutCountThisWeek(): Int
    suspend fun updateTimer(sessionId: Long, seconds: Int)
}
