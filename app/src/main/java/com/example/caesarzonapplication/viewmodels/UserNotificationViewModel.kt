package com.example.caesarzonapplication.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.caesarzonapplication.model.dto.UserNotificationDTO
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.io.IOException
import java.net.URL
import java.util.UUID

class UserNotificationViewModel : ViewModel() {

    val client = OkHttpClient()
    private val _userNotifications = mutableStateListOf<UserNotificationDTO>()
    val userNotifications: List<UserNotificationDTO> get() = _userNotifications

    init{
        loadUserNotifications()
    }

    fun loadUserNotifications(){
        CoroutineScope(Dispatchers.IO).launch {
            println("Il mio token: "+ myToken?.accessToken)
            val manageURL = URL("http://25.49.50.144:8090/notify-api/user/notifications")
            val request = Request.Builder().url(manageURL).addHeader("Authorization", "Bearer  ${myToken?.accessToken}").build()
            try {
                val response = client.newCall(request).execute()
                println("valore della risposta: "+response.message)
                if(!response.isSuccessful){
                    return@launch
                }
                val responseBody = response.body?.string()
                val jsonResponse = JSONArray(responseBody)
                _userNotifications.clear()
                for(i in 0 until jsonResponse.length()){
                    val id = jsonResponse.getJSONObject(i).getString("id")
                    val date = jsonResponse.getJSONObject(i).getString("date")
                    val subject = jsonResponse.getJSONObject(i).getString("subject")
                    val user = jsonResponse.getJSONObject(i).getString("user")
                    val read = jsonResponse.getJSONObject(i).getBoolean("read")
                    val explanation = jsonResponse.getJSONObject(i).getString("explanation")
                    _userNotifications.add(UserNotificationDTO(UUID.fromString(id), date, subject, user, read, explanation))
                }
                for(notify in _userNotifications)
                    println(notify.user)
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

}