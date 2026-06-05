package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.MarketDao
import com.example.data.model.CartItem
import com.example.data.model.FurnitureItem
import com.example.data.model.Order

@Database(entities = [FurnitureItem::class, CartItem::class, Order::class], version = 1, exportSchema = false)
abstract class MarketDatabase : RoomDatabase() {
    abstract fun marketDao(): MarketDao

    companion object {
        @Volatile
        private var INSTANCE: MarketDatabase? = null

        fun getDatabase(context: Context): MarketDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MarketDatabase::class.java,
                    "furniture_marketplace_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
