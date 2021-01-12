package com.example.rancho.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Purchase(
    @PrimaryKey(autoGenerate = false)
    val puchaseName:String,
    val purchaseDate:String
)