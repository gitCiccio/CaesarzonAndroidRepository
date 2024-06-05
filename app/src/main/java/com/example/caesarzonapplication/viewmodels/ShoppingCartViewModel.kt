package com.example.caesarzonapplication.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.traceEventEnd
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.caesarzonapplication.R
import com.example.caesarzonapplication.model.Product
import kotlinx.coroutines.launch

class ShoppingCartViewModel: ViewModel() {
    private val _productsInShoppingCart = mutableStateListOf<Product>()

    val productInShoppingCart: List<Product> get() = _productsInShoppingCart

    init{
        loadShoppingCartProduct()
    }

    private fun loadShoppingCartProduct(){
        viewModelScope.launch {
            _productsInShoppingCart.addAll(
                listOf(
                    Product("Calzettoni da calcio puzzolenti", R.drawable.ic_calcio, 100.00, "Calzettoni da calcio fetidi", true),
                    Product("Paradenti masticato", R.drawable.ic_lotta, 20.00, "Un paradenti consumato", true),
                    Product("Racchette rotte", R.drawable.ic_tennis, 10.00, "C'è il manico ma non la racchetta", true),
                    Product("Tanga anaerobico", R.drawable.ic_atletica, 40.00, "Intimo utile per rinfrscare il sotto palla", true),
                    Product("Capocuffia", R.drawable.ic_nuoto, 30.00, "Una cuffia da nuoto con la stampa di una capocchia", true),
                    Product("Pallone bucato", R.drawable.ic_pallavolo, 104.00, "Palla bucata", true)
                )
            )
        }
    }

    fun addProduct(product: Product){
        /*TODO*/
    }

    fun deleteProduct(name: String){
        /*TODO*/
    }
}