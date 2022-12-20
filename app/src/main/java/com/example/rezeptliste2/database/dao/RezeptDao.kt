package com.example.rezeptliste2.database.dao

import androidx.room.*
import com.example.rezeptliste2.database.dto.Rezept

@Dao
interface RezeptDao {
    @Query("SELECT * FROM rezept")
    fun getAll(): List<Rezept>

    @Query("SELECT * FROM rezept WHERE r_id LIKE :rezeptId LIMIT 1")
    fun getByID(rezeptId: Int): Rezept

    @Query("SELECT * FROM rezept WHERE name LIKE :name LIMIT 1")
    fun getByName(name: String): Rezept


    @Update
    fun updateRezept(rezept: Rezept)

    @Insert
    fun insert(vararg rezept: Rezept)

    @Delete
    fun delete(rezept: Rezept)
}