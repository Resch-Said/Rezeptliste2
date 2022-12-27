package com.example.rezeptliste2.database.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Rezept")
data class Recipe(
    @PrimaryKey(autoGenerate = true) val r_id: Int,
    var name: String,
    var dauer: Int?,
    var zubereitung: String?,
    val bild: ByteArray?,
)