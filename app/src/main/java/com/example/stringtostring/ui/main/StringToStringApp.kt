package com.example.stringtostring.ui.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.stringtostring.ui.screens.colorsmatcher.ColorsMatcherViewModel
import com.example.stringtostring.ui.screens.manufacturerscompare.ManufacturersCompareViewModel
import com.example.stringtostring.ui.screens.shelve.ShelveViewModel
import com.example.stringtostring.ui.theme.StringToStringTheme
import com.example.stringtostring.util.Screen
import com.example.stringtostring.util.SettingsViewModel

/**
 * Главная точка входа приложения с навигацией и управлением темной/светлой темой.
 *
 * Этот Composable компонент управляет глобальной навигацией, темой приложения и отображением основного
 * интерфейса, используя Scaffold для создания стандартного пользовательского интерфейса с верхней и нижней панелью.
 *
 * В зависимости от состояния темы (темная или светлая), выбранного экрана и настроек отображается соответствующий
 * контент с использованием навигации и ViewModel.
 *
 * Параметры:
 * @param colorsMatcherViewModel       ViewModel для экрана сопоставления цветов.
 * @param manufacturersCompareViewModel ViewModel для экрана сравнения производителей.
 * @param shelveViewModel              ViewModel для экрана "Полка".
 * @param viewModelSettings            ViewModel для управления настройками (например, для темы).
 * @param modifier                     Модификатор для кастомизации внешнего вида компонента (по умолчанию пустой).
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun StringToStringApp(
    colorsMatcherViewModel: ColorsMatcherViewModel,
    manufacturersCompareViewModel: ManufacturersCompareViewModel,
    shelveViewModel: ShelveViewModel,
    viewModelSettings: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    // Подписка на состояние темы (темная или светлая).
    val isDarkTheme by viewModelSettings.themeFlow.collectAsState()

    // Инициализация контроллера навигации.
    val navController = rememberNavController()

    // Определение текущего экрана для отображения в нижней навигационной панели.
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val selectedScreen = currentBackStackEntry.value?.destination?.route ?: Screen.ColorsMaster.route

    // Список экранов для нижней навигационной панели.
    val items = listOf(Screen.ColorsMaster, Screen.ManufacturersCompare, Screen.Shelves)

    // Применение темы (светлая/темная) и отображение интерфейса.
    StringToStringTheme(darkTheme = isDarkTheme) {
        Scaffold (
            // Верхняя панель приложения с заголовком.
            topBar = { StringToStringAppBar(viewModelSettings) },

            // Нижняя панель навигации.
            bottomBar = { StringToStringAppBottomNavigation(
                items = items,
                navController = navController,
                selectedScreen = selectedScreen) },

            // Основной контент с навигационным графом.
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
