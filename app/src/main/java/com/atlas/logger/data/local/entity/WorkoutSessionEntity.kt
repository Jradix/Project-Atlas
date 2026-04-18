package com.atlas.logger.data.local.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * WorkoutSession entity — represents a single workout session.
 * Duration is tracked in seconds.
 * TotalVolume is computed when workout finishes (weight × reps summed).
 */
@Keep
@Entity(
    tableName = "workout_sessions",
    indices = [Index(value = ["date"]), Index(value = ["isCompleted"])]
)
data class WorkoutSessionEntity(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val id: Long = 0,
    @SerializedName("date")
    val date: Long = System.currentTimeMillis(),
    @SerializedName("name")
    val name: String = "",
    @SerializedName("nameAr")
    val nameAr: String = "",
    @SerializedName("note")
    val note: String = "",
    @SerializedName("durationSeconds")
    val durationSeconds: Int = 0,
    @SerializedName("currentDurationSeconds")
    val currentDurationSeconds: Int = 0, // Persist current timer progress
    @SerializedName("totalVolume")
    val totalVolume: Double = 0.0,
    @SerializedName("isCompleted")
    val isCompleted: Boolean = false
)
