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

/**
 * Компонент отображения информации о нити:
 * - Цветовой прямоугольник, код цвета и производитель.
 * - Процент совпадения с основной нитью (если передан).
 *
 * @param thread модель нити для отображения.
 * @param percent необязательный процент совпадения (например, при сравнении).
 * @param modifier модификатор компоновки.
 */
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
        // Отображение прямоугольника цвета на основе RGB-кода
        Box(
            modifier = Modifier
                .size(70.dp, 30.dp)
                .background(Color(android.graphics.Color.parseColor("#" + thread.rgbCode)))
                .border(1.dp, MaterialTheme.colorScheme.onBackground)
        )

        // Отображение производителя и кода цвета
        Text(
            modifier = Modifier
                .padding(start = Dimens.Small, end = Dimens.ExtraSmall),
            text = "${thread.manufacturer} ${thread.colorCode}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        // Условное отображение процента совпадения с цветовой маркировкой
        if (percent != null) {
            val percentColor = when {
                percent.toInt() == 100 -> Color(0xFFD3BD60) // золото
                percent.toInt() in 90..99 -> Color(0xFF619466) // зелёный
                percent.toInt() in 70..89 -> Color(0xFF628D91) // синий
                else -> Color(0xFFB24C38) // красный
            }

            Text(
                text = "$percent%",
                style = MaterialTheme.typography.bodyLarge,
                color = percentColor
            )
        }
    }
}

/**
 * Превью-компонент для визуальной оценки компонента ThreadInfoRow.
 */
@Preview(showBackground = true)
@Composable
fun ThreadInfoRowPreview(
    modifier: Modifier = Modifier
) {
    StringToStringTheme {
        ThreadInfoRow(
            thread = ThreadUiModel("761", "f4c4c1", "DMC"),
            percent = 92
        )
    }
}
