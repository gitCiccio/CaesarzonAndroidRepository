package com.example.caesarzonapplication.model.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caesarzonapplication.R
import com.example.caesarzonapplication.model.dto.ProductSearchDTO
import com.example.caesarzonapplication.model.entities.shoppingCartEntities.Product
import kotlinx.coroutines.launch

class ShoppingCartViewModel: ViewModel() {
    private val _productsInShoppingCart = mutableStateListOf<Product>()

    private var _buyLaterProducts = mutableStateListOf<Product>()

    val productInShoppingCart = mutableStateListOf<Product>()

    val buyLaterProducts = mutableStateListOf<ProductSearchDTO>()

    init{
        loadShoppingCartProduct()
    }

    private fun loadShoppingCartProduct(){
       //Carica i prodotti sul carrello
    }

    fun addLaterProduct(product: Product){
        println("aggiunto")
        _buyLaterProducts.add(product)
    }

    fun getBuyLaterProducts(): List<Product>{
        return _buyLaterProducts
    }


    /*
    GEMINI esempio
    fun loadProductsFromServer() {
    viewModelScope.launch {
        try {
            val products = repository.getProductsFromApi()
            _products.value = products
        } catch (e: Exception) {
            _errorMessage.value = e.message
        }
    }
}*/
    /*
    fun saveProductToDatabase(product: Product) {
    viewModelScope.launch {
        repository.saveProductToDatabase(product)
    }
}

     */

    fun addProduct(product: Product){
        if(_productsInShoppingCart.contains(product)){
            val existingProduct = _productsInShoppingCart.find { it == product }
            existingProduct?.copy(quantity = product.quantity+1)
        }else{
            _productsInShoppingCart.add(product.copy(quantity = 1))
        }

    }

    fun getProduct(name: String): Product?{
        return _productsInShoppingCart.find { it.name == name }
    }

    /*fun getImage(name: String): Int {
        return _productsInShoppingCart.find { it.name == name }?.imageRes ?: 0
    }*/


    fun increaseProduct(product: Product) {
        val existingProduct = _productsInShoppingCart.find { it.name == product.name }
        existingProduct?.let {
            if (it.quantity < 10) {
                val updatedProduct = it.copy(quantity = it.quantity + 1)
                val index = _productsInShoppingCart.indexOf(it)
                _productsInShoppingCart[index] = updatedProduct // Update quantity at the same index
            }
        }
        println("nome " + existingProduct?.name + " quantità: " + existingProduct?.quantity)
    }

    fun decreaseProduct(product: Product) {
        val existingProduct = _productsInShoppingCart.find { it.name == product.name }
        existingProduct?.let {
            if (it.quantity-1 > 0) {
                val updatedProduct = it.copy(quantity = it.quantity - 1)
                val index = _productsInShoppingCart.indexOf(it)
                _productsInShoppingCart[index] = updatedProduct // Update quantity at the same index
            }
        }
    }


    fun deleteProduct(name: String){
        println("Prodotto eliminato: $name")
        _productsInShoppingCart.removeAll{ it.name == name }
    }

}