package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.ui.screens.MainMarketLayout
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.MarketViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Initialize our high-grade Furniture Marketplace ViewModel
        val viewModel = ViewModelProvider(this, MarketViewModel.Factory(this))[MarketViewModel::class.java]
        
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainMarketLayout(viewModel = viewModel)
                }
            }
        }
    }
}
