package com.example.rezeptliste2.database.controller

import android.content.Context
import androidx.room.Room
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

    fun getRecipeIngredients(recipe: Recipe, ingredients: List<Ingredient>): List<RecipeIngredient?> {

        val recipeIngredients: MutableList<RecipeIngredient?> = mutableListOf()

        ingredients.forEach {
            recipeIngredients.add(rezeptZutatDao.getByID(it.z_id, recipe.r_id))
        }

        return recipeIngredients
    }

    fun updateRecipe(newRecipe: Recipe) {
        // TODO: Update recipe

        rezeptDao.updateRecipe(newRecipe)
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