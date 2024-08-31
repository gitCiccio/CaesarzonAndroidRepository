package com.example.caesarzonapplication.model.viewmodels.adminViewModels

import androidx.compose.runtime.mutableStateListOf
import com.example.caesarzonapplication.model.dto.notificationDTO.SupportDTO
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
import org.json.JSONArray
import java.io.IOException
import java.net.URL
import java.util.UUID

class AdminSupportRequestViewModel {

    val client = OkHttpClient()
    val gson = Gson()

    private val _supportRequests: MutableStateFlow<List<SupportDTO>> = MutableStateFlow(emptyList())
    val supportRequests: StateFlow<List<SupportDTO>> get() = _supportRequests

    private val num:Int = 0


    //dopo che prendi le richieste di supporto, quelle che vengono gestite devono essere eliminate chiamando la delete

    suspend fun searchSupportRequests() {
        val manageURL = URL("http://25.49.50.144:8090/notify-api/support?num=${num}");
        val request = Request.Builder().url(manageURL)
            .addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()
        withContext(Dispatchers.IO){
            _supportRequests.value = emptyList()
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
                    println("Problemi nella richiesta di supporto: ${response.message}")
                    return@withContext
                }

                val supportRequestList = object : TypeToken<List<SupportDTO>>() {}.type
                val supportRequests = gson.fromJson<List<SupportDTO>>(responseBody, supportRequestList)
                _supportRequests.value = supportRequests
                num.inc()

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun deleteSupport(supportDTOId: UUID, explain: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val manageURL = "http://25.49.50.144:8090/notify-api/support?support-id=${supportDTOId}&explain=${explain}"
            val request = Request.Builder()
                .url(manageURL)
                .delete()
                .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
                .build()

            try {
                val response = client.newCall(request).execute() // Chiamata sincrona
                val responseBody = response.body?.string()

                if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
                    println("Problemi nella richiesta di supporto: ${response.message}")
                    return@launch
                }

                // Rimuovi la richiesta di supporto dalla lista locale
                _supportRequests.value = _supportRequests.value.filter { it.id != supportDTOId.toString() }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }



}