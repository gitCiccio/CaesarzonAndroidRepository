package com.example.caesarzonapplication.model.repository

import com.example.caesarzonapplication.model.dto.ProductSearchDTO
import com.example.caesarzonapplication.model.service.KeycloakService
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.basicToken
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.net.URL

class ProductRepository {

    private val client = OkHttpClient()

    private val _newProducts = MutableStateFlow<List<ProductSearchDTO>>(emptyList())
    val newProducts: MutableStateFlow<List<ProductSearchDTO>> get() = _newProducts

    private val _hotProducts = MutableStateFlow<List<ProductSearchDTO>>(emptyList())
    val hotProducts: MutableStateFlow<List<ProductSearchDTO>> get() = _hotProducts

    init{
        KeycloakService().getBasicToken()
    }
    suspend fun loadNewProducts() {
        // If products are already cached, use them

        withContext(Dispatchers.IO) {
            val manageURL = URL("http://25.49.50.144:8090/product-api/new")
            val request = Request.Builder()
                .url(manageURL)
                .addHeader("Authorization", "Bearer ${basicToken?.accessToken}")
                .build()
            try {
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    return@withContext
                }

                response.body?.string()?.let { responseBody ->
                    val gson = Gson()
                    val productType = object : TypeToken<List<ProductSearchDTO>>() {}.type
                    val products: List<ProductSearchDTO> = gson.fromJson(responseBody, productType)
                    // Cache the products
                    _newProducts.emit(products)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    suspend fun loadHotProducts(){

            withContext(Dispatchers.IO) {
                val manageURL = URL("http://25.49.50.144:8090/product-api/product/offer")
                val request = Request.Builder()
                    .url(manageURL)
                    .addHeader("Authorization", "Bearer ${basicToken?.accessToken}")
                    .build()
                try {
                    val response = client.newCall(request).execute()
                    if (!response.isSuccessful) {
                        return@withContext
                    }

                    response.body?.string()?.let { responseBody ->
                        val gson = Gson()
                        val productType = object : TypeToken<List<ProductSearchDTO>>() {}.type
                        val products: List<ProductSearchDTO> = gson.fromJson(responseBody, productType)
                        // Cache the products
                        _hotProducts.emit(products)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
    }
}