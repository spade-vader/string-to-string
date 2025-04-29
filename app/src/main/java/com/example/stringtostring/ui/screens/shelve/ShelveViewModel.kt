package com.example.stringtostring.ui.screens.shelve

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stringtostring.model.Manufacturer
import com.example.stringtostring.model.ShelfThreadEntity
import com.example.stringtostring.model.ThreadEntity
import com.example.stringtostring.repository.ThreadsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShelveViewModel@Inject constructor(
    private val repository: ThreadsRepository
): ViewModel() {
    var shelfThreads by mutableStateOf(listOf<ShelfThreadEntity>())
    var threadInShelfState by mutableStateOf<Map<Int, Boolean>>(emptyMap())


    init {
        loadShelfThreads()
    }

    fun onDeleteThreadClick(threadId: Int) {
        viewModelScope.launch {
            repository.deleteThreadByThreadId(threadId)
            loadShelfThreads()
        }
    }

    fun onFavoriteIconClick(thread: ThreadEntity, manufacturerName: String) {
        val threadToAdd = ShelfThreadEntity(
            threadId = thread.id,
            colorCode = thread.colorCode,
            rgbCode = thread.rgbCode,
            manufacturerName = manufacturerName)

        viewModelScope.launch {
            repository.addShelfThread(threadToAdd)
            loadShelfThreads()
        }
    }

    fun isThreadInShelf(originalThreadId: Int): Boolean {
        return threadInShelfState[originalThreadId] ?: false
    }

    private fun loadShelfThreads() {
        viewModelScope.launch {
            val threads = repository.getAllShelfThreads()
            shelfThreads = threads

            threadInShelfState = threads.associateBy({ it.threadId }, { true })
        }
    }
}