package com.example.stringtostring.util

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object SettingsDataStore {
    private val Context.dataStore by preferencesDataStore(name = "settings")

    val THEME_KEY = booleanPreferencesKey("dark_theme")
    val LANGUAGE_KEY = stringPreferencesKey("language")

    suspend fun setDarkTheme(context: Context, isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDark
        }
    }

    val isDarkThemeFlow: (Context) -> Flow<Boolean> = { context ->
        context.dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: true
        }
    }

    suspend fun saveLanguage(context: Context, language: String) {
        context.dataStore.edit { prefs ->
            prefs[LANGUAGE_KEY] = language
        }
    }

    fun languageFlow(context: Context): Flow<String?> =
        context.dataStore.data
            .map { prefs -> prefs[LANGUAGE_KEY] }
}
