package com.example.derscalisma.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.derscalisma.model.Program
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface ProgramDAO {
    @Query("SELECT * FROM Program")
    fun getAll(): Flowable<List<Program>>

    @Query("SELECT * FROM Program WHERE id = :id")
    fun findById(id :Int):Flowable<Program>

    @Insert
    fun insert(program: Program):Completable

    @Delete
    fun delete(program: Program):Completable




}