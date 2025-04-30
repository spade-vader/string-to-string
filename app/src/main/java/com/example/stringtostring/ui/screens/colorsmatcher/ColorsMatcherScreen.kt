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

/**
 * Экран сопоставления цветов, объединяющий компоненты ввода и вывода.
 *
 * Состав:
 * - [ColorsMatcherInputScreen]: отображение выпадающего списка производителей,
 *   поля для выбора цветового кода и отображение выбранной нити.
 * - Разделительная линия между блоками ввода и вывода.
 * - [ColorsMatcherOutputScreen]: отображение подходящих нитей по цвету.
 *
 * Параметры:
 * @param viewModel ViewModel, содержащий логику выбора и сопоставления нитей.
 * @param shelfViewModel ViewModel, управляющий состоянием сохранённых нитей (полка).
 * @param modifier модификатор для внешней настройки компонента.
 */
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
        // Отображение секции выбора производителя и цветового кода
        ColorsMatcherInputScreen(viewModel, shelfViewModel)

        // Отображение горизонтального разделителя между секциями
        HorizontalDivider(
            thickness = Dimens.ExtraSmall,
            modifier = Modifier
                .padding(Dimens.Small)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.onBackground
        )

        // Отображение подходящих нитей-заменителей
        ColorsMatcherOutputScreen(viewModel, shelfViewModel)
    }
}
