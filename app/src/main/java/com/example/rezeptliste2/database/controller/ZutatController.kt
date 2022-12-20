package com.example.rezeptliste2.database.controller

import android.content.Context
import androidx.room.Room
import com.example.rezeptliste2.database.Database
import com.example.rezeptliste2.database.dao.RezeptDao
import com.example.rezeptliste2.database.dao.RezeptZutatDao
import com.example.rezeptliste2.database.dao.ZutatDao
import com.example.rezeptliste2.database.dto.Zutat

class ZutatController(context: Context) {
    fun getAllAvailable(): List<Zutat> {
        return zutatDao.getAllAvailable(true)
    }

    fun setAvailable(newIngredient: String, available: Boolean) {

        val zutat = zutatDao.getByName(newIngredient)
        zutatDao.setAvailable(zutat.z_id, available)

        if (available) {
            zutatDao.setOrderID(zutat.z_id, zutatDao.getLastOrderID() + 1)
        }
    }


    private var zutatDao: ZutatDao
    private var rezeptZutatDao: RezeptZutatDao
    private var rezeptDao: RezeptDao
    private var db: Database

    init {
        this.db = Room.databaseBuilder(context, Database::class.java, "Rezeptliste.db")
            .createFromAsset("Database/Rezeptliste.db").allowMainThreadQueries().build()
        rezeptDao = db.rezeptDao()
        zutatDao = db.zutatDao()
        rezeptZutatDao = db.rezeptZutatDao()
    }
}