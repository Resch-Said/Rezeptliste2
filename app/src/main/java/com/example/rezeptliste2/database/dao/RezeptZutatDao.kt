package com.example.rezeptliste2.database.dao

import androidx.room.*
import com.example.rezeptliste2.database.dto.RecipeIngredient

@Dao
interface RezeptZutatDao {

    @Query("SELECT * FROM RezeptZutat")
    fun getAll(): List<RecipeIngredient>

    @Query("SELECT * FROM RezeptZutat WHERE z_id LIKE :zutatID AND r_id LIKE :rezeptID LIMIT 1")
    fun getByID(zutatID: Int, rezeptID: Int): RecipeIngredient

    @Query("UPDATE RezeptZutat SET menge = :menge WHERE z_id LIKE :zutatId AND r_id LIKE :rezeptId")
    fun updateMenge(zutatId: Int, rezeptId: Int, menge: String)

    @Query("SELECT * FROM RezeptZutat WHERE z_id NOT IN (SELECT z_id FROM Zutat) OR r_id NOT IN (SELECT r_id FROM Rezept)")
    fun getAllNotInZutatOrRezept(): List<RecipeIngredient>

    @Update
    fun updateZutat(rezeptZutat: RecipeIngredient)

    @Insert
    fun insert(vararg rezeptZutat: RecipeIngredient)

    @Delete
    fun delete(rezeptZutat: RecipeIngredient)

}