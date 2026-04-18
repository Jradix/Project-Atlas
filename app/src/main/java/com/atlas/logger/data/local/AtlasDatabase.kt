package com.atlas.logger.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.atlas.logger.data.local.dao.ExerciseDao
import com.atlas.logger.data.local.dao.WorkoutSessionDao
import com.atlas.logger.data.local.dao.WorkoutSetDao
import com.atlas.logger.data.local.entity.ExerciseEntity
import com.atlas.logger.data.local.entity.WorkoutSessionEntity
import com.atlas.logger.data.local.entity.WorkoutSetEntity

@Database(
    entities = [
        ExerciseEntity::class,
        WorkoutSessionEntity::class,
        WorkoutSetEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AtlasDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutSessionDao(): WorkoutSessionDao
    abstract fun workoutSetDao(): WorkoutSetDao

    companion object {
        const val DATABASE_NAME = "atlas_logger_db"
    }
}
