package com.example.stringtostring.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shelf_threads")
data class ShelfThreadEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val threadId: Int,
    val colorCode: String,
    val rgbCode: String,
    val manufacturerName: String
)

