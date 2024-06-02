package com.example.caesarzonapplication.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caesarzonapplication.R
import com.example.caesarzonapplication.model.Product
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel(){

    private val _products = mutableStateListOf<Product>()
    val products: List<Product> get() = _products

    init{
        loadProducts()
    }

    private fun loadProducts(){
        viewModelScope.launch {
            _products.addAll(
                listOf(
                    Product("Product 1", R.drawable.logo, 29.99, "Descrizione di Product 1", false),
                    Product("Product 2", R.drawable.logo, 59.99, "Descrizione di Product 2", true),
                    Product("Product 3", R.drawable.logo, 19.99, "Descrizione di Product 3", true),
                    Product("Product 4", R.drawable.logo, 99.99, "Descrizione di Product 4", false)
                )
            )
        }
    }

}