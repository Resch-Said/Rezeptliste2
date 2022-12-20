package com.example.rezeptliste2.database.dao

import androidx.room.*
import com.example.rezeptliste2.database.dto.Zutat

@Dao
interface ZutatDao {
    @Query("SELECT * FROM zutat")
    fun getAll(): List<Zutat>

    @Query("SELECT * FROM zutat WHERE z_id LIKE :zutatId LIMIT 1")
    fun getByID(zutatId: Int): Zutat

    @Query("SELECT * FROM zutat WHERE name LIKE :name LIMIT 1")
    fun getByName(name: String): Zutat

    @Query("SELECT * FROM zutat WHERE isAvailable LIKE :isAvailable")
    fun getAllAvailable(isAvailable: Boolean): List<Zutat>

    @Query("SELECT * FROM zutat WHERE name LIKE '%' || :search || '%' AND isAvailable LIKE false")
    fun getAllContaining(search: String): List<Zutat>

    @Query("UPDATE zutat SET isAvailable = 0 WHERE z_id LIKE :zutatId")
    fun setUnavailable(zutatId: Int)

    @Query("UPDATE zutat SET isAvailable = 1 WHERE z_id LIKE :zutatId")
    fun setAvailable(zutatId: Int)

    @Query("SELECT * FROM zutat WHERE z_id NOT IN (SELECT z_id FROM RezeptZutat)")
    fun getAllNotInRezeptZutat(): List<Zutat>


    @Query("SELECT * FROM zutat WHERE z_id IN (SELECT z_id FROM RezeptZutat WHERE r_id LIKE :rezeptId)")
    fun getAllByRezept(rezeptId: Int): List<Zutat>


    @Update
    fun updateZutat(zutat: Zutat)

    @Insert
    fun insert(vararg zutaten: Zutat)

    @Delete
    fun delete(zutat: Zutat)
}