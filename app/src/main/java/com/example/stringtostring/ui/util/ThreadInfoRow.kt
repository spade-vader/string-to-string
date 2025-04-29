package com.example.stringtostring.ui.util

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stringtostring.model.ThreadUiModel
import com.example.stringtostring.ui.theme.Dimens
import com.example.stringtostring.ui.theme.StringToStringTheme

@Composable
fun ThreadInfoRow(
    thread: ThreadUiModel,
    percent: Number? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(70.dp, 30.dp)
                .background(Color(android.graphics.Color.parseColor("#"+thread.rgbCode)))
                .border(1.dp, MaterialTheme.colorScheme.onBackground)
        )
        Text(modifier = Modifier
            .padding(start = Dimens.Small, end = Dimens.ExtraSmall),
            text = "${thread.manufacturer} ${thread.colorCode}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground)
        if (percent != null) {
            val percentColor = when {
                percent.toInt() == 100 -> Color(0xFFD3BD60)
                percent.toInt() in 90..99 -> Color(0xFF619466)
                percent.toInt() in 70 .. 89 -> Color(0xFF628D91)
                else -> Color(0xFFB24C38)
            }

            Text(
                text = "$percent%",
                style = MaterialTheme.typography.bodyLarge,
                color = percentColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ThreadInfoRowPreview(
    modifier: Modifier = Modifier
) {
    StringToStringTheme {
        ThreadInfoRow(
            ThreadUiModel("761","f4c4c1", "DMC"),
            92
        )
    }
}