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

sealed class Screen(val route: String, val icon: ImageVector) {
    object ColorsMaster : Screen("colorsMaster", Icons.Default.Search)
    object ManufacturersCompare : Screen("manufacturersCompare", Icons.Filled.Menu)
    object Shelves : Screen("shelves", Icons.Filled.Star)
}

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

    composable(
        route = Screen.ColorsMaster.route,
        enterTransition = { slideInHorizontally { fullWidth -> -fullWidth } },
        exitTransition = { slideOutHorizontally { fullWidth -> -fullWidth } }
    )
    {
        ColorsMatcherScreen(colorsMasterViewModel, shelveViewModel)
    }

    composable(
        route = Screen.ManufacturersCompare.route,
        enterTransition = { scaleIn() },
        exitTransition = { scaleOut() }
    ) {
        ManufacturersCompareScreen(manufacturersCompareViewModel)
    }

    composable(
        route = Screen.Shelves.route,
        enterTransition = { slideInHorizontally { fullWidth -> fullWidth } },
        exitTransition = { slideOutHorizontally { fullWidth -> fullWidth } }
    ) {
        ShelveScreen(shelveViewModel)
    }
}
