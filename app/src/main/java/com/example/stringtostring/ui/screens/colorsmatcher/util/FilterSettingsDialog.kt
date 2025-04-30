package com.example.stringtostring.ui.screens.colorsmatcher.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.stringtostring.R
import com.example.stringtostring.model.Manufacturer

/**
 * Диалоговое окно, в котором пользователь может выбрать настройки для поиска производителей.
 * Включает возможность выбора нескольких производителей и включения/выключения округления процентов.
 *
 * @param manufacturers Список доступных производителей.
 * @param selectedManufacturers Список уже выбранных производителей.
 * @param onManufacturersSelected Колбэк, который срабатывает при изменении выбранных производителей.
 * @param isRoundingEnabled Состояние, указывающее, включено ли округление процентов.
 * @param onRoundingToggle Колбэк, который срабатывает при изменении состояния округления.
 * @param onDismiss Колбэк для закрытия диалогового окна.
 */
@Composable
fun SettingsDialog(
    manufacturers: List<Manufacturer>,
    selectedManufacturers: List<Manufacturer>,
    onManufacturersSelected: (List<Manufacturer>) -> Unit,
    isRoundingEnabled: Boolean,
    onRoundingToggle: () -> Unit,
    onDismiss: () -> Unit
) {
    // Состояние для управления открытием/закрытием выпадающего меню выбора производителей
    var expanded by remember { mutableStateOf(false) }
    // Список выбранных производителей
    val selected = remember { mutableStateListOf<Manufacturer>().apply { addAll(selectedManufacturers) } }

    // Диалоговое окно с настройками
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {
                // Сохранение выбранных производителей при подтверждении
                onManufacturersSelected(selected)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        title = {
            Text(text = stringResource(R.string.search_settings)) // Заголовок диалога
        },
        text = {
            // Содержимое диалогового окна
            Column {
                // Секция выбора производителей
                Text("${stringResource(R.string.manufacturers)}:")
                Spacer(Modifier.height(8.dp))
                Box {
                    // Кнопка для открытия выпадающего меню с выбором производителей
                    Button(onClick = { expanded = !expanded }) {
                        Text(stringResource(R.string.choose_manufacturers))
                    }
                    // Выпадающее меню для выбора производителей
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        manufacturers.forEach { manufacturer ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        // Чекбокс для каждого производителя
                                        Checkbox(
                                            checked = selected.contains(manufacturer),
                                            onCheckedChange = {
                                                // Добавление или удаление производителя из выбранных
                                                if (it) selected.add(manufacturer)
                                                else selected.remove(manufacturer)
                                            }
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text(manufacturer.name) // Название производителя
                                    }
                                },
                                onClick = { }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Секция с переключателем для округления процентов
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(stringResource(R.string.round_percent)) // Текст для переключателя
                    Spacer(Modifier.weight(1f))
                    Switch(
                        checked = isRoundingEnabled,
                        onCheckedChange = { onRoundingToggle() } // Изменение состояния округления
                    )
                }

                Spacer(Modifier.height(20.dp))

                // Текст, объясняющий необходимость применения настроек
                Text(
                    text = stringResource(R.string.to_apply_settings),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    )
}
