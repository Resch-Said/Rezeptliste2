package com.example.rezeptliste2.database.dao

import androidx.room.*
import com.example.rezeptliste2.database.dto.Recipe

@Dao
interface RecipeDao {

    @Query("SELECT * FROM rezept")
    fun getAll(): List<Recipe>

    @Query("SELECT * FROM rezept WHERE r_id LIKE :rezeptId LIMIT 1")
    fun getByID(rezeptId: Int): Recipe?

    @Query("SELECT * FROM rezept WHERE name LIKE :name LIMIT 1")
    fun getByName(name: String): Recipe

    @Query("SELECT * FROM Rezept WHERE r_id IN (SELECT max(r_id) FROM Rezept)")
    fun getLast(): Recipe

    @Query("SELECT max(z_id) FROM RezeptZutat WHERE r_id LIKE :recipeID")
    fun getLastIngredientID(recipeID: Int): Int

    @Update
    fun update(rezept: Recipe)

    @Query("INSERT INTO rezept (name, dauer, zubereitung, bild) VALUES (:name, :dauer, :zubereitung, :bild)")
    fun insert(name: String, dauer: Int?, zubereitung: String?, bild: String?)

    @Delete
    fun delete(rezept: Recipe)
}
