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

@HiltViewModel
class ManufacturersCompareViewModel @Inject constructor(
    private val threadsRepository: ThreadsRepository
): ViewModel() {
    var manufacturers by mutableStateOf(listOf<Manufacturer>())
        private set
    var selectedMainManufacturer by mutableStateOf<Manufacturer?>(null)
        private set
    var selectedManufacturers by mutableStateOf<List<Manufacturer>>(emptyList())
        private set
    var isLoading by mutableStateOf<Boolean>(false)
        private set
    var isNoMatchShow by mutableStateOf<Boolean>(false)
        private set
    var matches by mutableStateOf<List<ThreadPerfectMatch>>(emptyList())

    init {
        viewModelScope.launch {
            val allManufacturers = threadsRepository.getAllManufacturers()
            manufacturers = allManufacturers
        }
    }
    fun onMainManufacturerSelected(manufacturer: Manufacturer) {
        selectedMainManufacturer = manufacturer
        selectedManufacturers = emptyList()
    }

    fun toggleManufacturerSelection(manufacturer: Manufacturer, isSelected: Boolean) {
        val current = selectedManufacturers
        selectedManufacturers = if (isSelected) {
            (current + manufacturer).distinct()
        } else {
            (current - manufacturer)
        }
    }

    fun toggleNoMatchShow() {
        isNoMatchShow = !isNoMatchShow
    }

    fun getFilteredMatches(): List<ThreadPerfectMatch> {
        if (isNoMatchShow) {
            return matches
        }
        return matches.filter { match ->
            match.perfectMatches.values.any { it != null }
        }
    }

    fun reset() {
        selectedMainManufacturer = null
        selectedManufacturers = emptyList()
        matches = emptyList()
    }

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

                matches = foundMatches.sortedWith(compareBy { it.thread.colorCode.toIntOrNull() ?: Int.MAX_VALUE })

            } catch (e: Exception) {
                e.printStackTrace()
                matches = emptyList()
            } finally {
                isLoading = false
            }
        }
    }
}