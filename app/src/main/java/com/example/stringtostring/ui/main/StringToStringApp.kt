package com.example.stringtostring.ui.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.stringtostring.ui.screens.colorsmatcher.ColorsMatcherScreen
import com.example.stringtostring.ui.screens.colorsmatcher.ColorsMatcherViewModel
import com.example.stringtostring.ui.screens.manufacturerscompare.ManufacturersCompareScreen
import com.example.stringtostring.ui.screens.manufacturerscompare.ManufacturersCompareViewModel
import com.example.stringtostring.ui.screens.shelve.ShelveViewModel
import com.example.stringtostring.ui.theme.StringToStringTheme
import com.example.stringtostring.util.Screen
import com.example.stringtostring.util.SettingsViewModel
import com.example.stringtostring.util.appNavGraph
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun StringToStringApp(
    colorsMatcherViewModel: ColorsMatcherViewModel,
    manufacturersCompareViewModel: ManufacturersCompareViewModel,
    shelveViewModel: ShelveViewModel,
    viewModelSettings: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    val isDarkTheme by viewModelSettings.themeFlow.collectAsState()

    val navController = rememberNavController()
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val selectedScreen = currentBackStackEntry.value?.destination?.route ?: Screen.ColorsMaster.route
    val items = listOf(Screen.ColorsMaster, Screen.ManufacturersCompare, Screen.Shelves)

    StringToStringTheme(darkTheme = isDarkTheme) {
        Scaffold (
            topBar = { StringToStringAppBar(viewModelSettings) },
            bottomBar = { StringToStringAppBottomNavigation(
                items = items,
                navController = navController,
                selectedScreen = selectedScreen) },
            content = { paddingValues ->
                AppNavGraph(
                    colorsMatcherViewModel = colorsMatcherViewModel,
                    manufacturersCompareViewModel = manufacturersCompareViewModel,
                    shelveViewModel = shelveViewModel,
                    navController = navController,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        )
    }
}