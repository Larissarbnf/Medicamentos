package com.example.medicamentos.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "settings"

// property delegate for DataStore
val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

object ThemePreferences {
    val DARK_MODE = booleanPreferencesKey("dark_mode")
}

suspend fun saveDarkMode(context: Context, enabled: Boolean) {
    context.dataStore.edit { prefs ->
        prefs[ThemePreferences.DARK_MODE] = enabled
    }
}

fun darkModeFlow(context: Context): Flow<Boolean> =
    context.dataStore.data.map { prefs ->
        prefs[ThemePreferences.DARK_MODE] ?: true
    }
