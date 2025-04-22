package com.example.stringtostring.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "threads",
    foreignKeys = [
        ForeignKey(
            entity = Manufacturer::class,
            parentColumns = ["id"],
            childColumns = ["manufacturer_id"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        )
    ])
data class ThreadEntity (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "color_code") val colorCode: String,
    @ColumnInfo(name = "rgb_code") val rgbCode: String,
    @ColumnInfo(name = "manufacturer_id") val manufacturerId: Int
)