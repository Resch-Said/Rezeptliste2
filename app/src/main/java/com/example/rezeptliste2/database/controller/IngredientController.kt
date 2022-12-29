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

    fun setAvailable(ingredient: String, available: Boolean) {

        val zutat = zutatDao.getByName(ingredient)

        if (zutat != null) {
            zutatDao.setAvailable(zutat.z_id, available)

            if (available) {
                zutatDao.setOrderID(zutat.z_id, zutatDao.getLastOrderID() + 1)
            }
        }
    }

    fun getByName(ingredient: String): Ingredient? {
        return zutatDao.getByName(ingredient)
    }

    fun getByID(id: Int): Ingredient? {
        return zutatDao.getByID(id)
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