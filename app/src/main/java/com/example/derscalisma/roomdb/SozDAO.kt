package com.example.derscalisma.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.derscalisma.model.Soz
@Dao
interface SozDAO {

    @Query("SELECT * FROM Soz ")
    fun getAll():List<Soz>

    @Query("SELECT * FROM Soz WHERE id = :id ")
    fun findById(id:Int):Soz

    @Query("DELETE FROM Soz")
    fun deleteAll()


    @Query("DELETE FROM Soz WHERE id = :id")
    fun deleteById(id: Int)

    @Insert
    fun insert(soz: Soz)

    @Delete
    fun delete(soz:Soz)



}