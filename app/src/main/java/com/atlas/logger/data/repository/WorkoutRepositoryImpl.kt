package com.atlas.logger.data.repository

import com.atlas.logger.data.local.dao.ExerciseDao
import com.atlas.logger.data.local.dao.WorkoutSessionDao
import com.atlas.logger.data.local.dao.WorkoutSetDao
import com.atlas.logger.data.local.entity.WorkoutSessionEntity
import com.atlas.logger.data.local.entity.WorkoutSetEntity
import com.atlas.logger.domain.model.*
import com.atlas.logger.domain.repository.WorkoutRepository
import com.atlas.logger.util.DebugLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutRepositoryImpl @Inject constructor(
    private val sessionDao: WorkoutSessionDao,
    private val setDao: WorkoutSetDao,
    private val exerciseDao: ExerciseDao
) : WorkoutRepository {

    // ═══════════════════════════════════════
    // Sessions
    // ═══════════════════════════════════════

    override fun getCompletedSessions(): Flow<List<WorkoutSession>> =
        sessionDao.getCompletedSessions().map { list -> list.map { it.toDomain() } }

    override fun getRecentSessions(limit: Int): Flow<List<WorkoutSession>> =
        sessionDao.getRecentSessions(limit).map { list -> list.map { it.toDomain() } }

    override suspend fun getActiveSession(): WorkoutSession? =
        sessionDao.getActiveSession()?.toDomain()

    override fun getActiveSessionFlow(): Flow<WorkoutSession?> =
        sessionDao.getActiveSessionFlow().map { it?.toDomain() }

    override suspend fun getSessionById(id: Long): WorkoutSession? =
        sessionDao.getById(id)?.toDomain()

    override suspend fun startNewSession(name: String, nameAr: String): Long {
        // Cleanup any existing empty session first
        val currentActive = sessionDao.getActiveSession()
        if (currentActive != null) {
            val currentSets = setDao.getSetsForWorkoutOnce(currentActive.id)
            if (currentSets.isEmpty()) {
                sessionDao.delete(currentActive)
                DebugLogger.log("Cleaned up empty session ${currentActive.id} before starting new one")
            }
        }

        val session = WorkoutSessionEntity(
            date = System.currentTimeMillis(),
            name = name,
            nameAr = nameAr,
            isCompleted = false
        )
        val id = sessionDao.insert(session)
        DebugLogger.log("Started new workout session (id=$id)")
        return id
    }

    override suspend fun repeatSession(sessionId: Long): Long {
        // Cleanup any existing empty session first
        val currentActive = sessionDao.getActiveSession()
        if (currentActive != null) {
            val currentSets = setDao.getSetsForWorkoutOnce(currentActive.id)
            if (currentSets.isEmpty()) {
                sessionDao.delete(currentActive)
                DebugLogger.log("Cleaned up empty session ${currentActive.id} before repeating")
            }
        }

        val oldSession = sessionDao.getById(sessionId) ?: return 0L
        val oldSets = setDao.getSetsForWorkoutOnce(sessionId)
        
        val newSessionId = startNewSession(oldSession.name, oldSession.nameAr)
        
        // Copy sets but marked as incomplete
        oldSets.forEach { oldSet ->
            val newSet = oldSet.copy(
                id = 0,
                workoutId = newSessionId,
                isCompleted = false
            )
            setDao.insert(newSet)
        }
        
        DebugLogger.log("Repeated workout session (oldId=$sessionId, newId=$newSessionId) with ${oldSets.size} sets")
        return newSessionId
    }

    override suspend fun finishSession(sessionId: Long, name: String, nameAr: String, totalVolume: Double, durationSeconds: Int) {
        val session = sessionDao.getById(sessionId) ?: return

        // If no external volume provided, compute from sets
        val finalVolume = if (totalVolume > 0.0) totalVolume else {
            val sets = setDao.getSetsForWorkoutOnce(sessionId)
            sets.filter { it.isCompleted }.sumOf { it.weight * it.reps }
        }

        val updatedSession = session.copy(
            isCompleted = true,
            name = name.ifEmpty { session.name },
            nameAr = nameAr.ifEmpty { session.nameAr },
            totalVolume = finalVolume,
            durationSeconds = durationSeconds // Use the actual duration passed from UI
        )
        sessionDao.update(updatedSession)
        DebugLogger.log("Finished workout session (id=$sessionId, name='$name', volume=$finalVolume kg, duration=$durationSeconds)")
    }

    override suspend fun deleteSession(sessionId: Long) {
        val session = sessionDao.getById(sessionId) ?: return
        sessionDao.delete(session)
        DebugLogger.log("Deleted workout session (id=$sessionId)")
    }

    override suspend fun deleteAllSessions() {
        sessionDao.deleteAll()
        DebugLogger.log("Deleted all workout sessions")
    }

    // ═══════════════════════════════════════
    // Sets
    // ═══════════════════════════════════════

    override fun getSetsForWorkout(workoutId: Long): Flow<List<WorkoutSet>> =
        setDao.getSetsForWorkout(workoutId).map { list -> list.map { it.toDomain() } }

    override suspend fun addSet(set: WorkoutSet): Long {
        val id = setDao.insert(set.toEntity())
        DebugLogger.log("Added set: exercise=${set.exerciseId}, ${set.weight}kg x ${set.reps}")
        return id
    }

    override suspend fun updateSet(set: WorkoutSet) {
        setDao.update(set.toEntity())
        DebugLogger.log("Updated set (id=${set.id}): ${set.weight}kg x ${set.reps}, completed=${set.isCompleted}")
    }

    override suspend fun deleteSet(set: WorkoutSet) {
        setDao.delete(set.toEntity())
        DebugLogger.log("Deleted set (id=${set.id})")
    }

    override suspend fun getLastPerformance(exerciseId: Long): List<WorkoutSet> =
        setDao.getLastPerformance(exerciseId).map { it.toDomain() }

    // ═══════════════════════════════════════
    // Stats
    // ═══════════════════════════════════════

    override suspend fun getWeeklyVolumes(weeks: Int): List<WeeklyVolume> {
        val result = mutableListOf<WeeklyVolume>()
        val cal = Calendar.getInstance()
        // Set to start of current week (Monday)
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        for (i in 0 until weeks) {
            val weekStart = cal.timeInMillis
            cal.add(Calendar.WEEK_OF_YEAR, 1)
            val weekEnd = cal.timeInMillis
            cal.add(Calendar.WEEK_OF_YEAR, -1) // Reset

            val volume = sessionDao.getVolumeForWeek(weekStart, weekEnd) ?: 0.0
            result.add(0, WeeklyVolume(weekStart = weekStart, totalVolume = volume))
            cal.add(Calendar.WEEK_OF_YEAR, -1)
        }
        return result
    }

    override suspend fun checkPersonalRecords(sessionId: Long): List<PersonalRecord> {
        val records = mutableListOf<PersonalRecord>()
        val exerciseIds = setDao.getExerciseIdsForWorkout(sessionId)

        for (exerciseId in exerciseIds) {
            val exercise = exerciseDao.getById(exerciseId) ?: continue
            val sets = setDao.getSetsForWorkoutOnce(sessionId)
                .filter { it.exerciseId == exerciseId && it.isCompleted }

            if (sets.isEmpty()) continue

            // Check weight PR
            val maxWeight = sets.maxOf { it.weight }
            val previousBestWeight = setDao.getPersonalBestWeight(exerciseId) ?: 0.0
            if (maxWeight > previousBestWeight && maxWeight > 0) {
                records.add(
                    PersonalRecord(
                        exerciseId = exerciseId,
                        exerciseName = exercise.name,
                        exerciseNameAr = exercise.nameAr,
                        type = PRType.WEIGHT_PR,
                        value = maxWeight
                    )
                )
            }

            // Check volume PR
            val maxVolume = sets.maxOf { it.weight * it.reps }
            val previousBestVolume = setDao.getPersonalBestVolume(exerciseId) ?: 0.0
            if (maxVolume > previousBestVolume && maxVolume > 0) {
                records.add(
                    PersonalRecord(
                        exerciseId = exerciseId,
                        exerciseName = exercise.name,
                        exerciseNameAr = exercise.nameAr,
                        type = PRType.VOLUME_PR,
                        value = maxVolume
                    )
                )
            }
        }
        return records
    }

    override suspend fun getWorkoutCountThisWeek(): Int {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return sessionDao.getWorkoutCountThisWeek(cal.timeInMillis)
    }

    override suspend fun updateTimer(sessionId: Long, seconds: Int) {
        sessionDao.updateTimer(sessionId, seconds)
    }

    // ═══ Mappers ═══

    private fun WorkoutSessionEntity.toDomain() = WorkoutSession(
        id = id,
        date = date,
        name = name,
        nameAr = nameAr,
        note = note,
        durationSeconds = durationSeconds,
        currentDurationSeconds = currentDurationSeconds,
        totalVolume = totalVolume,
        isCompleted = isCompleted
    )

    private fun WorkoutSetEntity.toDomain() = WorkoutSet(
        id = id,
        workoutId = workoutId,
        exerciseId = exerciseId,
        weight = weight,
        reps = reps,
        rpe = rpe,
        isCompleted = isCompleted,
        setOrder = setOrder
    )

    private fun WorkoutSet.toEntity() = WorkoutSetEntity(
        id = id,
        workoutId = workoutId,
        exerciseId = exerciseId,
        weight = weight,
        reps = reps,
        rpe = rpe,
        isCompleted = isCompleted,
        setOrder = setOrder
    )
}
