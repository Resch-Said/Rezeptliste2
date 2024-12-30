package com.example.rezeptliste2.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.rezeptliste2.database.dao.RecipeDao
import com.example.rezeptliste2.database.dao.RezeptZutatDao
import com.example.rezeptliste2.database.dao.ZutatDao
import com.example.rezeptliste2.database.dto.Recipe
import com.example.rezeptliste2.database.dto.RecipeIngredient
import com.example.rezeptliste2.database.dto.Ingredient


@Database(entities = [Recipe::class, Ingredient::class, RecipeIngredient::class], version = 4)
abstract class Database : RoomDatabase() {
    abstract fun rezeptDao(): RecipeDao
    abstract fun zutatDao(): ZutatDao
    abstract fun rezeptZutatDao(): RezeptZutatDao
}
