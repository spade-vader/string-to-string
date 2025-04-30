package com.example.stringtostring.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.stringtostring.ui.screens.colorsmatcher.ColorsMatcherViewModel
import com.example.stringtostring.ui.screens.manufacturerscompare.ManufacturersCompareViewModel
import com.example.stringtostring.ui.screens.shelve.ShelveViewModel
import com.example.stringtostring.util.Screen
import com.example.stringtostring.util.appNavGraph

/**
 * Основной навигационный граф для приложения с использованием Jetpack Compose.
 *
 * Этот компонент управляет навигацией между экранами в приложении. В качестве корневого элемента используется
 * компонент NavHost, который связывает навигацию с контроллером (NavHostController) и определяет начальный экран.
 * Навигационные действия и переходы между экранами обрабатываются через вспомогательную функцию `appNavGraph`.
 *
 * Параметры:
 * @param colorsMatcherViewModel           ViewModel для экрана сопоставления цветов.
 * @param manufacturersCompareViewModel    ViewModel для экрана сравнения производителей.
 * @param shelveViewModel                  ViewModel для экрана "Полка" (с нитями).
 * @param navController                    Контроллер навигации для управления переходами между экранами.
 * @param modifier                         Модификатор для настройки внешнего вида компонента (по умолчанию пустой).
 */
@Composable
fun AppNavGraph(
    colorsMatcherViewModel: ColorsMatcherViewModel,
    manufacturersCompareViewModel: ManufacturersCompareViewModel,
    shelveViewModel: ShelveViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.ColorsMaster.route,  // Указание начального экрана для навигации.
        modifier = modifier
    ) {
        // Определение навигационных маршрутов и привязка экранов к их соответствующим ViewModel.
        appNavGraph(
            colorsMasterViewModel = colorsMatcherViewModel,
            manufacturersCompareViewModel = manufacturersCompareViewModel,
            shelveViewModel = shelveViewModel,
            navController = navController
        )
    }
}
