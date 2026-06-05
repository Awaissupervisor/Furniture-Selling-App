package com.example.data.dao

import androidx.room.*
import com.example.data.model.CartItem
import com.example.data.model.FurnitureItem
import com.example.data.model.Order
import kotlinx.coroutines.flow.Flow

@Dao
interface MarketDao {

    // --- Furniture Items ---
    @Query("SELECT * FROM furniture_items ORDER BY timestamp DESC")
    fun getAllFurnitureItems(): Flow<List<FurnitureItem>>

    @Query("SELECT * FROM furniture_items WHERE category = :category ORDER BY timestamp DESC")
    fun getFurnitureItemsByCategory(category: String): Flow<List<FurnitureItem>>

    @Query("SELECT * FROM furniture_items WHERE id = :id")
    suspend fun getFurnitureItemById(id: Int): FurnitureItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFurnitureItem(item: FurnitureItem)

    @Delete
    suspend fun deleteFurnitureItem(item: FurnitureItem)

    // --- Cart Items ---
    @Query("SELECT * FROM cart_items")
    fun getCartItems(): Flow<List<CartItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartItem)

    @Query("UPDATE cart_items SET quantity = :quantity WHERE id = :id")
    suspend fun updateCartItemQuantity(id: Int, quantity: Int)

    @Query("DELETE FROM cart_items WHERE id = :id")
    suspend fun deleteCartItem(id: Int)

    @Query("DELETE FROM cart_items WHERE furnitureId = :furnitureId")
    suspend fun deleteCartItemByFurnitureId(furnitureId: Int)

    @Query("SELECT * FROM cart_items WHERE furnitureId = :furnitureId LIMIT 1")
    suspend fun getCartItemByFurnitureId(furnitureId: Int): CartItem?

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    // --- Orders ---
    @Query("SELECT * FROM orders ORDER BY timestamp DESC")
    fun getAllOrders(): Flow<List<Order>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)
}
