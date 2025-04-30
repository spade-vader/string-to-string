package com.example.stringtostring.ui.screens.manufacturerscompare

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.stringtostring.R
import com.example.stringtostring.model.Manufacturer
import com.example.stringtostring.model.ThreadPerfectMatch
import com.example.stringtostring.ui.screens.colorsmatcher.ManufacturerDropdown
import com.example.stringtostring.ui.theme.Dimens

/**
 * Экран сравнения производителей нитей:
 * - Отображение ввода или результатов в зависимости от наличия совпадений.
 *
 * @param viewModel ViewModel сравнения производителей.
 * @param modifier внешний модификатор.
 */
@Composable
fun ManufacturersCompareScreen(
    viewModel: ManufacturersCompareViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimens.Small)
    ) {
        if (viewModel.matches.isEmpty()) {
            ManufacturersCompareInputScreen(viewModel)
        } else {
            ManufacturersCompareOutputScreen(viewModel)
        }
    }
}

/**
 * Экран выбора основного и сравниваемых производителей:
 * - Предоставляет выпадающий список с возможностью множественного выбора.
 * - Отображает кнопку поиска после выбора производителей.
 *
 * @param viewModel ViewModel сравнения производителей.
 * @param modifier внешний модификатор.
 */
@Composable
fun ManufacturersCompareInputScreen(
    viewModel: ManufacturersCompareViewModel,
    modifier: Modifier = Modifier
) {
    val selectedTargetManufacturer by viewModel::selectedMainManufacturer
    val selectedManufacturers by viewModel::selectedManufacturers
    var manufacturersMenuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.input_manufacturers_screen_label),
            modifier = modifier.padding(Dimens.Small)
        )

        ManufacturerDropdown(
            manufacturers = viewModel.manufacturers,
            selectedManufacturer = viewModel.selectedMainManufacturer,
            onManufacturerSelected = { viewModel.onMainManufacturerSelected(it) }
        )

        AnimatedVisibility(visible = selectedTargetManufacturer != null) {
            ManufacturersMultiSelectDropdown(
                allManufacturers = viewModel.manufacturers.filter { it != selectedTargetManufacturer },
                selectedManufacturers = viewModel.selectedManufacturers,
                onManufacturerSelected = { manufacturer, isSelected ->
                    viewModel.toggleManufacturerSelection(manufacturer, isSelected)
                }
            )
        }

        AnimatedVisibility(visible = selectedManufacturers.isNotEmpty()) {
            Button(
                onClick = { viewModel.findPerfectMatches() },
                modifier = Modifier.padding(Dimens.Small)
            ) {
                Text(text = stringResource(R.string.find_replacement))
            }
        }
    }
}

/**
 * Экран вывода результатов сравнения производителей:
 * - Отображает таблицу совпадений.
 * - Предоставляет переключатель видимости несовпадений.
 * - Включает кнопку возврата к экрану ввода.
 *
 * @param viewModel ViewModel сравнения производителей.
 * @param modifier внешний модификатор.
 */
@Composable
fun ManufacturersCompareOutputScreen(
    viewModel: ManufacturersCompareViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(Dimens.Small)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier.fillMaxWidth()
        ) {
            Button(onClick = { viewModel.reset() }) {
                Text(text = stringResource(R.string.back))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.show_no_matches))
                Switch(
                    checked = viewModel.isNoMatchShow,
                    onCheckedChange = { viewModel.toggleNoMatchShow() },
                    modifier = Modifier.padding(Dimens.Small)
                )
            }
        }

        MatchesTable(
            targetManufacturer = viewModel.selectedMainManufacturer!!,
            secondaryManufacturers = viewModel.selectedManufacturers,
            matches = viewModel.getFilteredMatches()
        )
    }
}

/**
 * Компонент выпадающего списка с множественным выбором производителей:
 * - Использует OutlinedTextField с ExposedDropdownMenuBox.
 * - Позволяет выбрать несколько элементов.
 *
 * @param allManufacturers список всех производителей.
 * @param selectedManufacturers список выбранных производителей.
 * @param onManufacturerSelected обратный вызов изменения выбора.
 * @param modifier внешний модификатор.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManufacturersMultiSelectDropdown(
    allManufacturers: List<Manufacturer>,
    selectedManufacturers: List<Manufacturer>,
    onManufacturerSelected: (Manufacturer, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val selectedText = if (selectedManufacturers.isEmpty()) "" else {
        selectedManufacturers.joinToString(", ") { it.name }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.replacement_manufacturers)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            allManufacturers.forEach { manufacturer ->
                val isSelected = selectedManufacturers.contains(manufacturer)
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = null
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(manufacturer.name)
                        }
                    },
                    onClick = { onManufacturerSelected(manufacturer, !isSelected) },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

/**
 * Таблица отображения совпадений между производителями нитей:
 * - В первой колонке — цвета и коды нитей целевого производителя.
 * - В остальных — соответствия из других производителей.
 *
 * @param targetManufacturer основной производитель.
 * @param secondaryManufacturers список сравниваемых производителей.
 * @param matches список совпадений по нитям.
 * @param modifier внешний модификатор.
 */
@Composable
fun MatchesTable(
    targetManufacturer: Manufacturer,
    secondaryManufacturers: List<Manufacturer>,
    matches: List<ThreadPerfectMatch>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = targetManufacturer.name,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.labelLarge
        )
        secondaryManufacturers.forEach { manufacturer ->
            Text(
                text = manufacturer.name,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }

    HorizontalDivider(thickness = 1.dp)

    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(matches) { match ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(Color(android.graphics.Color.parseColor("#" + match.thread.rgbCode)))
                        .border(1.dp, MaterialTheme.colorScheme.onBackground)
                )
                Text(
                    text = match.thread.colorCode,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium
                )
                secondaryManufacturers.forEach { manufacturer ->
                    val matchedThread = match.perfectMatches[manufacturer]
                    Text(
                        text = matchedThread?.colorCode ?: "—",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            HorizontalDivider(thickness = 0.5.dp)
        }
    }
}
