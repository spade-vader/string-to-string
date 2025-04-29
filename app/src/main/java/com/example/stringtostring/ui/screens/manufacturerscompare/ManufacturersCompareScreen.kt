package com.example.stringtostring.ui.screens.manufacturerscompare

import android.widget.ToggleButton
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stringtostring.R
import com.example.stringtostring.model.Manufacturer
import com.example.stringtostring.model.ThreadPerfectMatch
import com.example.stringtostring.ui.screens.colorsmatcher.ManufacturerDropdown
import com.example.stringtostring.ui.theme.Dimens

@Composable
fun ManufacturersCompareScreen(
    viewModel: ManufacturersCompareViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(Dimens.Small)
    ) {
        if (viewModel.matches.isEmpty()) {
            ManufacturersCompareInputScreen(viewModel)
        } else {
            ManufacturersCompareOutputScreen(viewModel)
        }
    }
}

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
            modifier = modifier.padding(Dimens.Small))
        ManufacturerDropdown(
            manufacturers = viewModel.manufacturers,
            selectedManufacturer = viewModel.selectedMainManufacturer,
            onManufacturerSelected = { viewModel.onMainManufacturerSelected(it) }
        )
        AnimatedVisibility(
            visible = selectedTargetManufacturer != null
        ) {
            ManufacturersMultiSelectDropdown(
                allManufacturers = viewModel.manufacturers.filter { it != selectedTargetManufacturer },
                selectedManufacturers = viewModel.selectedManufacturers,
                onManufacturerSelected = { manufacturer, isSelected ->
                    viewModel.toggleManufacturerSelection(manufacturer, isSelected) }
            )
        }
        AnimatedVisibility(
            visible = selectedManufacturers.isNotEmpty()
        ) {
            Button(
                onClick = { viewModel.findPerfectMatches() },
                modifier = Modifier.padding(Dimens.Small)
            ) {
                Text(text = stringResource(R.string.find_replacement))
            }
        }
    }
}

@Composable
fun ManufacturersCompareOutputScreen(
    viewModel: ManufacturersCompareViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(Dimens.Small).fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { viewModel.reset() }
            ) {
                Text(text = stringResource(R.string.back))
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManufacturersMultiSelectDropdown(
    allManufacturers: List<Manufacturer>,
    selectedManufacturers: List<Manufacturer>,
    onManufacturerSelected: (Manufacturer, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val selectedText = if (selectedManufacturers.isEmpty()) {
        ""
    } else {
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
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
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
                    onClick = {
                        onManufacturerSelected(manufacturer, !isSelected)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

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
                        .background(Color(android.graphics.Color.parseColor("#"+match.thread.rgbCode)))
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

