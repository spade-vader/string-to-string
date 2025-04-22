package com.example.stringtostring.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.stringtostring.ui.screens.colorsmatcher.ColorsMatcherScreen
import com.example.stringtostring.ui.screens.colorsmatcher.ColorsMatcherViewModel
import com.example.stringtostring.ui.theme.StringToStringTheme
import com.example.stringtostring.util.SettingsViewModel

@Composable
fun StringToStringApp(
    viewModel: ColorsMatcherViewModel,
    viewModelSettings: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    val isDarkTheme by viewModelSettings.themeFlow.collectAsState()

    StringToStringTheme(darkTheme = isDarkTheme) {
        Scaffold (
            topBar = { StringToStringAppBar(viewModelSettings) },
            content = { paddingValues ->
                ColorsMatcherScreen(
                    viewModel = viewModel,
                    modifier = Modifier.padding(paddingValues))
            }
        )
    }
}