package com.example.rezeptliste2.database.dto

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(primaryKeys = ["r_id", "z_id"], tableName = "RezeptZutat",
    indices = [Index(value = ["z_id", "r_id"])],
    foreignKeys = [ForeignKey(entity = Recipe::class,
        parentColumns = ["r_id"],
        childColumns = ["r_id"]), ForeignKey(entity = Ingredient::class,
        parentColumns = ["z_id"],
        childColumns = ["z_id"])])
data class RecipeIngredient(
    val r_id: Int,
    val z_id: Int,
    var menge: String?,
)