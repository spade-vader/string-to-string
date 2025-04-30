package com.example.stringtostring.ui.screens.colorsmatcher

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.stringtostring.R
import com.example.stringtostring.model.*
import com.example.stringtostring.ui.screens.colorsmatcher.util.SettingsDialog
import com.example.stringtostring.ui.screens.shelve.ShelveViewModel
import com.example.stringtostring.ui.theme.Dimens

/**
 * Экран ввода цвета и выбора производителя для сопоставления нитей.
 *
 * @param viewModel ViewModel с бизнес-логикой для текущего экрана.
 * @param shelveViewModel ViewModel для работы с сохранёнными нитями.
 * @param modifier Дополнительные параметры модификации.
 */
@Composable
fun ColorsMatcherInputScreen(
    viewModel: ColorsMatcherViewModel,
    shelveViewModel: ShelveViewModel,
    modifier: Modifier = Modifier
) {
    val selectedThread by viewModel::selectedThread
    val selectedManufacturer by viewModel::selectedManufacturer

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Отображение заглушки, если нить не выбрана
        AnimatedVisibility(visible = selectedThread == null) {
            Text(
                text = stringResource(R.string.input_screen_label),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(Dimens.Medium)
            )
        }

        // Выпадающий список производителей
        ManufacturerDropdown(
            manufacturers = viewModel.manufacturers,
            selectedManufacturer = selectedManufacturer,
            onManufacturerSelected = { viewModel.onManufacturerSelected(it) },
            modifier = Modifier.fillMaxWidth().padding(Dimens.Small)
        )

        // Поле ввода кода цвета отображается после выбора производителя
        AnimatedVisibility(visible = selectedManufacturer != null) {
            ColorCodeInputField(
                viewModel = viewModel,
                onColorCodeChange = { viewModel.onColorCodeInputChanged(it) },
                threads = viewModel.threads,
                modifier = Modifier.fillMaxWidth().padding(Dimens.Small)
            )
        }

        // Отображение выбранной нити и кнопки поиска замен
        AnimatedVisibility(visible = selectedThread != null) {
            selectedThread?.let {
                val selectedThreadUiModel = it.toUiModel(viewModel.selectedManufacturer!!.name)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    SelectedThread(
                        thread = selectedThreadUiModel,
                        onAddThreadButtonClick = {
                            shelveViewModel.onFavoriteIconClick(it, selectedThreadUiModel.manufacturer)
                        },
                        onDeleteThreadButtonClick = { shelveViewModel.onDeleteThreadClick(it.id) },
                        isOnShelf = shelveViewModel.isThreadInShelf(it.id)
                    )
                    FindButtonAndSearchSettings(
                        onClickButton = { viewModel.findMatches() },
                        viewModel = viewModel,
                        modifier = Modifier.padding(horizontal = Dimens.Medium)
                    )
                }
            }
        }
    }
}

/**
 * Компонент выпадающего списка производителей с Material 3 API.
 *
 * @param manufacturers Список всех производителей.
 * @param selectedManufacturer Текущий выбранный производитель.
 * @param onManufacturerSelected Обработка выбора производителя.
 * @param modifier Модификатор компонента.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManufacturerDropdown(
    manufacturers: List<Manufacturer>,
    selectedManufacturer: Manufacturer?,
    onManufacturerSelected: (Manufacturer) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedManufacturer?.name ?: "",
            onValueChange = {},
            readOnly = true,
            textStyle = MaterialTheme.typography.bodyMedium,
            label = { Text(stringResource(R.string.manufacturer)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            manufacturers.forEach { manufacturer ->
                DropdownMenuItem(
                    text = { Text(text = manufacturer.name) },
                    onClick = {
                        onManufacturerSelected(manufacturer)
                        expanded = false
                    }
                )
            }
        }
    }
}

/**
 * Поле ввода кода цвета с возможностью выбора из всплывающего окна.
 *
 * @param viewModel ViewModel текущего экрана.
 * @param onColorCodeChange Обработка выбора кода цвета.
 * @param threads Список нитей, доступных для выбора.
 * @param modifier Модификатор компонента.
 */
