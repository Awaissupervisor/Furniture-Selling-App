package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val itemsSummary: String, // Stringified list of items
    val totalPrice: Double,
    val cardLast4: String,
    val buyerName: String,
    val buyerAddress: String,
    val timestamp: Long = System.currentTimeMillis(),
    val paymentStatus: String = "Success" // "Success" or other dummy status
)
