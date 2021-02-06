package com.example.rancho.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.rancho.model.Product


@Dao
interface ProductDao {

    @Insert
    suspend fun addProduct(prod:Product)

    @Query("SELECT * FROM product ORDER BY id DESC")
    suspend fun getAllProducts():List<Product>

    @Update
    suspend fun updateProduct(prod: Product)

}