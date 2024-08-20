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

}