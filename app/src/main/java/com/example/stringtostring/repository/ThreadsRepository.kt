package com.example.stringtostring.repository

import com.example.stringtostring.database.ThreadsDao
import com.example.stringtostring.model.Manufacturer
import com.example.stringtostring.model.ThreadEntity

class ThreadsRepository(
    private val threadsDao: ThreadsDao
) {
    suspend fun getThreadById(threadId: Int): ThreadEntity {
        return threadsDao.getThreadById(threadId)
    }

    suspend fun getAllThreads(): List<ThreadEntity> {
        return threadsDao.getAllThreads()
    }

    suspend fun getThreadsByManufacturer(manufacturerId: Int): List<ThreadEntity> {
        return threadsDao.getThreadsByManufacturer(manufacturerId)
    }

    suspend fun getThreadsByManufacturers(manufacturersId: List<Int>): List<ThreadEntity> {
        return threadsDao.getThreadsByManufacturers(manufacturersId)
    }

    suspend fun getAllManufacturers(): List<Manufacturer> {
        return threadsDao.getAllManufacturers()
    }

    suspend fun getManufacturerById(id: Int): Manufacturer {
        return threadsDao.getManufacturerById(id)
    }
}