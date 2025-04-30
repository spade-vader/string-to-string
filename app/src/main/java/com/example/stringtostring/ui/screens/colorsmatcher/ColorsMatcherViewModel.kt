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

@HiltViewModel
class ColorsMatcherViewModel @Inject constructor(
    private val repository: ThreadsRepository
) : ViewModel() {

    // Input
    var manufacturers by mutableStateOf(listOf<Manufacturer>())
        private set
    var selectedManufacturer by mutableStateOf<Manufacturer?>(null)
        private set
    var threads by mutableStateOf(listOf<ThreadEntity>())
        private set
    var selectedColorCode by mutableStateOf("")
        private set
    var selectedThread by mutableStateOf<ThreadEntity?>(null)
        private set
    var isSearchButtonEnabled by mutableStateOf(false)
        private set

    // Output
    var matches by mutableStateOf<List<ThreadMatch>?>(null)
        private set

    // Filter options
    var isPercentRound by mutableStateOf(true)
        private set
    var manufacturersToSearchIn by mutableStateOf<List<Manufacturer>>(emptyList())
        private set

    init {
        loadManufacturers()
    }

    private fun loadManufacturers() {
        viewModelScope.launch {
            manufacturers = repository.getAllManufacturers()
            manufacturersToSearchIn = manufacturers
        }
    }


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

    fun onColorCodeInputChanged(input: String) {
        selectedColorCode = input
        isSearchButtonEnabled = false
        viewModelScope.launch {
            selectedThread = threads.find { it.colorCode == selectedColorCode }
            matches = emptyList()
            isSearchButtonEnabled = selectedThread != null
        }
    }

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

    fun toggleRoundPercent() {
        isPercentRound = !isPercentRound
    }

    fun getPercent(percent: Double): Number {
        if (isPercentRound) {
            return kotlin.math.floor(percent).toInt()
        }
        return percent.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
    }

    fun updateManufacturersToSearch(selectedManufacturers: List<Manufacturer>) {
        manufacturersToSearchIn = selectedManufacturers
    }
}