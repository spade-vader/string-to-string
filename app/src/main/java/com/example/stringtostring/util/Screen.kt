package com.example.stringtostring.util

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.stringtostring.ui.screens.colorsmatcher.ColorsMatcherScreen
import com.example.stringtostring.ui.screens.colorsmatcher.ColorsMatcherViewModel
import com.example.stringtostring.ui.screens.manufacturerscompare.ManufacturersCompareScreen
import com.example.stringtostring.ui.screens.manufacturerscompare.ManufacturersCompareViewModel
import com.example.stringtostring.ui.screens.shelve.ShelveScreen
import com.example.stringtostring.ui.screens.shelve.ShelveViewModel

/**
 * Класс с определением всех навигационных экранов приложения.
 * Каждый экран представлен объектом с route и иконкой для навбаров и табов.
 */
sealed class Screen(val route: String, val icon: ImageVector) {
    object ColorsMaster : Screen("colorsMaster", Icons.Default.Search)
    object ManufacturersCompare : Screen("manufacturersCompare", Icons.Filled.Menu)
    object Shelves : Screen("shelves", Icons.Filled.Star)
}

/**
 * Расширение NavGraphBuilder, реализующее структуру навигации по приложению.
 *
 * Включает переходы к основным экранам:
 * - Поиск ближайших цветов (ColorsMatcherScreen)
 * - Сравнение производителей (ManufacturersCompareScreen)
 * - Сохранённые нити (ShelveScreen)
 *
 * Каждый экран сопровождается анимациями входа и выхода, определёнными через Compose Navigation.
 *
 * @param colorsMasterViewModel ViewModel для экрана поиска ближайших цветов.
 * @param manufacturersCompareViewModel ViewModel для экрана сравнения производителей.
 * @param shelveViewModel ViewModel для экрана с полкой (избранным).
 * @param navController контроллер навигации.
 */
@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.appNavGraph(
    colorsMasterViewModel: ColorsMatcherViewModel,
    manufacturersCompareViewModel: ManufacturersCompareViewModel,
    shelveViewModel: ShelveViewModel,
    navController: NavHostController
) {
    val screens = listOf(
        Screen.ColorsMaster.route,
        Screen.ManufacturersCompare.route,
        Screen.Shelves.route
    )

    // Экран подбора ближайших нитей
    composable(
        route = Screen.ColorsMaster.route,
        enterTransition = { slideInHorizontally { fullWidth -> -fullWidth } },
        exitTransition = { slideOutHorizontally { fullWidth -> -fullWidth } }
    ) {
        ColorsMatcherScreen(colorsMasterViewModel, shelveViewModel)
    }

    // Экран сравнения производителей
    composable(
        route = Screen.ManufacturersCompare.route,
        enterTransition = { scaleIn() },
        exitTransition = { scaleOut() }
    ) {
        ManufacturersCompareScreen(manufacturersCompareViewModel)
    }

    // Экран сохранённых нитей
    composable(
        route = Screen.Shelves.route,
        enterTransition = { slideInHorizontally { fullWidth -> fullWidth } },
        exitTransition = { slideOutHorizontally { fullWidth -> fullWidth } }
    ) {
        ShelveScreen(shelveViewModel)
    }
}
