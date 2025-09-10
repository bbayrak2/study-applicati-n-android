package com.example.derscalisma.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.derscalisma.model.Soz

@Database(entities =[Soz::class], version = 1 )
abstract class SozDatabase: RoomDatabase(){
    abstract fun sozDao():SozDAO
}