package com.example.stringtostring.database

import androidx.room.Dao
import androidx.room.Query
import com.example.stringtostring.model.Manufacturer
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
}