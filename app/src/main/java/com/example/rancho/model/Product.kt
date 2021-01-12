package com.example.rancho.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Product(
    @PrimaryKey(autoGenerate = false)
    val productName:String,
    val productQuantity: Int,
    val productValue:Double,
    val productDone:Boolean
)