package com.example.derscalisma.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Soz(
    @ColumnInfo(name= "cumle")
    val cumle:String?
)
{
    @PrimaryKey(autoGenerate = true)
    var id=0
}