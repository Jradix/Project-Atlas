package com.atlas.logger.util

import java.util.Locale

/**
 * Unit conversion utilities.
 * All weights are stored internally in KG.
 * Display conversion happens at the UI layer via these helpers.
 */
object UnitConverter {
    private const val KG_TO_LBS = 2.20462

    fun kgToLbs(kg: Double): Double = kg * KG_TO_LBS
    fun lbsToKg(lbs: Double): Double = lbs / KG_TO_LBS

    /**
     * Format weight for display.
     * @param kg weight in kilograms
     * @param unit target unit for display
     * @return formatted string like "80" or "176"
     */
    fun formatWeight(kg: Double, unit: WeightUnit): String {
        val value = when (unit) {
            WeightUnit.KG -> kg
            WeightUnit.LBS -> kgToLbs(kg)
        }
        return if (value == value.toLong().toDouble()) {
            String.format(Locale.US, "%.0f", value)
        } else {
            String.format(Locale.US, "%.1f", value)
        }
    }

    /**
     * Format volume (total weight moved) for display.
     */
    fun formatVolume(kg: Double, unit: WeightUnit): String {
        val value = when (unit) {
            WeightUnit.KG -> kg
            WeightUnit.LBS -> kgToLbs(kg)
        }
        return when {
            value >= 1000 -> String.format(Locale.US, "%.1fk", value / 1000)
            else -> String.format(Locale.US, "%.0f", value)
        }
    }

    /**
     * Get unit label.
     */
    fun unitLabel(unit: WeightUnit): String = when (unit) {
        WeightUnit.KG -> "KG"
        WeightUnit.LBS -> "LBS"
    }

    /**
     * Convert a display-entered value back to KG for storage.
     */
    fun toKgForStorage(displayValue: Double, unit: WeightUnit): Double = when (unit) {
        WeightUnit.KG -> displayValue
        WeightUnit.LBS -> lbsToKg(displayValue)
    }
}

enum class WeightUnit {
    KG, LBS
}
