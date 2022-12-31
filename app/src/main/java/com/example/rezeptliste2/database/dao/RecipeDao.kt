package com.example.rezeptliste2.database.dao

import androidx.room.*
import com.example.rezeptliste2.database.controller.RecipeIngredientController
import com.example.rezeptliste2.database.dto.Recipe

@Dao
interface RecipeDao {

    @Query("SELECT * FROM rezept")
    fun getAll(): List<Recipe>

    @Query("SELECT * FROM rezept WHERE r_id LIKE :rezeptId LIMIT 1")
    fun getByID(rezeptId: Int): Recipe

    @Query("SELECT * FROM rezept WHERE name LIKE :name LIMIT 1")
    fun getByName(name: String): Recipe

    @Update
    fun update(rezept: Recipe)

    @Insert
    fun insert(vararg rezept: Recipe)

    @Delete
    fun delete(rezept: Recipe)
}