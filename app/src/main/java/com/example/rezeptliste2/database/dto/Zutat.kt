package com.example.rezeptliste2.database.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Zutat(
    @PrimaryKey(autoGenerate = true) val z_id: Int,
    val name: String,
    var isAvailable: Boolean?,
)