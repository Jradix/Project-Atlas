package com.atlas.logger.data.local.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * WorkoutSet entity — a single set within a workout for a specific exercise.
 * Weight is always stored in KG internally.
 * SetOrder ensures correct chronological ordering within an exercise.
 * Foreign keys enforce referential integrity with cascade delete.
 */
@Keep
@Entity(
    tableName = "workout_sets",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutSessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["workoutId"]),
        Index(value = ["exerciseId"])
    ]
)
data class WorkoutSetEntity(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val id: Long = 0,
    @SerializedName("workoutId")
    val workoutId: Long,
    @SerializedName("exerciseId")
    val exerciseId: Long,
    @SerializedName("weight")
    val weight: Double = 0.0,
    @SerializedName("reps")
    val reps: Int = 0,
    @SerializedName("rpe")
    val rpe: Int? = null,
    @SerializedName("isCompleted")
    val isCompleted: Boolean = false,
    @SerializedName("setOrder")
    val setOrder: Int = 0
)
