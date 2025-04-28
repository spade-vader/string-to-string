package com.example.stringtostring.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.example.stringtostring.R
import com.example.stringtostring.ui.theme.Dimens
import com.example.stringtostring.util.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StringToStringAppBar(
    viewModelSettings: SettingsViewModel
) {
    var menuExpanded by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(248, 240, 237)
                )
            }
        },
        actions = {
            IconButton(onClick = { menuExpanded = true }) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = "Menu",
                    tint = Color(248, 240, 237)
                )
            }
            AppBarMenu(
                viewModelSettings = viewModelSettings,
                expanded = menuExpanded,
                onDismiss = { menuExpanded = false },
                onAboutClick = { showAboutDialog = true }
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )

    if (showAboutDialog) {
        AboutAppDialog(onDismiss = { showAboutDialog = false })
    }
}

@Composable
fun AppBarMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onAboutClick: () -> Unit,
    viewModelSettings: SettingsViewModel
) {
    val isDarkTheme by viewModelSettings.themeFlow.collectAsState()

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        DropdownMenuItem(
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.dark_theme))
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { viewModelSettings.setTheme(!isDarkTheme) },
                        modifier = Modifier.padding(start = Dimens.Small)
                    )
                }
                 },
            onClick = {  }
        )

        DropdownMenuItem(
            text = { Text(stringResource(R.string.about_app)) },
            onClick = {
                onAboutClick()
                onDismiss()
            }
        )
    }
}

@Composable
fun AboutAppDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("OK")
            }
        },
        title = {
            Text(
                text = stringResource(R.string.about_app),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(R.drawable.loading)
                            .decoderFactory(GifDecoder.Factory())
                            .build()
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(150.dp)
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.app_description),
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center
                )
                HorizontalDivider(
                    thickness = 2.dp,
                    modifier = Modifier.padding(Dimens.Medium)
                )
                Text(
                    text = stringResource(R.string.app_version),
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    )
}
