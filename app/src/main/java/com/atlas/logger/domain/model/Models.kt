package com.atlas.logger.domain.model

/**
 * Domain model for Exercise.
 */
data class Exercise(
    val id: Long = 0,
    val name: String,
    val nameAr: String,
    val muscleGroup: String,
    val muscleGroupAr: String,
    val imageUri: String? = null,
    val isCustom: Boolean = false
)

/**
 * Domain model for a workout session.
 */
data class WorkoutSession(
    val id: Long = 0,
    val date: Long = System.currentTimeMillis(),
    val name: String = "",
    val nameAr: String = "",
    val note: String = "",
    val durationSeconds: Int = 0,
    val currentDurationSeconds: Int = 0,
    val totalVolume: Double = 0.0,
    val isCompleted: Boolean = false,
    val exercises: List<WorkoutExercise> = emptyList()
)

/**
 * A group of sets for a single exercise within a workout.
 */
data class WorkoutExercise(
    val exercise: Exercise,
    val sets: List<WorkoutSet>,
    val firstSetId: Long = 0L // Used for stable sorting in UI
)

/**
 * Domain model for a single set.
 * Weight is stored in KG.
 */
data class WorkoutSet(
    val id: Long = 0,
    val workoutId: Long = 0,
    val exerciseId: Long = 0,
    val weight: Double = 0.0,
    val reps: Int = 0,
    val rpe: Int? = null,
    val isCompleted: Boolean = false,
    val setOrder: Int = 0
)

/**
 * Personal record for an exercise.
 */
data class PersonalRecord(
    val exerciseId: Long,
    val exerciseName: String,
    val exerciseNameAr: String,
    val type: PRType,
    val value: Double
)

enum class PRType {
    WEIGHT_PR,   // New max weight
    VOLUME_PR    // New max volume (weight × reps)
}

/**
 * Weekly volume data point for the dashboard graph.
 */
data class WeeklyVolume(
    val weekStart: Long,
    val totalVolume: Double
)
