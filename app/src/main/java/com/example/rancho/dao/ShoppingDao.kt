package com.example.rancho.dao


import androidx.room.*

import com.example.rancho.model.Shopping

@Dao
interface ShoppingDao {

    @Insert
    suspend fun addShopping(shop:Shopping)

    @Query("SELECT * FROM shopping ORDER BY id DESC")
    suspend fun getAllShoppings():List<Shopping>

    @Query("SELECT * FROM shopping WHERE dateShopping = :thisDate")
    suspend fun getAllShoppingsThisDate(thisDate:String):List<Shopping>

    @Update
    suspend fun updateShopping(shop: Shopping)

    @Delete
    suspend fun deleteShopping(shop:Shopping)

}