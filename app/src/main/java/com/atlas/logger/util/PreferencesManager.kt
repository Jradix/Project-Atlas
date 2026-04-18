package com.atlas.logger.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "atlas_settings")

/**
 * Manages user preferences via DataStore.
 * Provides reactive flows for language and weight unit settings.
 */
@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val LANGUAGE_KEY = stringPreferencesKey("language")
        private val WEIGHT_UNIT_KEY = stringPreferencesKey("weight_unit")
    }

    // ═══ Language ═══

    val language: Flow<AppLocale> = context.dataStore.data.map { prefs ->
        when (prefs[LANGUAGE_KEY]) {
            "ar" -> AppLocale.ARABIC
            else -> AppLocale.ENGLISH
        }
    }

    suspend fun setLanguage(locale: AppLocale) {
        context.dataStore.edit { prefs ->
            prefs[LANGUAGE_KEY] = when (locale) {
                AppLocale.ENGLISH -> "en"
                AppLocale.ARABIC -> "ar"
            }
        }
    }

    // ═══ Weight Unit ═══

    val weightUnit: Flow<WeightUnit> = kotlinx.coroutines.flow.flowOf(WeightUnit.KG)

    // LBS system removed — no-op setter
    suspend fun setWeightUnit(unit: WeightUnit) {}

    // Rest Timer removed — feature disabled as requested.
}

enum class AppLocale {
    ENGLISH, ARABIC
}
