package com.example.rezeptliste2.database.dto

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Rezept(
    @PrimaryKey(autoGenerate = true) val r_id: Int,
    val name: String,
    val dauer: Int?,
    val zubereitung: String?,
    val bild: ByteArray?,
)