package com.example.stringtostring.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.stringtostring.model.Manufacturer
import com.example.stringtostring.model.ShelfThreadEntity
import com.example.stringtostring.model.ThreadEntity

@Dao
interface ThreadsDao {
    @Query("SELECT * FROM threads")
    suspend fun getAllThreads(): List<ThreadEntity>
    @Query("SELECT * FROM threads WHERE id = :id")
    suspend fun getThreadById(id: Int): ThreadEntity
    @Query("SELECT * FROM threads WHERE manufacturer_id = :manufacturerId")
    suspend fun getThreadsByManufacturer(manufacturerId: Int): List<ThreadEntity>
    @Query("SELECT * FROM threads WHERE manufacturer_id IN (:manufacturersId)")
    suspend fun getThreadsByManufacturers(manufacturersId: List<Int>): List<ThreadEntity>
    @Query("SELECT * FROM manufacturers")
    suspend fun getAllManufacturers(): List<Manufacturer>
    @Query("SELECT * FROM manufacturers WHERE id = :id")
    suspend fun getManufacturerById(id: Int): Manufacturer
    @Query("SELECT * FROM shelf_threads")
    suspend fun getAllShelfThreads(): List<ShelfThreadEntity>
    @Insert()
    suspend fun addShelfThread(thread: ShelfThreadEntity)
    @Query("DELETE FROM shelf_threads WHERE threadId = :threadId")
    suspend fun deleteThreadByThreadId(threadId: Int)
    @Query("DELETE FROM shelf_threads")
    suspend fun clearShelf()
    @Query("SELECT COUNT(*) FROM shelf_threads WHERE threadId = :originalThreadId")
    suspend fun isThreadInShelf(originalThreadId: Int): Int
}