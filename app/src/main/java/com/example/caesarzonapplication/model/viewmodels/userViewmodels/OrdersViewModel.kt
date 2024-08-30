package com.example.caesarzonapplication.model.viewmodels.userViewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.caesarzonapplication.model.dto.productDTOS.OrderDTO
import com.example.caesarzonapplication.model.dto.productDTOS.ProductCartDTO
import com.example.caesarzonapplication.model.dto.productDTOS.ProductCartWithImage
import com.example.caesarzonapplication.model.dto.productDTOS.RefundDTO
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.isAdmin
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.URL
import java.util.UUID

class OrdersViewModel: ViewModel() {

    val client = OkHttpClient()
    val gson = Gson()

    private val _orders: MutableStateFlow<List<OrderDTO>> = MutableStateFlow(emptyList())
    val orders: StateFlow<List<OrderDTO>> = _orders

    private val _refoundOrders: MutableStateFlow<List<OrderDTO>> = MutableStateFlow(emptyList())
    val refoundOrders: StateFlow<List<OrderDTO>> = _refoundOrders

    private val _productCardDTOList: MutableStateFlow<List<ProductCartDTO>> = MutableStateFlow(emptyList())
    val productCardDTOList: StateFlow<List<ProductCartDTO>> = _productCardDTOList

    private val _address: MutableStateFlow<String> = MutableStateFlow("")
    val address: StateFlow<String> = _address

    private val _card: MutableStateFlow<String> = MutableStateFlow("")
    val card: StateFlow<String> = _card


    fun getOrdersFromServer() {
        viewModelScope.launch {
            try{
                doGetOrdersFromServer()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }

    }

    suspend fun doGetOrdersFromServer() {
        val manageUrl = URL("http://25.49.50.144:8090/product-api/orders")
        val request =  Request.Builder().url(manageUrl).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()
        _orders.value = emptyList()
        _refoundOrders.value = emptyList()

        withContext(Dispatchers.IO){
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
            }

            val orders = object : TypeToken<List<OrderDTO>>() {}.type
            val ordersList = gson.fromJson<List<OrderDTO>>(responseBody, orders)
            for(order in ordersList){
                if(order.refund)
                    _refoundOrders.value += order
                else
                    _orders.value += order
            }
        }

    }

    fun getProductInOrder(orderId: String) {
        viewModelScope.launch {
            try {
                doGetProductInOrder(orderId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun doGetProductInOrder(orderId: String){
        val manageUrl = URL("http://25.49.50.144:8090/product-api/order/products/$orderId")
        val request =  Request.Builder().url(manageUrl).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()
        _productCardDTOList.value = emptyList()

        withContext(Dispatchers.IO){
            try{

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                }

                val productCardList = object : TypeToken<List<ProductCartDTO>>() {}.type
                val productCardDTOList = gson.fromJson<List<ProductCartDTO>>(responseBody, productCardList)
                _productCardDTOList.value = productCardDTOList


            }catch (e: Exception){
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
            }
        }
    }

    /*fun showOrderDetails(orderId: String, addressId: String, cardId: String){
        viewModelScope.launch {
            try {
                doShowOrderDetails(orderId, addressId, cardId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    suspend fun doShowOrderDetails(addressId: String, cardId: String){
        //_address.value = addressViewModel.getAddressesFromServer(UUID.fromString(addressId))
    }*/

    fun reFund(orderId: String, username: String){
        viewModelScope.launch {
            try{
                doReFund(orderId, username)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun doReFund(orderId: String, username: String) {
        val purchaseDTO = RefundDTO(orderId)


        // Debug: Print whether the user is admin and which URL is being used
        println("User is admin: ${isAdmin.value}")

        if (isAdmin.value) {
            val manageUrl = URL("http://25.49.50.144:8090/product-api/admin/refund/$username")
            println("Using URL: $manageUrl")

            val JSON = "application/json; charset=utf-8".toMediaType()
            val json = gson.toJson(purchaseDTO)
            val requestBody = json.toRequestBody(JSON)

            // Debug: Print the JSON payload being sent
            println("JSON Payload: $json")

            val request = Request.Builder()
                .url(manageUrl)
                .put(requestBody)
                .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
                .build()

            // Debug: Print the Authorization header being used
            println("Authorization Header: Bearer ${myToken?.accessToken}")

            withContext(Dispatchers.IO) {
                try {
                    val response = client.newCall(request).execute()

                    // Debug: Print the HTTP response code
                    println("HTTP Response Code: ${response.code}")

                    val responseBody = response.body?.string()

                    // Debug: Print the response body
                    println("Response Body: $responseBody")

                    if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
                        println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                    }

                    // Debug: Print the lists before modification
                    println("Orders before: ${_orders.value}")
                    println("Refund orders before: ${_refoundOrders.value}")

                    val refundOrder = _orders.value.find { it.id == orderId }
                        ?: _refoundOrders.value.find { it.id == orderId }

                    if (refundOrder != null) {
                        val order = _orders.value.find { it.id == orderId }
                        if (order != null) {
                            _orders.value -= order
                        }
                        _refoundOrders.value += refundOrder
                    }

                    // Debug: Print the lists after modification
                    println("Orders after: ${_orders.value}")
                    println("Refund orders after: ${_refoundOrders.value}")

                } catch (e: Exception) {
                    e.printStackTrace()
                    println("Errore durante la chiamata: ${e.message}")
                }
            }
        } else {
            val manageUrl = URL("http://25.49.50.144:8090/product-api/refund")
            // Debug: Print the URL being used
            println("Using URL: $manageUrl")

            val JSON = "application/json; charset=utf-8".toMediaType()
            val json = gson.toJson(purchaseDTO)
            val requestBody = json.toRequestBody(JSON)

            // Debug: Print the JSON payload being sent
            println("JSON Payload: $json")

            val request = Request.Builder()
                .url(manageUrl)
                .put(requestBody)
                .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
                .build()

            // Debug: Print the Authorization header being used
            println("Authorization Header: Bearer ${myToken?.accessToken}")

            withContext(Dispatchers.IO) {
                try {
                    val response = client.newCall(request).execute()

                    // Debug: Print the HTTP response code
                    println("HTTP Response Code: ${response.code}")

                    val responseBody = response.body?.string()

                    // Debug: Print the response body
                    println("Response Body: $responseBody")

                    if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
                        println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                    }

                    // Debug: Print the lists before modification
                    println("Orders before: ${_orders.value}")
                    println("Refund orders before: ${_refoundOrders.value}")

                    val refundOrder = _orders.value.find { it.id == orderId }
                        ?: _refoundOrders.value.find { it.id == orderId }

                    if (refundOrder != null) {
                        val order = _orders.value.find { it.id == orderId }
                        if (order != null) {
                            _orders.value -= order
                        }
                        _refoundOrders.value += refundOrder
                    }

                    // Debug: Print the lists after modification
                    println("Orders after: ${_orders.value}")
                    println("Refund orders after: ${_refoundOrders.value}")

                } catch (e: Exception) {
                    e.printStackTrace()
                    println("Errore durante la chiamata: ${e.message}")
                }
            }
        }
    }

}

