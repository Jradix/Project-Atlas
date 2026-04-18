package com.atlas.logger.data.backup

import androidx.annotation.Keep
import com.atlas.logger.data.local.entity.ExerciseEntity
import com.atlas.logger.data.local.entity.WorkoutSessionEntity
import com.atlas.logger.data.local.entity.WorkoutSetEntity
import com.google.gson.annotations.SerializedName

@Keep
data class AtlasBackup(
    @SerializedName("version")
    val version: Int = 1,
    @SerializedName("exportDate")
    val exportDate: Long = System.currentTimeMillis(),
    @SerializedName("exercises")
    val exercises: List<ExerciseEntity>,
    @SerializedName("sessions")
    val sessions: List<WorkoutSessionEntity>,
    @SerializedName("sets")
    val sets: List<WorkoutSetEntity>
)
