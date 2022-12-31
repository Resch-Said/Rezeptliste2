package com.example.rezeptliste2.database.dao

import androidx.room.*
import com.example.rezeptliste2.database.dto.Ingredient
import com.example.rezeptliste2.database.dto.RecipeIngredient

@Dao
interface RezeptZutatDao {

    @Query("SELECT * FROM RezeptZutat")
    fun getAll(): List<RecipeIngredient>

    @Query("SELECT * FROM RezeptZutat WHERE z_id LIKE :ingredientID AND r_id LIKE :recipeID LIMIT 1")
    fun getByID(ingredientID: Int, recipeID: Int): RecipeIngredient

    @Query("UPDATE RezeptZutat SET menge = :amount WHERE z_id LIKE :ingredientID AND r_id LIKE :recipeID")
    fun updateAmount(ingredientID: Int, recipeID: Int, amount: String)

    @Query("SELECT * FROM RezeptZutat WHERE z_id NOT IN (SELECT z_id FROM Zutat) OR r_id NOT IN (SELECT r_id FROM Rezept)")
    fun getAllNotInZutatOrRezept(): List<RecipeIngredient>

    @Query("SELECT * FROM Zutat WHERE z_id IN (SELECT z_id FROM RezeptZutat WHERE r_id LIKE :recipeID)")
    fun getRecipeIngredients(recipeID: Int): List<Ingredient>

    @Query("SELECT * FROM Zutat WHERE z_id IN (SELECT z_id FROM RezeptZutat WHERE r_id LIKE :recipeID) AND isAvailable LIKE :available")
    fun getRecipeIngredientsAvailable(recipeID: Int, available: Boolean): List<Ingredient>

    @Query("SELECT menge FROM RezeptZutat WHERE z_id LIKE :ingredientID AND r_id LIKE :recipeID")
    fun getRecipeIngredientAmount(recipeID: Int, ingredientID: Int): String?

    @Query("UPDATE RezeptZutat SET menge = :amount WHERE z_id LIKE :ingredientID AND r_id LIKE :recipeID")
    fun update(ingredientID: Int, recipeID: Int, amount: String)

    @Query("INSERT INTO RezeptZutat (z_id, r_id, menge) VALUES (:ingredientID, :recipeID, :amount)")
    fun insert(ingredientID: Int, recipeID: Int, amount: String)

    @Delete
    fun delete(rezeptZutat: RecipeIngredient)

}