package com.example.data.repository

import com.example.data.dao.MarketDao
import com.example.data.model.CartItem
import com.example.data.model.FurnitureItem
import com.example.data.model.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

class MarketRepository(private val marketDao: MarketDao) {

    val allFurniture: Flow<List<FurnitureItem>> = marketDao.getAllFurnitureItems()
    val allOrders: Flow<List<Order>> = marketDao.getAllOrders()
    val cartItems: Flow<List<CartItem>> = marketDao.getCartItems()

    fun getFurnitureByCategory(category: String): Flow<List<FurnitureItem>> {
        return marketDao.getFurnitureItemsByCategory(category)
    }

    suspend fun getFurnitureItemById(id: Int): FurnitureItem? {
        return marketDao.getFurnitureItemById(id)
    }

    suspend fun addFurnitureItem(item: FurnitureItem) = withContext(Dispatchers.IO) {
        marketDao.insertFurnitureItem(item)
    }

    suspend fun removeFurnitureItem(item: FurnitureItem) = withContext(Dispatchers.IO) {
        marketDao.deleteFurnitureItem(item)
    }

    suspend fun addToCart(furnitureId: Int) = withContext(Dispatchers.IO) {
        val existing = marketDao.getCartItemByFurnitureId(furnitureId)
        if (existing != null) {
            marketDao.updateCartItemQuantity(existing.id, existing.quantity + 1)
        } else {
            marketDao.insertCartItem(CartItem(furnitureId = furnitureId, quantity = 1))
        }
    }

    suspend fun updateCartQuantity(cartItemId: Int, quantity: Int) = withContext(Dispatchers.IO) {
        if (quantity <= 0) {
            marketDao.deleteCartItem(cartItemId)
        } else {
            marketDao.updateCartItemQuantity(cartItemId, quantity)
        }
    }

    suspend fun removeFromCart(cartItemId: Int) = withContext(Dispatchers.IO) {
        marketDao.deleteCartItem(cartItemId)
    }

    suspend fun removeFromCartByFurniture(furnitureId: Int) = withContext(Dispatchers.IO) {
        marketDao.deleteCartItemByFurnitureId(furnitureId)
    }

    suspend fun placeOrder(order: Order) = withContext(Dispatchers.IO) {
        marketDao.insertOrder(order)
        marketDao.clearCart() // Clear cart after successful checkout as per secure payment protocol!
    }

    suspend fun preseedIfNecessary() = withContext(Dispatchers.IO) {
        // We look up all existing furniture. If none exists, we add our standard preseeded premium designer items!
        val items = marketDao.getAllFurnitureItems().firstOrNull() ?: emptyList()
        if (items.isEmpty()) {
            val preseeded = listOf(
                FurnitureItem(
                    title = "Mid-Century Walnut Lounge Chair",
                    description = "Classic lounge chair with top-grain amber leather cushions and an elegant, sculptural American Walnut frame. Perfect comfort posture for modern workspaces.",
                    price = 650.00,
                    category = "Living Room",
                    condition = "Like New",
                    sellerName = "Aiden Studio",
                    sellerContact = "aiden@retrodesign.com",
                    stylePattern = 2,
                    colorHex = "#7B4F37"
                ),
                FurnitureItem(
                    title = "Nordic Oak Dining Bench",
                    description = "Solid white oak dining bench with clean tapered legs and matte protective natural finish. Hand-crafted timber joints built to endure generations.",
                    price = 240.00,
                    category = "Dining",
                    condition = "New",
                    sellerName = "Soren & Co.",
                    sellerContact = "soren@oakdesign.com",
                    stylePattern = 1,
                    colorHex = "#E5C49F"
                ),
                FurnitureItem(
                    title = "Japanese Slate Coffee Table",
                    description = "Minimalist basalt slate stone slab mounted gracefully on a dark matte cylindrical steel foundation. Harmonizes raw organic materials with sharp industrial design.",
                    price = 320.00,
                    category = "Living Room",
                    condition = "Good",
                    sellerName = "ZenSpace Loft",
                    sellerContact = "listings@zenspace.org",
                    stylePattern = 3,
                    colorHex = "#454545"
                ),
                FurnitureItem(
                    title = "Emerald Velvet Queen Bed Frame",
                    description = "Gently curved headboard fully upholstered in luxurious, wear-resistant deep emerald green velvet. Hand-crafted wooden support slats ensure zero squeaking.",
                    price = 890.00,
                    category = "Bedroom",
                    condition = "New",
                    sellerName = "Velvet Sleep Boutique",
                    sellerContact = "sales@velvetsleep.com",
                    stylePattern = 4,
                    colorHex = "#1D4B39"
                ),
                FurnitureItem(
                    title = "Bauhaus Steel Cantilever Desk Chair",
                    description = "A classic Mies-inspired 1920 design reissue. Chrome-plated hand-polished tubular steel and stretched full-sling top grain resilient harness leather.",
                    price = 190.00,
                    category = "Office",
                    condition = "Good",
                    sellerName = "Modernist Office Supply",
                    sellerContact = "desk@bauhausreprints.com",
                    stylePattern = 5,
                    colorHex = "#1C1C1C"
                ),
                FurnitureItem(
                    title = "Cedar Slat Outdoor Sofa Set",
                    description = "Bold, weather-treated Solid Western Red Cedar sofa, outfitted in plush off-white waterproof Sunbrella acrylic outdoor fabrics. Perfect comfort for garden settings.",
                    price = 1150.00,
                    category = "Outdoor",
                    condition = "Like New",
                    sellerName = "Veranda Living",
                    sellerContact = "hello@verandaliving.co",
                    stylePattern = 6,
                    colorHex = "#C57B57"
                )
            )
            for (item in preseeded) {
                marketDao.insertFurnitureItem(item)
            }
        }
    }
}
