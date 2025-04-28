package com.example.stringtostring.ui.screens.colorsmatcher

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.stringtostring.R
import com.example.stringtostring.model.Manufacturer
import com.example.stringtostring.model.ThreadEntity
import com.example.stringtostring.model.ThreadUiModel
import com.example.stringtostring.model.toUiModel
import com.example.stringtostring.ui.screens.colorsmatcher.util.SettingsDialog
import com.example.stringtostring.ui.theme.Dimens

@Composable
fun ColorsMatcherInputScreen(
    viewModel: ColorsMatcherViewModel,
    modifier: Modifier = Modifier
) {
    val selectedThread by viewModel::selectedThread
    val selectedManufacturer by viewModel::selectedManufacturer

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(
            visible = selectedThread == null
        ) {
            Text(text = stringResource(R.string.input_screen_label),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(Dimens.Medium))
        }

        ManufacturerDropdown(
            manufacturers = viewModel.manufacturers,
            selectedManufacturer = viewModel.selectedManufacturer,
            onManufacturerSelected = { viewModel.onManufacturerSelected(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.Small)
        )

        AnimatedVisibility(
            visible = selectedManufacturer != null
        ) {
            ColorCodeInputField(
                viewModel = viewModel,
                onColorCodeChange = { viewModel.onColorCodeInputChanged(it) },
                threads = viewModel.threads,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.Small)
            )
        }

        AnimatedVisibility(
            visible = selectedThread != null
        ) {
            if (selectedThread != null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val selectedThreadUiModel = viewModel.selectedThread!!.toUiModel(viewModel.selectedManufacturer!!.name)
                    SelectedThread(thread = selectedThreadUiModel)
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
                    text = { Text(
                        text = manufacturer.name) },
                    onClick = {
                        onManufacturerSelected(manufacturer)
                        expanded = false
                    }
                )
            }
        }
    }
}

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

        if (expanded) {
            AlertDialog(
                onDismissRequest = { expanded = false },
                confirmButton = {
                    Button(onClick = { expanded = false }) {
                        Text(
                            text = stringResource(R.string.close),
                            style = MaterialTheme.typography.bodySmall)
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

@Composable
fun SelectedThread(
    thread: ThreadUiModel,
    modifier: Modifier = Modifier
) {
    val chosenString = stringResource(R.string.chosen_string)
    Row() {
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
    }
}

@Composable
fun FindButtonAndSearchSettings(
    onClickButton: () -> Unit,
    viewModel: ColorsMatcherViewModel,
    modifier: Modifier = Modifier
) {
    var isDialogOpen by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onClickButton,
            modifier = Modifier
        ) {
            Text(text = stringResource(R.string.find_replacement))
        }

        IconButton(onClick = { isDialogOpen = true }) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings"
            )
        }

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
