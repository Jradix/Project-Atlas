package com.atlas.logger.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

/**
 * Haptic feedback helper — provides tactile feedback on key actions.
 * Short vibration on set completion, gentle tick on UI interactions.
 */
class HapticHelper(context: Context) {

    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        manager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    /**
     * Short vibration when a set is completed (checkmark tap).
     */
    fun onSetCompleted() {
        vibrate(50)
    }

    /**
     * Light tick for general UI interactions.
     */
    fun tick() {
        vibrate(10)
    }

    /**
     * Strong vibration for workout completion / PR.
     */
    fun onWorkoutCompleted() {
        vibrate(100)
    }

    private fun vibrate(durationMs: Long) {
        vibrator?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                it.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                it.vibrate(durationMs)
            }
        }
    }
}
