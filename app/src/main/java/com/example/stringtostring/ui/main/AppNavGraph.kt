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

@Composable
fun AppNavGraph(
    colorsMatcherViewModel: ColorsMatcherViewModel,
    manufacturersCompareViewModel: ManufacturersCompareViewModel,
    shelveViewModel: ShelveViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier) {
    NavHost(
        navController          = navController,
        startDestination       = Screen.ColorsMaster.route,
        modifier               = modifier
    ) {
        appNavGraph(
            colorsMasterViewModel = colorsMatcherViewModel,
            manufacturersCompareViewModel = manufacturersCompareViewModel,
            shelveViewModel = shelveViewModel,
            navController = navController
        )
    }
}
