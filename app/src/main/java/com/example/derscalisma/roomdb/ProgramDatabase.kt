package com.example.derscalisma.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.derscalisma.model.Program

@Database(entities =[Program::class], version = 1 )
abstract class ProgramDatabase: RoomDatabase(){
    abstract fun programDao():ProgramDAO
}