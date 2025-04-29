package com.example.stringtostring.ui.screens.shelve

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.stringtostring.R
import com.example.stringtostring.model.toUiModel
import com.example.stringtostring.ui.theme.Dimens
import com.example.stringtostring.ui.util.ThreadInfoRow

@Composable
fun ShelveScreen(
    viewModel: ShelveViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.shelf_screen_label),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(Dimens.Small)
        )

        HorizontalDivider(thickness = Dimens.ExtraSmall)

        if (viewModel.shelfThreads.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimens.Small),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_shelf_threads),
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                modifier = modifier.padding(Dimens.Small)
            ) {
                items(viewModel.shelfThreads) { thread ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = modifier.fillMaxWidth()
                    ) {
                        ThreadInfoRow(
                            thread = thread.toUiModel()
                        )
                        IconButton(
                            onClick = { viewModel.onDeleteThreadClick(thread.threadId) }
                        ) {
                            Icon(Icons.Filled.Delete, contentDescription = null)
                        }
                    }
                }
            }
        }
    }
}
