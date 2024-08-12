package com.example.caesarzonapplication.model.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.caesarzonapplication.model.entities.Product
import com.example.caesarzonapplication.model.dto.ProductDTO
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.basicToken
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.net.URL
import java.util.UUID

class ProductsViewModel: ViewModel() {


    val client = OkHttpClient()
    val gson = Gson()

    private val _productsInShoppingCart = mutableStateListOf<Product>()
    val productInShoppingCart: List<Product> get() = _productsInShoppingCart

    val selectedProduct = mutableStateOf<ProductDTO?>(null)


    /*
    // Metodo per aggiungere prodotti al carrello
    fun addProductToCart(product: Product) {
        _productsInShoppingCart.add(product)
    }
     */


    fun getProduct(productID: UUID){
        CoroutineScope(Dispatchers.IO).launch {
            val manageURL = URL("http://25.49.50.144:8090/product-api/product/"+productID)
            val request = Request
                .Builder()
                .url(manageURL)
                .addHeader("Authorization", "Bearer ${basicToken?.accessToken}")
                .build()
            try{
                println("Sono nella funzione per prendere il prodotto")
                val response = client.newCall(request).execute()
                println("sto prendendo il prodotto"+response.message+" code: "+response.code)
                if (!response.isSuccessful) {
                    println("Problemi nel caricamento del prodotto"+response.message+" code: "+response.code)
                    return@launch
                }else{
                    val responseBody = response.body?.string()

                    val productDTO = gson.fromJson(responseBody, ProductDTO::class.java)
                    println("prodotto preso response: "+response.message+" "+response.code)
                    selectedProduct.value = productDTO
                    println("Prodotto : "+productDTO.name)
                }

            }catch (e: IOException){
                e.printStackTrace()
            }
        }

    }

}