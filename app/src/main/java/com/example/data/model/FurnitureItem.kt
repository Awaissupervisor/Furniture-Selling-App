package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "furniture_items")
data class FurnitureItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val price: Double,
    val category: String, // "Living Room", "Bedroom", "Dining", "Office", "Outdoor"
    val condition: String, // "New", "Like New", "Good", "Fair"
    val sellerName: String,
    val sellerContact: String,
    val stylePattern: Int = 1, // Determines which Compose vector asset/design to render
    val colorHex: String = "#8B5A2B", // Main visual tint
    val isListedByMe: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
