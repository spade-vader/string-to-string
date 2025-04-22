package com.example.stringtostring.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "manufacturers")
data class Manufacturer(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String
)
