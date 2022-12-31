package com.example.rezeptliste2.database.controller

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.rezeptliste2.MapUtil
import com.example.rezeptliste2.database.Database
import com.example.rezeptliste2.database.dao.RecipeDao
import com.example.rezeptliste2.database.dao.RezeptZutatDao
import com.example.rezeptliste2.database.dao.ZutatDao
import com.example.rezeptliste2.database.dto.Ingredient
import com.example.rezeptliste2.database.dto.Recipe
import com.example.rezeptliste2.database.dto.RecipeIngredient

class RecipeController(context: Context) {
    fun getAllRecipes(): List<Recipe> {
        return rezeptDao.getAll()
    }

    fun getRecipeIngredients(recipe: Recipe): List<Ingredient> {

        return rezeptZutatDao.getRecipeIngredients(recipe.r_id)
    }

    fun getRecipeIngredientsAvailable(recipe: Recipe, available: Boolean = true): List<Ingredient> {

        return rezeptZutatDao.getRecipeIngredientsAvailable(recipe.r_id, available)
    }

    fun getRecipeIngredientAmount(recipe: Recipe, ingredient: Ingredient): String? {

        return rezeptZutatDao.getRecipeIngredientAmount(recipe.r_id, ingredient.z_id)
    }

    fun getRecipeIngredients(
        recipe: Recipe, ingredients: List<Ingredient>
    ): List<RecipeIngredient?> {

        val recipeIngredients: MutableList<RecipeIngredient?> = mutableListOf()

        ingredients.forEach {
            recipeIngredients.add(rezeptZutatDao.getByID(it.z_id, recipe.r_id))
        }

        return recipeIngredients
    }

    fun getRecipeIngredientAmounts(recipe: Recipe, ingredients: List<Ingredient>): List<String> {

        val amounts: MutableList<String> = mutableListOf()

        for (ingredient in ingredients) {
            amounts += rezeptZutatDao.getRecipeIngredientAmount(recipe.r_id, ingredient.z_id)
                ?: "not defined"
        }

        return amounts
    }

    private val recipeIngredientController: RecipeIngredientController =
        RecipeIngredientController(context)

    private val ingredientController: IngredientController = IngredientController(context)

    fun deleteIngredient(recipeID: Int, ingredientID: Int) {
        val recipeIngredient = recipeIngredientController.getByID(ingredientID, recipeID)
        recipeIngredientController.delete(recipeIngredient)
    }

    fun insertIngredient(recipeID: Int, ingredientID: Int, amount: String?) {
        recipeIngredientController.insert(ingredientID, recipeID, amount ?: "not defined")
    }

    private fun updateIngredient(recipeID: Int, ingredientID: Int, amount: String?) {
        val recipeIngredient = recipeIngredientController.getByID(ingredientID, recipeID)
        recipeIngredient.menge = amount
        recipeIngredientController.update(recipeIngredient)
    }

    fun updateRecipeIngredients(recipe: Recipe, recipeIngredientsAmount: MapUtil) {

        rezeptDao.update(recipe)

        getRecipeIngredients(recipe).forEach {
            if (!recipeIngredientsAmount.containsKey(it)) {
                deleteIngredient(recipe.r_id, it.z_id)
            }
        }

        recipeIngredientsAmount.getKeys().forEach {
            if (!ingredientController.getAllIngredients()
                    .contains(it)
            ) { // Wenn die Zutat noch nicht in der Datenbank ist
                Log.i("RecipeController", "inserting ingredient $it")

                ingredientController.insert(it)

                val newIngredient = ingredientController.getByName(it.name)

                if (newIngredient != null) {

                    // Füge Zutat in das Rezept ein
                    recipeIngredientController.insert(
                        newIngredient.z_id, recipe.r_id, recipeIngredientsAmount.getValue(it)!!
                    )

                    Log.i(
                        "RecipeController",
                        "inserting recipeIngredient: ${newIngredient.z_id}, ${recipe.r_id}, ${
                            recipeIngredientsAmount.getValue(
                                it
                            )
                        }"
                    )

                } else {
                    Log.e("RecipeController", "newIngredient is null")
                }

            } else {
                val newIngredient = ingredientController.getByName(it.name)

                // Wenn die Zutat nicht im Rezept ist, füge diese hinzu
                if (newIngredient != null && !getRecipeIngredients(recipe).contains(newIngredient)) {
                    recipeIngredientController.insert(
                        newIngredient.z_id, recipe.r_id, recipeIngredientsAmount.getValue(it)!!
                    )
                } else {
                    // Wenn die Zutat im Rezept ist, aktualisiere die Menge
                    updateIngredient(
                        recipe.r_id, newIngredient!!.z_id, recipeIngredientsAmount.getValue(it)
                    )
                }
            }
        }
    }


    private var zutatDao: ZutatDao
    private var rezeptZutatDao: RezeptZutatDao
    private var rezeptDao: RecipeDao
    private var db: Database

    init {
        this.db = Room.databaseBuilder(context, Database::class.java, "Rezeptliste.db")
            .createFromAsset("Database/Rezeptliste.db").allowMainThreadQueries().build()
        rezeptDao = db.rezeptDao()
        zutatDao = db.zutatDao()
        rezeptZutatDao = db.rezeptZutatDao()
    }


}