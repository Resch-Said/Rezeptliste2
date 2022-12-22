package com.example.rezeptliste2.database.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Rezept")
data class Recipe(
    @PrimaryKey(autoGenerate = true) val r_id: Int,
    val name: String,
    val dauer: Int?,
    val zubereitung: String?,
    val bild: ByteArray?,
)