package com.example.rancho.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class Product(

    val id_shopping:Int,
    val productName:String,
    val productQuantity: Double,
    val productValue:Double,
    val productDone:Boolean,
    val typeOfMeasure: String

):Serializable{
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}