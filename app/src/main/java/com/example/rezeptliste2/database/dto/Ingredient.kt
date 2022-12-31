package com.example.rezeptliste2.database.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Zutat")
data class Ingredient(
    @PrimaryKey(autoGenerate = true) var z_id: Int,
    var name: String,
    var isAvailable: Boolean?,
    val orderID: Int,
)