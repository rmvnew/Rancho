package com.example.rancho.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.rancho.model.Product
import com.example.rancho.model.Shopping


@Database(
    entities = [
        Shopping::class,
        Product::class
    ], version = 1
)
abstract class ProductDatabase : RoomDatabase() {

    abstract fun getShoppingDao():ShoppingDao
    abstract fun getProductDao():ProductDao

    companion object {
        @Volatile
        private var instance: ProductDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            ProductDatabase::class.java,
            "purchaseDB.db"
        ).build()

    }

}