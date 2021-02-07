package com.example.rancho.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class Shopping(
    val name : String,
    val dateShopping : String,
    val timeShopping : String,
    val active:Boolean
):Serializable {
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}