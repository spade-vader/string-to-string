package com.example.stringtostring.util

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Объект для работы с DataStore, который используется для хранения настроек приложения.
 * Хранит информацию о предпочтениях пользователя, таких как тема (светлая/тёмная) и язык.
 */
object SettingsDataStore {
    // Создание DataStore с именем "settings" для хранения настроек
    private val Context.dataStore by preferencesDataStore(name = "settings")

    // Ключ для хранения настроек темы (светлая/тёмная)
    val THEME_KEY = booleanPreferencesKey("dark_theme")

    // Ключ для хранения настроек языка
    val LANGUAGE_KEY = stringPreferencesKey("language")

    /**
     * Сохранение состояния темы (тёмная/светлая) в DataStore.
     *
     * @param context контекст, используемый для доступа к DataStore.
     * @param isDark флаг, указывающий, должна ли быть активирована тёмная тема.
     */
    suspend fun setDarkTheme(context: Context, isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDark
        }
    }

    /**
     * Возвращает поток, который отслеживает состояние темы (тёмная/светлая).
     *
     * @param context контекст, используемый для доступа к DataStore.
     * @return Flow<Boolean>, который предоставляет состояние тёмной темы.
     */
    val isDarkThemeFlow: (Context) -> Flow<Boolean> = { context ->
        context.dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: true  // Если нет значения, по умолчанию использовать тёмную тему
        }
    }

    /**
     * Сохранение выбранного языка в DataStore.
     *
     * @param context контекст, используемый для доступа к DataStore.
     * @param language строка, представляющая выбранный язык.
     */
    suspend fun saveLanguage(context: Context, language: String) {
        context.dataStore.edit { prefs ->
            prefs[LANGUAGE_KEY] = language
        }
    }

    /**
     * Возвращает поток, который отслеживает язык, сохранённый в DataStore.
     *
     * @param context контекст, используемый для доступа к DataStore.
     * @return Flow<String?> поток, который предоставляет сохранённый язык.
     */
    fun languageFlow(context: Context): Flow<String?> =
        context.dataStore.data
            .map { prefs -> prefs[LANGUAGE_KEY] }
}
