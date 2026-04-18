package com.atlas.logger.di

import android.content.Context
import androidx.room.Room
import com.atlas.logger.data.local.AtlasDatabase
import com.atlas.logger.data.local.dao.ExerciseDao
import com.atlas.logger.data.local.dao.WorkoutSessionDao
import com.atlas.logger.data.local.dao.WorkoutSetDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AtlasDatabase {
        return Room.databaseBuilder(
            context,
            AtlasDatabase::class.java,
            AtlasDatabase.DATABASE_NAME
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    fun provideExerciseDao(db: AtlasDatabase): ExerciseDao = db.exerciseDao()

    @Provides
    fun provideWorkoutSessionDao(db: AtlasDatabase): WorkoutSessionDao = db.workoutSessionDao()

    @Provides
    fun provideWorkoutSetDao(db: AtlasDatabase): WorkoutSetDao = db.workoutSetDao()
}
