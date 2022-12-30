package com.example.rezeptliste2.database.controller

import android.content.Context
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

    fun getRecipeIngredients(recipe: Recipe, ingredients: List<Ingredient>): List<RecipeIngredient?> {

        val recipeIngredients: MutableList<RecipeIngredient?> = mutableListOf()

        ingredients.forEach {
            recipeIngredients.add(rezeptZutatDao.getByID(it.z_id, recipe.r_id))
        }

        return recipeIngredients
    }

    fun getRecipeIngredientAmounts(recipe: Recipe, ingredients: List<Ingredient>): List<String> {

        val amounts: MutableList<String> = mutableListOf()

        for (ingredient in ingredients) {
            amounts += rezeptZutatDao.getRecipeIngredientAmount(recipe.r_id, ingredient.z_id) ?: "not defined"
        }

        return amounts
    }

    fun updateRecipeIngredients(recipe: Recipe, recipeIngredientsAmount: MapUtil) {

        // TODO: Zutaten aus der Datenbank entfernen, die nicht mehr in der Liste sind (RezeptZutat)
        // TODO: Zutaten in die Datenbank hinzufügen, die in der Liste sind, aber noch nicht in der Datenbank (Zutat)
        // TODO: Zutaten die in der liste sind, aber noch nicht in der Datenbank, müssen auch in RezeptZutat eingefügt werden (RezeptZutat)
        // TODO: Menge der Zutaten die vorhanden sind, updaten (RezeptZutat)

        rezeptDao.update(recipe)





        // Das sollte am Ende sein, wenn alles sicher in der Datenbank ist.
        for (recipeIngredient in recipeIngredientsAmount) {
            rezeptZutatDao.updateAmount(recipeID = recipe.r_id, ingredientID = recipeIngredient.key.z_id, amount = recipeIngredient.value)
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