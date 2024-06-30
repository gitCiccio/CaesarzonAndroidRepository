package com.example.caesarzonapplication.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.caesarzonapplication.model.dto.AvailabilityDTO
import com.example.caesarzonapplication.model.dto.ProductDTO
import com.example.caesarzonapplication.model.dto.ProductSearchDTO
import com.example.caesarzonapplication.model.service.KeycloakService
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.basicToken
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.net.URL
import java.util.UUID

class HomeViewModel : ViewModel(){

    private val _products = mutableStateListOf<ProductSearchDTO>()
    val products: List<ProductSearchDTO> get() = _products

    val client = OkHttpClient()

    init{
        KeycloakService().getBasicToken()
        loadProducts()
    }


    fun loadProducts() {
        CoroutineScope(Dispatchers.IO).launch {
            val manageURL = URL("http://25.49.50.144:8090/product-api/new")
            val request = Request.Builder()
                .url(manageURL)
                .addHeader("Authorization", "Bearer ${basicToken?.accessToken}")
                .build()
            try {
                val response = client.newCall(request).execute()
                println("response dell'home viewmodel:" + response.message)
                if (!response.isSuccessful) return@launch

                response.body?.string()?.let { responseBody ->
                    val gson = Gson()
                    val productType = object : TypeToken<List<ProductSearchDTO>>() {}.type
                    val products: List<ProductSearchDTO> = gson.fromJson(responseBody, productType)
                    _products.addAll(products)
                    products.forEach { product ->
                        println("Product Name: ${product.productName}")
                        println("Product ID: ${product.productId}")
                        println("Average Review: ${product.averageReview}")
                        println("Review Number: ${product.reviewNumber}")
                        println("Price: ${product.price}")
                        println("Discount: ${product.discount}")
                    }

                }
            } catch (e: IOException) {
                return@launch
            }
        }

    }

}