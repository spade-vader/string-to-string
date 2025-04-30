package com.example.stringtostring.ui.screens.manufacturerscompare

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stringtostring.domain.ColorsMaster
import com.example.stringtostring.model.Manufacturer
import com.example.stringtostring.model.ThreadPerfectMatch
import com.example.stringtostring.repository.ThreadsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для экрана сравнения производителей:
 * - Управление выбором производителей.
 * - Инициализация и хранение списка всех производителей.
 * - Поиск и фильтрация совпадений по нитям.
 * - Управление состояниями загрузки и отображения несовпадений.
 *
 * @param threadsRepository репозиторий получения данных о нитях и производителях.
 */
@HiltViewModel
class ManufacturersCompareViewModel @Inject constructor(
    private val threadsRepository: ThreadsRepository
) : ViewModel() {

    /** Список всех доступных производителей. */
    var manufacturers by mutableStateOf(listOf<Manufacturer>())
        private set

    /** Выбранный основной производитель. */
    var selectedMainManufacturer by mutableStateOf<Manufacturer?>(null)
        private set

    /** Список производителей для сравнения. */
    var selectedManufacturers by mutableStateOf<List<Manufacturer>>(emptyList())
        private set

    /** Флаг отображения состояния загрузки. */
    var isLoading by mutableStateOf(false)
        private set

    /** Флаг отображения несовпадений в таблице. */
    var isNoMatchShow by mutableStateOf(false)
        private set

    /** Список найденных совпадений по нитям. */
    var matches by mutableStateOf<List<ThreadPerfectMatch>>(emptyList())

    /**
     * Инициализация ViewModel:
     * - Загрузка списка всех производителей из репозитория.
     */
    init {
        viewModelScope.launch {
            val allManufacturers = threadsRepository.getAllManufacturers()
            manufacturers = allManufacturers
        }
    }

    /**
     * Обработка выбора основного производителя:
     * - Обнуление ранее выбранных производителей для сравнения.
     *
     * @param manufacturer основной выбранный производитель.
     */
    fun onMainManufacturerSelected(manufacturer: Manufacturer) {
        selectedMainManufacturer = manufacturer
        selectedManufacturers = emptyList()
    }

    /**
     * Обработка переключения производителя в списке сравниваемых:
     * - Добавление или удаление в зависимости от текущего выбора.
     *
     * @param manufacturer производитель.
     * @param isSelected флаг выбора.
     */
    fun toggleManufacturerSelection(manufacturer: Manufacturer, isSelected: Boolean) {
        val current = selectedManufacturers
        selectedManufacturers = if (isSelected) {
            (current + manufacturer).distinct()
        } else {
            (current - manufacturer)
        }
    }

    /**
     * Переключение флага отображения несовпадений.
     */
    fun toggleNoMatchShow() {
        isNoMatchShow = !isNoMatchShow
    }

    /**
     * Получение отфильтрованных совпадений:
     * - Исключение строк без совпадений, если флаг `isNoMatchShow` выключен.
     *
     * @return список отфильтрованных совпадений.
     */
    fun getFilteredMatches(): List<ThreadPerfectMatch> {
        if (isNoMatchShow) {
            return matches
        }
        return matches.filter { match ->
            match.perfectMatches.values.any { it != null }
        }
    }

    /**
     * Сброс состояния выбора и результатов:
     * - Обнуление выбранных производителей и списка совпадений.
     */
    fun reset() {
        selectedMainManufacturer = null
        selectedManufacturers = emptyList()
        matches = emptyList()
    }

    /**
     * Поиск идеальных совпадений нитей между выбранными производителями:
     * - Получение списка нитей по ID производителей.
     * - Вызов доменной логики сопоставления.
     * - Сортировка результатов по цветовым кодам.
     */
    fun findPerfectMatches() {
        viewModelScope.launch {
            val targetManufacturer = selectedMainManufacturer
            val secondaryManufacturers = selectedManufacturers

            if (targetManufacturer == null || secondaryManufacturers.isEmpty()) {
                return@launch
            }

            isLoading = true

            try {
                val allManufacturerIds = buildList {
                    add(targetManufacturer.id)
                    addAll(secondaryManufacturers.map { it.id })
                }

                val allThreads = threadsRepository.getThreadsByManufacturers(allManufacturerIds)

                val foundMatches = ColorsMaster.manufacturersPerfectMatches(
                    targetManufacturer = targetManufacturer,
                    secondaryManufacturers = secondaryManufacturers,
                    allThreads = allThreads
                )

                matches = foundMatches.sortedWith(
                    compareBy { it.thread.colorCode.toIntOrNull() ?: Int.MAX_VALUE }
                )

            } catch (e: Exception) {
                e.printStackTrace()
                matches = emptyList()
            } finally {
                isLoading = false
            }
        }
    }
}
