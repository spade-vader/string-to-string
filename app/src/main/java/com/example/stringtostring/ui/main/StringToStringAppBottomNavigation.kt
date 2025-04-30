package com.example.stringtostring.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.stringtostring.util.Screen

@Composable
fun StringToStringAppBottomNavigation(
    items: List<Screen>,
    navController: NavHostController,
    selectedScreen: String
) {

    NavigationBar(
        modifier = Modifier.height(100.dp)
    ) {
        items.forEach { screen ->
            val isSelected = screen.route == selectedScreen

            NavigationBarItem(
                icon = { Icon(
                    imageVector = screen.icon,
                    contentDescription = null
                ) },
                selected = isSelected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = androidx.compose.material3.NavigationBarItemDefaults
                    .colors(
                        selectedIconColor = MaterialTheme.colorScheme.background,
                        indicatorColor = MaterialTheme.colorScheme.primary
                    ),
                modifier = Modifier.fillMaxHeight().then(
                    if (isSelected) {
                        Modifier.background(MaterialTheme.colorScheme.primary)
                    } else {
                        Modifier
                    }
                )
            )
        }
    }
}
