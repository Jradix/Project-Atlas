package com.atlas.logger.data.local.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Exercise entity — represents a single exercise in the library.
 * Stores both English and Arabic names for runtime i18n.
 * IsCustom = true for user-created exercises.
 */
@Keep
@Entity(
    tableName = "exercises",
    indices = [Index(value = ["muscleGroup"]), Index(value = ["name"])]
)
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val id: Long = 0,
    @SerializedName("name")
    val name: String,
    @SerializedName("nameAr")
    val nameAr: String,
    @SerializedName("muscleGroup")
    val muscleGroup: String,
    @SerializedName("muscleGroupAr")
    val muscleGroupAr: String,
    @SerializedName("imageUri")
    val imageUri: String? = null,
    @SerializedName("isCustom")
    val isCustom: Boolean = false
)
