package com.example.rancho.dao

import androidx.room.*
import com.example.rancho.model.Product


@Dao
interface ProductDao {

    @Insert
    suspend fun addProduct(prod:Product)

    @Query("SELECT * FROM product WHERE id_shopping = :thisId ORDER BY id DESC")
    suspend fun getAllProducts(thisId:String):List<Product>

    @Update
    suspend fun updateProduct(prod: Product)

    @Delete
    suspend fun deleteProduct(prod:Product)

    @Query("DELETE FROM product WHERE id_shopping = :thisId")
    suspend fun deleteAllProducts(thisId: String)

}