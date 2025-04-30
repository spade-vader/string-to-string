package com.example.stringtostring.ui.screens.colorsmatcher

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
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
import com.example.stringtostring.ui.screens.shelve.ShelveViewModel
import com.example.stringtostring.ui.theme.Dimens
import com.example.stringtostring.ui.util.ThreadInfoRow

@Composable
fun ColorsMatcherOutputScreen(
    viewModel: ColorsMatcherViewModel,
    shelfViewModel: ShelveViewModel,
    modifier: Modifier = Modifier
) {
    val matches by viewModel::matches
    val selectedThread by viewModel::selectedThread

    AnimatedVisibility(
        visible = !matches.isNullOrEmpty() && selectedThread != null,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(Dimens.ExtraSmall),
            horizontalAlignment = Alignment.Start
        ) {
            items(viewModel.matches!!) { match ->
                val manufacturerName =
                    viewModel.manufacturers.find { it.id == match.thread.manufacturerId }?.name
                        ?: "Unknown"
                val percent = viewModel.getPercent(match.percent)

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier.fillMaxWidth()
                ) {
                    ThreadInfoRow(
                        thread = match.toUiModel(manufacturerName),
                        percent = percent
                    )
                    if (shelfViewModel.isThreadInShelf(match.thread.id)) {
                        IconButton(
                            onClick = { shelfViewModel.onDeleteThreadClick(match.thread.id) }
                        ) {
                            Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0xFFD3BD60))
                        }
                    } else {
                        IconButton(
                            onClick = { shelfViewModel.onFavoriteIconClick(match.thread, manufacturerName) }
                        ) {
                            Icon(Icons.Outlined.Star, contentDescription = null)
                        }
                    }
                }
                HorizontalDivider()
            }
        }
    }

    if (matches.isNullOrEmpty() || selectedThread == null) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(R.string.no_matches),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}