package com.example.stringtostring.ui.screens.shelve

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stringtostring.model.ShelfThreadEntity
import com.example.stringtostring.model.ThreadEntity
import com.example.stringtostring.repository.ThreadsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для экрана "Полка":
 * - Управление списком сохранённых нитей.
 * - Добавление и удаление нитей из локального хранилища.
 * - Отслеживание состояния наличия нити на полке.
 *
 * @param repository Репозиторий, обеспечивающий доступ к данным о нитях.
 */
@HiltViewModel
class ShelveViewModel @Inject constructor(
    private val repository: ThreadsRepository
) : ViewModel() {

    /** Текущее состояние полки — список сохранённых нитей. */
    var shelfThreads by mutableStateOf(listOf<ShelfThreadEntity>())

    /** Карта для быстрого определения, находится ли нить на полке. */
    var threadInShelfState by mutableStateOf<Map<Int, Boolean>>(emptyMap())

    /** Инициализация загрузки нитей из локального хранилища. */
    init {
        loadShelfThreads()
    }

    /**
     * Обработка нажатия на иконку удаления:
     * - Удаление нити по ID.
     * - Обновление списка после операции.
     *
     * @param threadId идентификатор нити, подлежащей удалению.
     */
    fun onDeleteThreadClick(threadId: Int) {
        viewModelScope.launch {
            repository.deleteThreadByThreadId(threadId)
            loadShelfThreads()
        }
    }

    /**
     * Обработка нажатия на иконку "избранное":
     * - Добавление нити на полку.
     * - Обновление состояния.
     *
     * @param thread объект нити.
     * @param manufacturerName имя производителя.
     */
    fun onFavoriteIconClick(thread: ThreadEntity, manufacturerName: String) {
        val threadToAdd = ShelfThreadEntity(
            threadId = thread.id,
            colorCode = thread.colorCode,
            rgbCode = thread.rgbCode,
            manufacturerName = manufacturerName
        )

        viewModelScope.launch {
            repository.addShelfThread(threadToAdd)
            loadShelfThreads()
        }
    }

    /**
     * Проверка наличия нити на полке.
     *
     * @param originalThreadId ID проверяемой нити.
     * @return true, если нить сохранена, иначе — false.
     */
    fun isThreadInShelf(originalThreadId: Int): Boolean {
        return threadInShelfState[originalThreadId] ?: false
    }

    /**
     * Загрузка всех нитей, сохранённых на полке:
     * - Обновление списка `shelfThreads`.
     * - Формирование состояния `threadInShelfState` для быстрого доступа.
     */
    private fun loadShelfThreads() {
        viewModelScope.launch {
            val threads = repository.getAllShelfThreads()
            shelfThreads = threads
            threadInShelfState = threads.associateBy({ it.threadId }, { true })
        }
    }
}
