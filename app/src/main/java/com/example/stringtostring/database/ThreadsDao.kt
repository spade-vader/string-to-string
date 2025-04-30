package com.example.stringtostring.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.stringtostring.model.Manufacturer
import com.example.stringtostring.model.ShelfThreadEntity
import com.example.stringtostring.model.ThreadEntity

/**
 * Интерфейс доступа к данным (DAO) для управления сущностями ниток и производителей в базе данных Room.
 *
 * Отвечает за выполнение SQL-запросов, связанных с таблицами `threads`, `manufacturers` и `shelf_threads`.
 * Использование `suspend` обеспечивает безопасную работу с Room в корутинах.
 */
@Dao
interface ThreadsDao {

    /**
     * Получение полного списка ниток из таблицы `threads`.
     *
     * @return список всех ниток, представленных в базе.
     */
    @Query("SELECT * FROM threads")
    suspend fun getAllThreads(): List<ThreadEntity>

    /**
     * Получение нитки по её уникальному идентификатору.
     *
     * @param id уникальный идентификатор нитки.
     * @return объект `ThreadEntity`, соответствующий запрашиваемому ID.
     */
    @Query("SELECT * FROM threads WHERE id = :id")
    suspend fun getThreadById(id: Int): ThreadEntity

    /**
     * Получение списка ниток, относящихся к определённому производителю.
     *
     * @param manufacturerId идентификатор производителя.
     * @return список ниток данного производителя.
     */
    @Query("SELECT * FROM threads WHERE manufacturer_id = :manufacturerId")
    suspend fun getThreadsByManufacturer(manufacturerId: Int): List<ThreadEntity>

    /**
     * Получение списка ниток от нескольких производителей.
     *
     * @param manufacturersId список идентификаторов производителей.
     * @return список ниток, принадлежащих указанным производителям.
     */
    @Query("SELECT * FROM threads WHERE manufacturer_id IN (:manufacturersId)")
    suspend fun getThreadsByManufacturers(manufacturersId: List<Int>): List<ThreadEntity>

    /**
     * Получение списка всех производителей, зарегистрированных в таблице `manufacturers`.
     *
     * @return список производителей.
     */
    @Query("SELECT * FROM manufacturers")
    suspend fun getAllManufacturers(): List<Manufacturer>

    /**
     * Получение конкретного производителя по его идентификатору.
     *
     * @param id идентификатор производителя.
     * @return объект `Manufacturer`, соответствующий запрашиваемому ID.
     */
    @Query("SELECT * FROM manufacturers WHERE id = :id")
    suspend fun getManufacturerById(id: Int): Manufacturer

    /**
     * Получение всех сохранённых ниток из пользовательской полки (`shelf_threads`).
     *
     * Используется для отображения текущих "закладок" пользователя.
     *
     * @return список объектов `ShelfThreadEntity`, "хранящихся на полке".
     */
    @Query("SELECT * FROM shelf_threads")
    suspend fun getAllShelfThreads(): List<ShelfThreadEntity>

    /**
     * Добавление новой нитки на полку.
     *
     * @param thread объект `ShelfThreadEntity`, подлежащий добавлению.
     */
    @Insert
    suspend fun addShelfThread(thread: ShelfThreadEntity)

    /**
     * Удаление нитки с полки по её исходному ID.
     *
     * Используется для удаления определённой позиции из пользовательского списка.
     *
     * @param threadId ID нитки, которую требуется удалить.
     */
    @Query("DELETE FROM shelf_threads WHERE threadId = :threadId")
    suspend fun deleteThreadByThreadId(threadId: Int)

    /**
     * Полная очистка полки от всех ниток.
     *
     * Применяется при сбросе состояния или инициализации.
     */
    @Query("DELETE FROM shelf_threads")
    suspend fun clearShelf()

    /**
     * Проверка наличия нитки на полке по её исходному ID.
     *
     * Используется для отображения состояния (например, иконки избранного).
     *
     * @param originalThreadId идентификатор нитки из основной таблицы.
     * @return количество записей с данным ID (0 — отсутствие, >0 — наличие).
     */
    @Query("SELECT COUNT(*) FROM shelf_threads WHERE threadId = :originalThreadId")
    suspend fun isThreadInShelf(originalThreadId: Int): Int
}
