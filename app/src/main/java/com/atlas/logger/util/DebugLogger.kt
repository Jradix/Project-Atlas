package com.atlas.logger.util

import android.util.Log
import com.atlas.logger.BuildConfig

/**
 * Debug logger that logs database transactions to IDE console.
 * Only active in debug builds.
 */
object DebugLogger {
    private const val TAG = "[ATLAS]"

    fun log(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message)
        }
    }

    fun logError(message: String, throwable: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, message, throwable)
        }
    }

    fun logDb(operation: String, table: String, details: String = "") {
        if (BuildConfig.DEBUG) {
            val msg = "DB [$operation] $table ${if (details.isNotEmpty()) "→ $details" else ""}"
            Log.d(TAG, msg)
        }
    }
}
