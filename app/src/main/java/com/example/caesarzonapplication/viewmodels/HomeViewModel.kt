package com.example.caesarzonapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caesarzonapplication.model.dto.AdminNotificationDTO
import com.example.caesarzonapplication.model.dto.ProductSearchDTO
import com.example.caesarzonapplication.model.repository.NotifyRepository
import com.example.caesarzonapplication.model.repository.ProductRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: ProductRepository, private val notifyRepository: NotifyRepository) : ViewModel(){

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

