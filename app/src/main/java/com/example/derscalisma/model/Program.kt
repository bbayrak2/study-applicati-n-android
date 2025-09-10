package com.example.derscalisma.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Program (

    @ColumnInfo(name = "isim")
    var isim:String?,
    @ColumnInfo(name = "gorsel")
    var gorsel:ByteArray?

)
{
    @PrimaryKey(autoGenerate = true)
    var id=0
}