package com.example.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.database.MarketDatabase
import com.example.data.model.CartItem
import com.example.data.model.FurnitureItem
import com.example.data.model.Order
import com.example.data.repository.MarketRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ModelCartItem(
    val cartId: Int,
    val item: FurnitureItem,
    val quantity: Int
)

sealed interface PaymentUiState {
    object Idle : PaymentUiState
    object Validating : PaymentUiState
    object Processing : PaymentUiState
    data class Success(val order: Order) : PaymentUiState
    data class Error(val message: String) : PaymentUiState
}

class MarketViewModel(private val repository: MarketRepository) : ViewModel() {

    // Pre-populate items and initialize
    init {
        viewModelScope.launch {
            repository.preseedIfNecessary()
        }
    }

    // All available furniture items
    val allFurniture: StateFlow<List<FurnitureItem>> = repository.allFurniture
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // All historic orders
    val allOrders: StateFlow<List<Order>> = repository.allOrders
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Current cart items from database
    private val rawCartItems: Flow<List<CartItem>> = repository.cartItems

    // Combined UI cart items containing the resolved Furniture details
    val cartUiItems: StateFlow<List<ModelCartItem>> = combine(rawCartItems, allFurniture) { cart, furniture ->
        cart.mapNotNull { cartItem ->
            val match = furniture.find { it.id == cartItem.furnitureId }
            match?.let {
                ModelCartItem(
                    cartId = cartItem.id,
                    item = it,
                    quantity = cartItem.quantity
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Total Cart sum
    val cartTotal: StateFlow<Double> = cartUiItems.map { list ->
        list.sumOf { it.item.price * it.quantity }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    // Secure Payment Processing UI state
    private val _paymentState = MutableStateFlow<PaymentUiState>(PaymentUiState.Idle)
    val paymentState: StateFlow<PaymentUiState> = _paymentState.asStateFlow()

    // Screen selection (Alternative inline routing inside application or type-safe keys)
    private val _currentTab = MutableStateFlow(0) // 0 = Browse, 1 = Sell, 2 = Cart, 3 = History
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    fun selectTab(index: Int) {
        _currentTab.value = index
    }

    // Selected furniture item for details modal/screen
    private val _selectedItem = MutableStateFlow<FurnitureItem?>(null)
    val selectedItem: StateFlow<FurnitureItem?> = _selectedItem.asStateFlow()

    fun showItemDetails(item: FurnitureItem?) {
        _selectedItem.value = item
    }

    // Seller Listing inputs & state
    private val _listingStatus = MutableStateFlow<String?>(null)
    val listingStatus: StateFlow<String?> = _listingStatus.asStateFlow()

    fun clearListingStatus() {
        _listingStatus.value = null
    }

    // Add list item
    fun publishItem(
        title: String,
        description: String,
        price: Double,
        category: String,
        condition: String,
        sellerName: String,
        sellerContact: String,
        colorHex: String,
        stylePattern: Int
    ) {
        viewModelScope.launch {
            if (title.isBlank() || description.isBlank() || sellerName.isBlank() || sellerContact.isBlank()) {
                _listingStatus.value = "Error: Please fill in all required fields."
                return@launch
            }
            if (price <= 0.0) {
                _listingStatus.value = "Error: Price must be a positive value."
                return@launch
            }

            val item = FurnitureItem(
                title = title,
                description = description,
                price = price,
                category = category,
                condition = condition,
                sellerName = sellerName,
                sellerContact = sellerContact,
                colorHex = colorHex,
                stylePattern = stylePattern,
                isListedByMe = true
            )
            repository.addFurnitureItem(item)
            _listingStatus.value = "Success"
        }
    }

    fun deleteItem(item: FurnitureItem) {
        viewModelScope.launch {
            // Delete from cart first if it was there
            repository.removeFromCartByFurniture(item.id)
            repository.removeFurnitureItem(item)
        }
    }

    // Add to cart action
    fun addToCart(item: FurnitureItem) {
        viewModelScope.launch {
            repository.addToCart(item.id)
        }
    }

    // Update quantity
    fun updateCartQty(cartId: Int, qty: Int) {
        viewModelScope.launch {
            repository.updateCartQuantity(cartId, qty)
        }
    }

    // Remove item outright
    fun removeFromCart(cartId: Int) {
        viewModelScope.launch {
            repository.removeFromCart(cartId)
        }
    }

    // Reset payment state
    fun resetPayment() {
        _paymentState.value = PaymentUiState.Idle
    }

    // Simulated Secure Payment Gateway Processing
    fun processSecurePayment(
        cardName: String,
        cardNumber: String,
        cardCvv: String,
        cardExpiry: String,
        buyerAddress: String,
        buyerContact: String
    ) {
        viewModelScope.launch {
            // Basic secure-gateway client side pre-validation rules
            if (cardName.isBlank()) {
                _paymentState.value = PaymentUiState.Error("Cardholder name is required.")
                return@launch
            }
            val cleanCardNum = cardNumber.replace(" ", "")
            if (cleanCardNum.length < 15 || cleanCardNum.length > 16 || cleanCardNum.any { !it.isDigit() }) {
                _paymentState.value = PaymentUiState.Error("Invalid card number. Must be 15-16 digits.")
                return@launch
            }
            if (cardCvv.length < 3 || cardCvv.length > 4 || cardCvv.any { !it.isDigit() }) {
                _paymentState.value = PaymentUiState.Error("Invalid CVV code. Must be 3 or 4 digits.")
                return@launch
            }
            val expiryRegex = Regex("^(0[1-9]|1[0-2])/([2-9][0-9])\$")
            if (!expiryRegex.matches(cardExpiry)) {
                _paymentState.value = PaymentUiState.Error("Invalid Expiry Format. Use MM/YY (e.g., 08/29).")
                return@launch
            }
            if (buyerAddress.isBlank()) {
                _paymentState.value = PaymentUiState.Error("Delivery shipping address is required.")
                return@launch
            }
            if (buyerContact.isBlank()) {
                _paymentState.value = PaymentUiState.Error("Contact mobile number is required.")
                return@launch
            }

            val currentCart = cartUiItems.value
            if (currentCart.isEmpty()) {
                _paymentState.value = PaymentUiState.Error("Shopping cart is empty.")
                return@launch
            }

            // Transit to processing state - shows secure gateway load sequence to user
            _paymentState.value = PaymentUiState.Processing

            // 2.5 seconds block to simulate merchant validation, card routing, and checkout confirmation
            kotlinx.coroutines.delay(2500)

            val summary = currentCart.joinToString(", ") { "${it.item.title} (x${it.quantity})" }
            val amount = cartTotal.value

            val last4 = if (cleanCardNum.length >= 4) cleanCardNum.takeLast(4) else "4242"

            val order = Order(
                itemsSummary = summary,
                totalPrice = amount,
                cardLast4 = last4,
                buyerName = cardName,
                buyerAddress = "$buyerAddress, Phone: $buyerContact"
            )

            // Persist order details, and clear custom cart
            repository.placeOrder(order)

            _paymentState.value = PaymentUiState.Success(order)
        }
    }

    // Factory Provider
    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val database = MarketDatabase.getDatabase(context.applicationContext)
            val repository = MarketRepository(database.marketDao())
            @Suppress("UNCHECKED_CAST")
            return MarketViewModel(repository) as T
        }
    }
}
