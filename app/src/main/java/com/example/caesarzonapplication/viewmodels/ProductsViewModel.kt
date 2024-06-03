package com.example.caesarzonapplication.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.caesarzonapplication.R
import com.example.caesarzonapplication.model.Product

class ProductsViewModel: ViewModel() {

    private val _productsInShoppingCart = mutableStateListOf<Product>()

    val productInShoppingCart: List<Product> get() = _productsInShoppingCart

    /*
    // Metodo per aggiungere prodotti al carrello
    fun addProductToCart(product: Product) {
        _productsInShoppingCart.add(product)
    }
     */
    init {
        //loadSimpleProducts()
    }
    fun loadSimpleProducts(){
        _productsInShoppingCart.addAll(
            listOf(
                Product("Product 2", R.drawable.logo, 59.99, "Descrizione di Product 2", true),
                Product("Product 3", R.drawable.logo, 19.99, "Descrizione di Product 3", true)
            )
        )
    }
}