@Composable
fun ColorCodeInputField(
    viewModel: ColorsMatcherViewModel,
    onColorCodeChange: (String) -> Unit,
    threads: List<ThreadEntity>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedThread = viewModel.selectedThread

    Column(modifier = modifier) {
        OutlinedTextField(
            value = selectedThread?.colorCode ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.colorCode)) },
            trailingIcon = {
                Button(
                    onClick = { expanded = true },
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.padding(end = Dimens.Small)
                ) {
                    Text(stringResource(R.string.choose_thread))
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Всплывающее окно со списком нитей
        if (expanded) {
            AlertDialog(
                onDismissRequest = { expanded = false },
                confirmButton = {
                    Button(onClick = { expanded = false }) {
                        Text(
                            text = stringResource(R.string.close),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                text = {
                    LazyColumn {
                        items(threads) { thread ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onColorCodeChange(thread.colorCode)
                                        expanded = false
                                    }
                                    .padding(vertical = 8.dp, horizontal = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(
                                            Color(android.graphics.Color.parseColor("#${thread.rgbCode}")),
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                        .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(thread.colorCode)
                            }
                        }
                    }
                }
            )
        }
    }
}

/**
 * Компонент отображения выбранной нити с цветом и кнопками "в избранное"/"удалить".
 *
 * @param thread UI-модель нити.
 * @param onAddThreadButtonClick Действие при добавлении в избранное.
 * @param onDeleteThreadButtonClick Действие при удалении из избранного.
 * @param isOnShelf Флаг, определяющий наличие нити в сохранённых.
 * @param modifier Модификатор компонента.
 */
@Composable
fun SelectedThread(
    thread: ThreadUiModel,
    onAddThreadButtonClick: () -> Unit,
    onDeleteThreadButtonClick: () -> Unit,
    isOnShelf: Boolean = false,
    modifier: Modifier = Modifier
) {
    val chosenString = stringResource(R.string.chosen_string)
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("$chosenString: ${thread.manufacturer} ${thread.colorCode}")
        Spacer(modifier = Modifier.padding(Dimens.ExtraSmall))
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(
                    color = Color(android.graphics.Color.parseColor("#${thread.rgbCode}")),
                    shape = RoundedCornerShape(4.dp)
                )
                .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
        )

        // Кнопка управления состоянием "избранное"
        IconButton(
            onClick = {
                if (isOnShelf) onDeleteThreadButtonClick()
                else onAddThreadButtonClick()
            }
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = if (isOnShelf) Color(0xFFD3BD60) else LocalContentColor.current
            )
        }
    }
}

/**
 * Компонент с кнопкой запуска поиска и иконкой открытия настроек поиска.
 *
 * @param onClickButton Действие при нажатии на кнопку поиска.
 * @param viewModel ViewModel для управления состоянием настроек.
 * @param modifier Модификатор компонента.
 */
@Composable
fun FindButtonAndSearchSettings(
    onClickButton: () -> Unit,
    viewModel: ColorsMatcherViewModel,
    modifier: Modifier = Modifier
) {
    var isDialogOpen by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onClickButton
        ) {
            Text(text = stringResource(R.string.find_replacement))
        }

        // Кнопка открытия диалога настроек
        IconButton(onClick = { isDialogOpen = true }) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings"
            )
        }

        // Диалог настроек поиска
        if (isDialogOpen) {
            SettingsDialog(
                manufacturers = viewModel.manufacturers,
                selectedManufacturers = viewModel.manufacturersToSearchIn,
                onManufacturersSelected = { viewModel.updateManufacturersToSearch(it) },
                isRoundingEnabled = viewModel.isPercentRound,
                onRoundingToggle = { viewModel.toggleRoundPercent() },
                onDismiss = { isDialogOpen = false }
            )
        }
    }
}
