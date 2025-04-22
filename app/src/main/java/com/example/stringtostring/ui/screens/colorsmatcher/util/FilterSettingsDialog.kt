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

@Composable
fun SettingsDialog(
    manufacturers: List<Manufacturer>,
    selectedManufacturers: List<Manufacturer>,
    onManufacturersSelected: (List<Manufacturer>) -> Unit,
    isRoundingEnabled: Boolean,
    onRoundingToggle: () -> Unit,
    onDismiss: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selected = remember { mutableStateListOf<Manufacturer>().apply { addAll(selectedManufacturers) } }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button (onClick = {
                onManufacturersSelected(selected)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        title = { Text(
            text = stringResource(R.string.search_settings)
            ) },
        text = {
            Column {
                Text("${stringResource(R.string.manufacturers)}:")
                Spacer(Modifier.height(8.dp))
                Box {
                    Button(onClick = { expanded = !expanded }) {
                        Text(stringResource(R.string.choose_manufacturers))
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        manufacturers.forEach { manufacturer ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Checkbox(
                                            checked = selected.contains(manufacturer),
                                            onCheckedChange = {
                                                if (it) selected.add(manufacturer)
                                                else selected.remove(manufacturer)
                                            }
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text(manufacturer.name)
                                    }
                                },
                                onClick = { }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(stringResource(R.string.round_percent))
                    Spacer(Modifier.weight(1f))
                    Switch(
                        checked = isRoundingEnabled,
                        onCheckedChange = { onRoundingToggle() }
                    )
                }

                Spacer(Modifier.height(20.dp))
                Text(
                    text = stringResource(R.string.to_apply_settings),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    )
}