package com.example.caesarzonapplication.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.caesarzonapplication.model.Product

class ShoppingCartViewModel: ViewModel() {
    private val _productsInShoppingCart = mutableStateListOf<Product>()

    val productInShoppingCart: List<Product> get() = _productsInShoppingCart

    init{

    }

    fun loadShoppingCartProduct(){

    }
}