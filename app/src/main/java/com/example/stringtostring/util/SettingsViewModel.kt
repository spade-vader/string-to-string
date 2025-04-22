package com.example.stringtostring.util

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val _themeFlow = MutableStateFlow(true)
    val themeFlow: StateFlow<Boolean> = _themeFlow.asStateFlow()

    init {
        viewModelScope.launch {
            SettingsDataStore.isDarkThemeFlow(application)
                .collect { newTheme ->
                    _themeFlow.value = newTheme
                }
        }
    }

    fun setTheme(isDarkTheme: Boolean) {
        viewModelScope.launch {
            SettingsDataStore.setDarkTheme(getApplication(), isDarkTheme)
        }
    }
}
