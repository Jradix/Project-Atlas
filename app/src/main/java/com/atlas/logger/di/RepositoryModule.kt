package com.atlas.logger.di

import com.atlas.logger.data.repository.ExerciseRepositoryImpl
import com.atlas.logger.data.repository.WorkoutRepositoryImpl
import com.atlas.logger.domain.repository.ExerciseRepository
import com.atlas.logger.domain.repository.WorkoutRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindExerciseRepository(impl: ExerciseRepositoryImpl): ExerciseRepository

    @Binds
    @Singleton
    abstract fun bindWorkoutRepository(impl: WorkoutRepositoryImpl): WorkoutRepository
}
