package com.example.stringtostring.ui.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.stringtostring.R

@Composable
fun LoadingScreen(
    titleId: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
            .background(Color.White.copy(alpha = 0.7f))
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(R.drawable.loading),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
            Text(text = stringResource(titleId))
        }

    }
}