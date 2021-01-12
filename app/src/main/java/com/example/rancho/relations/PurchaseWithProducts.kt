package com.example.rancho.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.rancho.model.Product
import com.example.rancho.model.Purchase

class PurchaseWithProducts (
    @Embedded val purchase: Purchase,
    @Relation(
        parentColumn = "purchaseName",
        entityColumn = "purchaseName"
    )
    val products: List<Product>
)