package com.example.caesarzonapplication.model.viewmodels.userViewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.caesarzonapplication.model.dto.UserNotificationDTO
import com.example.caesarzonapplication.model.repository.notificationRepository.UserNotificationRepository
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.URL
import java.util.UUID

class NotificationViewModel(private val userNotificationRepository: UserNotificationRepository): ViewModel() {
    //porssimo viewmodel che gestisce le notifiche
    private val _userNotification = MutableStateFlow<List<UserNotificationDTO>>(emptyList()) // Stato iniziale vuoto
    val userNotification: StateFlow<List<UserNotificationDTO>> get() = _userNotification

    val client = OkHttpClient()
    val gson = Gson()

    fun getNotification() {
        viewModelScope.launch {
            doGetNotification()
        }
    }

    fun deleteNotification(notification: UserNotificationDTO){
        viewModelScope.launch {
            doDeleteNotification(notification, true)
        }
    }

    fun updateNotification(){
        viewModelScope.launch {
            doUpdateNotification()
        }
    }

    //Funzione delle notifiche funziona
    suspend fun doGetNotification() {
        println("Mi stanno chiamando")
        val manageUrl = URL("http://25.49.50.144:8090/notify-api/user/notifications")
        val request = Request.Builder().url(manageUrl).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build() // Assicurati che il token non sia nullo

        withContext(Dispatchers.IO){
            try{
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                println("Risposta dal server: $responseBody")
                if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                    return@withContext
                }
                //Deserializzazione
                val notificationList: List<UserNotificationDTO> = gson.fromJson(responseBody, object : TypeToken<List<UserNotificationDTO>>() {}.type)
                for(notification in notificationList){
                    println(notification.toString())
                }
                _userNotification.value = notificationList // Aggiornamento diretto, StateFlow è thread-safe
                println("Notifiche recuperate con successo: ${notificationList.size}")
            }catch (e : Exception){
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
            }
        }
    }

    //Funziona eliminazione notifiche
    suspend fun doDeleteNotification(notification: UserNotificationDTO, isUser: Boolean){
        println("id della notifica: "+notification.id)
        val notificationId = UUID.fromString(notification.id)
        val manageURL = URL("http://25.49.50.144:8090/notify-api/notification?notify-id=${notificationId}&isUser=${isUser}")
        val request = Request.Builder().url(manageURL).delete().addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

        withContext(Dispatchers.IO){
            try{
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                _userNotification.update { currentList ->
                    currentList.toMutableList().apply {
                        remove(notification)
                    }
                }
                if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                    return@withContext
                }
                println("Risposta dal server: $responseBody")

            }catch (e: Exception){
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
            }
        }
    }

    suspend fun doUpdateNotification() {
        val currentNotification = _userNotification.value

        val udapadedNotification = currentNotification.map{ notification ->
            notification.copy(read = true)
        }

        _userNotification.value = udapadedNotification

        val manageUrl = URL("http://25.49.50.144:8090/notify-api/user/notifications")
        val JSON = "application/json; charset=utf-8".toMediaType()
        val json = gson.toJson(udapadedNotification)
        val requestBody = json.toRequestBody(JSON)
        val request = Request.Builder().url(manageUrl).put(requestBody).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

        withContext(Dispatchers.IO){
            try{
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                    return@withContext
                }
                println("Risposta dal server: $responseBody")
            }catch (e: Exception){
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
            }
        }

    }

}
class NotificationViewModelFactory(
    private val userRepositoryNotification: UserNotificationRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
            return NotificationViewModel(userRepositoryNotification) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}