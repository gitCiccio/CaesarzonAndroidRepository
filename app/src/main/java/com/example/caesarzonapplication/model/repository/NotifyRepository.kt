package com.example.caesarzonapplication.model.repository

import com.example.caesarzonapplication.model.dto.AdminNotificationDTO
import com.example.caesarzonapplication.model.dto.UserNotificationDTO
import com.example.caesarzonapplication.model.service.KeycloakService
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.net.URL
import java.util.UUID

class NotifyRepository {

    private val client = OkHttpClient()
    val _notificationAdmin = MutableStateFlow<List<AdminNotificationDTO>>(emptyList())
    val notificationAdmin: StateFlow<List<AdminNotificationDTO>> get() = _notificationAdmin

    val _notificationUser = MutableStateFlow<List<UserNotificationDTO>>(emptyList())
    val userNotification: StateFlow<List<UserNotificationDTO>> get() = _notificationUser


    suspend fun loadNotifcationAdmin(){

        withContext(Dispatchers.IO){
            val manageURL = URL("http://25.49.50.144:8090/notify-api/admin/notifications")
            val request = Request
                .Builder()
                .url(manageURL)
                .get()
                .addHeader("Authorization", "Bearer ${KeycloakService.myToken?.accessToken}")
                .build()
            try{
                val response = client.newCall(request).execute()
                if(!response.isSuccessful) return@withContext
                response.body?.string()?.let { responseBody ->
                    val gson = Gson()
                    val notificationType = object : TypeToken<List<AdminNotificationDTO>>() {}.type
                    val notifications: List<AdminNotificationDTO> =
                        gson.fromJson(responseBody, notificationType)
                    _notificationAdmin.emit(notifications)
                }
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    suspend fun loadNotificationsUser(){

        withContext(Dispatchers.IO){
            val manageURL = URL("http://25.49.50.144:8090/notify-api/user/notifications")
            val request = Request
                .Builder()
                .url(manageURL)
                .get()
                .addHeader("Authorization", "Bearer ${KeycloakService.myToken?.accessToken}")
                .build()
            try{
                val response = client.newCall(request).execute()
                if(!response.isSuccessful) return@withContext
                response.body?.string()?.let { responseBody ->
                    val gson = Gson()
                    val notificationType = object : TypeToken<List<UserNotificationDTO>>() {}.type
                    val notifications: List<UserNotificationDTO> =
                        gson.fromJson(responseBody, notificationType)
                    _notificationUser.emit(notifications)
                }

            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

}