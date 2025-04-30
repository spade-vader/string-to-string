package com.example.stringtostring.ui.screens.colorsmatcher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stringtostring.domain.ColorsMaster
import com.example.stringtostring.model.Manufacturer
import com.example.stringtostring.model.ThreadEntity
import com.example.stringtostring.model.ThreadMatch
import com.example.stringtostring.repository.ThreadsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.math.RoundingMode
import javax.inject.Inject

/**
 * ViewModel для экрана сопоставления цветов.
 *
 * Обязанности:
 * - Загрузка производителей и нитей по выбранному производителю.
 * - Хранение выбранного кода цвета и соответствующей нити.
 * - Запуск алгоритма поиска ближайших нитей.
 * - Управление фильтрами (округление процентов, ограничение по производителям).
 *
 * @constructor @Inject внедрение репозитория через Hilt.
 */
@HiltViewModel
class ColorsMatcherViewModel @Inject constructor(
    private val repository: ThreadsRepository
) : ViewModel() {

    // Состояние: все производители, полученные из репозитория
    var manufacturers by mutableStateOf(listOf<Manufacturer>())
        private set

    // Состояние: выбранный производитель
    var selectedManufacturer by mutableStateOf<Manufacturer?>(null)
        private set

    // Состояние: список нитей выбранного производителя
    var threads by mutableStateOf(listOf<ThreadEntity>())
        private set

    // Состояние: выбранный код цвета
    var selectedColorCode by mutableStateOf("")
        private set

    // Состояние: нить, соответствующая введённому коду цвета
    var selectedThread by mutableStateOf<ThreadEntity?>(null)
        private set

    // Состояние: доступность кнопки поиска
    var isSearchButtonEnabled by mutableStateOf(false)
        private set

    // Состояние: результат поиска похожих нитей
    var matches by mutableStateOf<List<ThreadMatch>?>(null)
        private set

    // Флаг: включено ли округление процентов
    var isPercentRound by mutableStateOf(true)
        private set

    // Список производителей, по которым осуществляется поиск
    var manufacturersToSearchIn by mutableStateOf<List<Manufacturer>>(emptyList())
        private set

    /**
     * Инициализация состояния: загрузка производителей.
     */
    init {
        loadManufacturers()
    }

    /**
     * Загрузка всех производителей из репозитория.
     * Автоматическое присвоение списка для поиска.
     */
    private fun loadManufacturers() {
        viewModelScope.launch {
            manufacturers = repository.getAllManufacturers()
            manufacturersToSearchIn = manufacturers
        }
    }

    /**
     * Обработка выбора производителя:
     * - загрузка нитей,
     * - сортировка по числовому значению colorCode (если возможно),
     * - сброс ранее введённого состояния.
     */
    fun onManufacturerSelected(manufacturer: Manufacturer) {
        selectedManufacturer = manufacturer
        viewModelScope.launch {
            threads = repository.getThreadsByManufacturer(manufacturer.id)
                .sortedWith(compareBy { it.colorCode.toIntOrNull() ?: Int.MAX_VALUE })
        }
        selectedColorCode = ""
        selectedThread = null
        isSearchButtonEnabled = false
    }

    /**
     * Обработка изменения ввода кода цвета:
     * - поиск соответствующей нити по коду цвета,
     * - сброс результатов поиска,
     * - активация кнопки поиска при успешном нахождении нити.
     */
    fun onColorCodeInputChanged(input: String) {
        selectedColorCode = input
        isSearchButtonEnabled = false
        viewModelScope.launch {
            selectedThread = threads.find { it.colorCode == selectedColorCode }
            matches = emptyList()
            isSearchButtonEnabled = selectedThread != null
        }
    }

    /**
     * Запуск поиска ближайших нитей через ColorsMaster.
     * Поиск осуществляется по списку выбранных производителей.
     */
    fun findMatches() {
        selectedThread?.let { thread ->
            viewModelScope.launch {
                val manufacturersIds = manufacturersToSearchIn.map { it.id }
                val allThreads = repository.getThreadsByManufacturers(manufacturersIds)

                val result = ColorsMaster.findClosestThreads(
                    targetThread = thread,
                    allThreads = allThreads
                )

                matches = result
            }
        }
    }

    /**
     * Переключение режима округления процентов (вкл./выкл.).
     */
    fun toggleRoundPercent() {
        isPercentRound = !isPercentRound
    }

    /**
     * Получение значения процента в зависимости от текущего режима округления.
     *
     * @param percent значение процента.
     * @return округлённое или точное значение.
     */
    fun getPercent(percent: Double): Number {
        if (isPercentRound) {
            return kotlin.math.floor(percent).toInt()
        }
        return percent.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
    }

    /**
     * Обновление списка производителей, по которым производится поиск.
     *
     * @param selectedManufacturers отфильтрованный список.
     */
    fun updateManufacturersToSearch(selectedManufacturers: List<Manufacturer>) {
        manufacturersToSearchIn = selectedManufacturers
    }
}
