package com.example.caesarzonapplication.model.repository

import com.example.caesarzonapplication.model.dto.AdminNotificationDTO
import com.example.caesarzonapplication.model.service.KeycloakService
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

class NotifyRepository {

    private val client = OkHttpClient()
    private val _notificationAdmin = MutableStateFlow<List<AdminNotificationDTO>>(emptyList())
    val notificationAdmin: StateFlow<List<AdminNotificationDTO>> get() = _notificationAdmin

    private var notificationAdminCache: List<AdminNotificationDTO>? = null

    suspend fun loadNotifcationAdmin(){
        notificationAdminCache?.let {
            _notificationAdmin.value = it
            return
        }

        withContext(Dispatchers.IO){
            println("Sono sono nella courotine delle notifiche")
            val manageURL = URL("http://25.49.50.144:8090/notify-api/admin/notifications")
            val request = Request
                .Builder()
                .url(manageURL)
                .get()
                .addHeader("Authorization", "Bearer ${KeycloakService.myToken?.accessToken}")
                .build()
            try{
                val response = client.newCall(request).execute()
                println("response notification admin api: "+ response.message+" "+response.code)
                if(!response.isSuccessful) return@withContext
                response.body?.string()?.let { responseBody ->
                    val gson = Gson()
                    val notificationType = object : TypeToken<List<AdminNotificationDTO>>() {}.type
                    val notifications: List<AdminNotificationDTO> =
                        gson.fromJson(responseBody, notificationType)
                    notificationAdminCache = notifications
                    _notificationAdmin.emit(notifications)
                    for(notification in _notificationAdmin.value)
                        println("Notifica: "+notification)
                }

            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }



}