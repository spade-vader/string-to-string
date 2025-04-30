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

/**
 * Нижняя панель навигации, которая отображает элементы меню для переключения между экранами.
 * Каждый элемент меню представляет собой кнопку с иконкой, по нажатию на которую осуществляется
 * переход на соответствующий экран.
 *
 * @param items Список экранов, которые будут отображаться в нижней панели.
 * @param navController Контроллер навигации, используемый для перехода между экранами.
 * @param selectedScreen Маршрут текущего выбранного экрана, используемый для определения
 *                       активного элемента меню.
 */
@Composable
fun StringToStringAppBottomNavigation(
    items: List<Screen>,
    navController: NavHostController,
    selectedScreen: String
) {
    // Нижняя навигационная панель
    NavigationBar(
        modifier = Modifier.height(100.dp) // Установка высоты панели
    ) {
        items.forEach { screen ->
            // Проверка, является ли экран выбранным
            val isSelected = screen.route == selectedScreen

            // Элемент навигации для каждого экрана
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = screen.icon, // Иконка экрана
                        contentDescription = null
                    )
                },
                selected = isSelected, // Проверка, выбран ли данный элемент
                onClick = {
                    // Переход на экран
                    navController.navigate(screen.route) {
                        // Настройки навигации для правильного управления состоянием
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                // Цвета для активного/неактивного состояния
                colors = androidx.compose.material3.NavigationBarItemDefaults
                    .colors(
                        selectedIconColor = MaterialTheme.colorScheme.background, // Цвет иконки при выборе
                        indicatorColor = MaterialTheme.colorScheme.primary // Цвет индикатора активного экрана
                    ),
                // Фон для выбранного элемента меню
                modifier = Modifier.fillMaxHeight().then(
                    if (isSelected) {
                        Modifier.background(MaterialTheme.colorScheme.primary) // Фон для выбранного элемента
                    } else {
                        Modifier // Для невыбранного элемента фона нет
                    }
                )
            )
        }
    }
}
