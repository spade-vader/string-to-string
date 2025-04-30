package com.example.stringtostring.util

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel для управления настройками темы приложения (светлая/тёмная).
 * Позволяет наблюдать за текущей темой и изменять её.
 * Используется для работы с хранилищем данных настроек (SettingsDataStore).
 *
 * @param application экземпляр приложения, передаваемый для работы с контекстом.
 */
class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    // Flow для отслеживания текущей темы (true - тёмная, false - светлая)
    private val _themeFlow = MutableStateFlow(true)
    val themeFlow: StateFlow<Boolean> = _themeFlow.asStateFlow()

    init {
        // Подписка на изменения темы в SettingsDataStore
        viewModelScope.launch {
            SettingsDataStore.isDarkThemeFlow(application)
                .collect { newTheme ->
                    // Обновление состояния при изменении темы
                    _themeFlow.value = newTheme
                }
        }
    }

    /**
     * Метод для изменения текущей темы (светлая/тёмная).
     *
     * @param isDarkTheme флаг, который определяет должна ли быть установлена тёмная тема.
     */
    fun setTheme(isDarkTheme: Boolean) {
        viewModelScope.launch {
            // Сохранение выбранной темы в SettingsDataStore
            SettingsDataStore.setDarkTheme(getApplication(), isDarkTheme)
        }
    }
}
