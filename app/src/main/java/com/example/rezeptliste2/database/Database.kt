package com.example.rezeptliste2.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.rezeptliste2.database.dao.RezeptDao
import com.example.rezeptliste2.database.dao.RezeptZutatDao
import com.example.rezeptliste2.database.dao.ZutatDao
import com.example.rezeptliste2.database.dto.Rezept
import com.example.rezeptliste2.database.dto.RezeptZutat
import com.example.rezeptliste2.database.dto.Zutat


@Database(entities = [Rezept::class, Zutat::class, RezeptZutat::class], version = 4)
abstract class Database : RoomDatabase() {
    abstract fun rezeptDao(): RezeptDao
    abstract fun zutatDao(): ZutatDao
    abstract fun rezeptZutatDao(): RezeptZutatDao
}


