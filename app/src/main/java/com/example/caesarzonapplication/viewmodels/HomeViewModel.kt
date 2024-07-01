package com.example.caesarzonapplication.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caesarzonapplication.model.dto.AdminNotificationDTO
import com.example.caesarzonapplication.model.dto.ProductDTO
import com.example.caesarzonapplication.model.dto.ProductSearchDTO
import com.example.caesarzonapplication.model.repository.NotifyRepository
import com.example.caesarzonapplication.model.repository.ProductRepository
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.net.URL
import java.util.UUID

class HomeViewModel(private val repository: ProductRepository, private val notifyRepository: NotifyRepository) : ViewModel(){


    val client = OkHttpClient()
    val gson = Gson()

    val newProducts: StateFlow<List<ProductSearchDTO>> get() = repository.newProducts
    val hotProducts: StateFlow<List<ProductSearchDTO>> get() = repository.hotProducts
    val notification: StateFlow<List<AdminNotificationDTO>> get() = notifyRepository.notificationAdmin




    fun loadProducts() {
        viewModelScope.launch {
            repository.loadNewProducts()
            repository.loadHotProducts()
        }
    }

    init{
        loadNotification()
    }

    fun loadNotification(){
       viewModelScope.launch {
           notifyRepository.loadNotifcationAdmin()
       }
    }

}

