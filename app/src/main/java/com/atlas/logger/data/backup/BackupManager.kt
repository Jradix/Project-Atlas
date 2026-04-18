package com.atlas.logger.data.backup

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.atlas.logger.data.local.dao.ExerciseDao
import com.atlas.logger.data.local.dao.WorkoutSessionDao
import com.atlas.logger.data.local.dao.WorkoutSetDao
import com.atlas.logger.data.local.entity.ExerciseEntity
import com.atlas.logger.data.local.entity.WorkoutSessionEntity
import com.atlas.logger.data.local.entity.WorkoutSetEntity
import com.atlas.logger.util.DebugLogger
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handles JSON export and import of all app data.
 * Export writes to Downloads/atlas_backup.json.
 * Import reads from a user-selected .json file.
 */
@Singleton
class BackupManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val exerciseDao: ExerciseDao,
    private val sessionDao: WorkoutSessionDao,
    private val setDao: WorkoutSetDao
) {
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()



    /**
     * Export all data to Downloads/atlas_backup.json.
     * Returns true on success.
     */
    suspend fun exportData(): Result<Unit> = runCatching {
        // Gather all data
        val exercises = exerciseDao.getAllExercisesOnce()
        val sessions = sessionDao.getAllSessionsOnce()
        val sets = setDao.getAllSetsOnce()

        val backup = AtlasBackup(
            exercises = exercises,
            sessions = sessions,
            sets = sets
        )

        val json = gson.toJson(backup)

        // Write to Downloads folder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "atlas_backup.json")
                put(MediaStore.MediaColumns.MIME_TYPE, "application/json")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            val resolver = context.contentResolver
            // Delete existing file if present
            resolver.delete(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                "${MediaStore.MediaColumns.DISPLAY_NAME} = ?",
                arrayOf("atlas_backup.json")
            )

            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                ?: throw Exception("Failed to create file in Downloads")

            resolver.openOutputStream(uri)?.use { out ->
                out.write(json.toByteArray())
            } ?: throw Exception("Failed to open output stream")
        } else {
            @Suppress("DEPRECATION")
            val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = java.io.File(dir, "atlas_backup.json")
            file.writeText(json)
        }

        DebugLogger.log("Exported backup: ${exercises.size} exercises, ${sessions.size} sessions, ${sets.size} sets")
    }

    /**
     * Import data from a JSON Uri.
     * Merges (appends) data with existing data.
     */
    suspend fun importData(uri: Uri): Result<Unit> = runCatching {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw Exception("Cannot open file")

        val json = BufferedReader(InputStreamReader(inputStream)).use { it.readText() }
        val backup = try {
            gson.fromJson(json, AtlasBackup::class.java)
        } catch (e: Exception) {
            throw Exception("Failed to parse JSON: ${e.message}")
        }

        if (backup == null || backup.exercises.isEmpty()) {
            throw Exception("Backup file is empty or invalid")
        }

        // ═══ Mapping Strategy for Merging ═══
        
        // 1. Map existing exercises by name to avoid duplicates
        val existingExercises = exerciseDao.getAllExercisesOnce()
        val exerciseNameMap = existingExercises.associateBy { it.name.lowercase() }
        val exerciseIdMap = mutableMapOf<Long, Long>() // Old ID -> New ID

        backup.exercises.forEach { backupEx ->
            val existing = exerciseNameMap[backupEx.name.lowercase()]
            if (existing != null) {
                exerciseIdMap[backupEx.id] = existing.id
            } else {
                // New exercise, insert and get ID
                val newId = exerciseDao.insert(backupEx.copy(id = 0)) // id=0 lets Room generate a new one
                exerciseIdMap[backupEx.id] = newId
            }
        }

        // 2. Insert sessions and map IDs
        val sessionIdMap = mutableMapOf<Long, Long>() // Old ID -> New ID
        backup.sessions.forEach { backupSession ->
            // Insert as new session
            val newSessionId = sessionDao.insert(backupSession.copy(id = 0))
            sessionIdMap[backupSession.id] = newSessionId
        }

        // 3. Insert sets with updated mapping
        val newSets = backup.sets.mapNotNull { backupSet ->
            val newSessionId = sessionIdMap[backupSet.workoutId]
            val newExerciseId = exerciseIdMap[backupSet.exerciseId]
            
            if (newSessionId != null && newExerciseId != null) {
                backupSet.copy(
                    id = 0,
                    workoutId = newSessionId,
                    exerciseId = newExerciseId
                )
            } else {
                null
            }
        }
        
        if (newSets.isNotEmpty()) {
            setDao.insertAll(newSets)
        }

        DebugLogger.log("Imported backup (Merged): ${backup.exercises.size} source exercises, ${backup.sessions.size} sessions, ${newSets.size} sets")
    }
}
