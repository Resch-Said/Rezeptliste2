package com.example.rezeptliste2.database.controller

import android.content.Context
import androidx.room.Room
import com.example.rezeptliste2.database.Database
import com.example.rezeptliste2.database.dao.RecipeDao
import com.example.rezeptliste2.database.dao.RezeptZutatDao
import com.example.rezeptliste2.database.dao.ZutatDao
import com.example.rezeptliste2.database.dto.Ingredient

class IngredientController(context: Context) {
    fun getAllAvailable(isAvailable: Boolean): List<Ingredient> {
        return zutatDao.getAllAvailable(isAvailable)
    }

    fun setAvailable(ingredientName: String, available: Boolean) {

        val ingredient = zutatDao.getByName(ingredientName)

        if (ingredient != null) {
            zutatDao.setAvailable(ingredient.z_id, available)

            if (available) {
                zutatDao.setOrderID(ingredient.z_id, zutatDao.getLastOrderID() + 1)
            }
        }
    }

    fun getByName(ingredientName: String): Ingredient? {
        return zutatDao.getByName(ingredientName)
    }

    fun getByID(ingredientID: Int): Ingredient? {
        return zutatDao.getByID(ingredientID)
    }

    fun getLastID(): Int {
        return zutatDao.getLastID()
    }

    fun getLastOrderID(): Int {
        return zutatDao.getLastOrderID()
    }

    fun getAllIngredients(): List<Ingredient> {
        return zutatDao.getAll()
    }

    fun insert(ingredient: Ingredient) {
        zutatDao.insert(ingredient.name, ingredient.isAvailable, ingredient.orderID)
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