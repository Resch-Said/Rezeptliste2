package com.example.rezeptliste2.database.controller

import android.content.Context
import androidx.room.Room
import com.example.rezeptliste2.database.Database
import com.example.rezeptliste2.database.dao.RecipeDao
import com.example.rezeptliste2.database.dao.RezeptZutatDao
import com.example.rezeptliste2.database.dao.ZutatDao
import com.example.rezeptliste2.database.dto.RecipeIngredient

class RecipeIngredientController(context: Context) {
    fun getByID(ingredientID: Int, recipeID: Int): RecipeIngredient {
        return rezeptZutatDao.getByID(ingredientID, recipeID)
    }

    fun delete(recipeIngredient: RecipeIngredient?) {
        if (recipeIngredient != null) {
            rezeptZutatDao.delete(recipeIngredient)
        }
    }

    fun insert(ingredientID: Int, recipeID: Int, amount: String) {
        rezeptZutatDao.insert(ingredientID, recipeID, amount)
    }

    fun update(recipeIngredient: RecipeIngredient) {
        rezeptZutatDao.update(
            recipeIngredient.z_id, recipeIngredient.r_id, recipeIngredient.menge?: "not defined"
        )
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