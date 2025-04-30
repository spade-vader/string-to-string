package com.example.stringtostring.repository

import com.example.stringtostring.database.ThreadsDao
import com.example.stringtostring.model.Manufacturer
import com.example.stringtostring.model.ShelfThreadEntity
import com.example.stringtostring.model.ThreadEntity

/**
 * Репозиторий для работы с данными о нитях и производителях, взаимодействующий с DAO.
 *
 * Репозиторий представляет собой абстракцию для получения и изменения данных в базе, скрывая детали реализации
 * DAO. Он предоставляет методы для работы с сущностями типа ThreadEntity, Manufacturer и ShelfThreadEntity.
 *
 * Параметры конструктора:
 * @property threadsDao  Объект DAO, предоставляющий методы для работы с таблицами базы данных.
 */
class ThreadsRepository(
    private val threadsDao: ThreadsDao
) {

    /**
     * Получение нити по её уникальному идентификатору.
     *
     * @param threadId  Идентификатор нити.
     * @return ThreadEntity  Объект нити с указанным идентификатором.
     */
    suspend fun getThreadById(threadId: Int): ThreadEntity {
        return threadsDao.getThreadById(threadId)
    }

    /**
     * Получение всех нитей из базы данных.
     *
     * @return List<ThreadEntity>  Список всех нитей.
     */
    suspend fun getAllThreads(): List<ThreadEntity> {
        return threadsDao.getAllThreads()
    }

    /**
     * Получение всех нитей по идентификатору производителя.
     *
     * @param manufacturerId  Идентификатор производителя.
     * @return List<ThreadEntity>  Список нитей указанного производителя.
     */
    suspend fun getThreadsByManufacturer(manufacturerId: Int): List<ThreadEntity> {
        return threadsDao.getThreadsByManufacturer(manufacturerId)
    }

    /**
     * Получение всех нитей по списку идентификаторов производителей.
     *
     * @param manufacturersId  Список идентификаторов производителей.
     * @return List<ThreadEntity>  Список нитей, принадлежащих указанным производителям.
     */
    suspend fun getThreadsByManufacturers(manufacturersId: List<Int>): List<ThreadEntity> {
        return threadsDao.getThreadsByManufacturers(manufacturersId)
    }

    /**
     * Получение всех производителей.
     *
     * @return List<Manufacturer>  Список всех производителей.
     */
    suspend fun getAllManufacturers(): List<Manufacturer> {
        return threadsDao.getAllManufacturers()
    }

    /**
     * Получение производителя по его идентификатору.
     *
     * @param id  Идентификатор производителя.
     * @return Manufacturer  Производитель с указанным идентификатором.
     */
    suspend fun getManufacturerById(id: Int): Manufacturer {
        return threadsDao.getManufacturerById(id)
    }

    /**
     * Получение всех нитей на "полке".
     *
     * @return List<ShelfThreadEntity>  Список всех нитей, добавленных на "полку".
     */
    suspend fun getAllShelfThreads(): List<ShelfThreadEntity> {
        return threadsDao.getAllShelfThreads()
    }

    /**
     * Добавление нити на "полку".
     *
     * @param thread  Нить, которую необходимо добавить на "полку".
     */
    suspend fun addShelfThread(thread: ShelfThreadEntity) {
        threadsDao.addShelfThread(thread)
    }

    /**
     * Удаление нити по её идентификатору.
     *
     * @param threadId  Идентификатор нити, которую необходимо удалить.
     */
    suspend fun deleteThreadByThreadId(threadId: Int) {
        threadsDao.deleteThreadByThreadId(threadId)
    }

    /**
     * Очистка "полки" (удаление всех нитей).
     */
    suspend fun clearShelf() {
        threadsDao.clearShelf()
    }

    /**
     * Проверка наличия нити на "полке".
     *
     * @param originalThreadId  Идентификатор нити.
     * @return Boolean  Возвращает true, если нить присутствует на "полке", иначе false.
     */
    suspend fun isThreadInShelf(originalThreadId: Int): Boolean {
        val count = threadsDao.isThreadInShelf(originalThreadId)
        return count > 0
    }
}
