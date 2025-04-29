package com.example.stringtostring.ui.screens.colorsmatcher

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.stringtostring.ui.screens.shelve.ShelveViewModel
import com.example.stringtostring.ui.theme.Dimens

@Composable
fun ColorsMatcherScreen(
    viewModel: ColorsMatcherViewModel,
    shelfViewModel: ShelveViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        ColorsMatcherInputScreen(viewModel, shelfViewModel)
        HorizontalDivider(
            thickness = Dimens.ExtraSmall,
            modifier = Modifier
                .padding(Dimens.Small)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.onBackground
        )
        ColorsMatcherOutputScreen(viewModel, shelfViewModel)
    }
}
