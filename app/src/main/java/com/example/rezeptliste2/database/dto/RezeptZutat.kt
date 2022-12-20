package com.example.rezeptliste2.database.dto

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(primaryKeys = ["r_id", "z_id"],
    indices = [Index(value = ["z_id", "r_id"])],
    foreignKeys = [ForeignKey(entity = Rezept::class,
        parentColumns = ["r_id"],
        childColumns = ["r_id"]), ForeignKey(entity = Zutat::class,
        parentColumns = ["z_id"],
        childColumns = ["z_id"])])
data class RezeptZutat(
    val r_id: Int,
    val z_id: Int,
    val menge: String?,
)