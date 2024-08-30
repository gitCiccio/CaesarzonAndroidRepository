package com.example.caesarzonapplication.model.viewmodels.adminViewModels

import androidx.compose.runtime.mutableStateListOf
import com.example.caesarzonapplication.model.dto.notificationDTO.SupportDTO
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

class AdminSupportRequestViewModel {

    val client = OkHttpClient()

    private val _supportRequests = mutableStateListOf<SupportDTO>()
    val supportRequests: List<SupportDTO> get() = _supportRequests

    init {
        searchSupportRequests()
    }

    //dopo che prendi le richieste di supporto, quelle che vengono gestite devono essere eliminate chiamando la delete

    fun searchSupportRequests() {
        CoroutineScope(Dispatchers.IO).launch {
            val manageURL = URL("http://25.49.50.144:8090/notify-api/support?num=0");
            val request = Request.Builder().url(manageURL)
                .addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

            try {
                val response = client.newCall(request).execute()
                if (!response.isSuccessful)
                    return@launch
                val responseBody = response.body?.string()
                val jsonResponse = JSONArray(responseBody)
                _supportRequests.clear()
                for (i in 0 until jsonResponse.length()) {
                    val id = jsonResponse.getJSONObject(i).getString("id")
                    val username = jsonResponse.getJSONObject(i).getString("username")
                    val type = jsonResponse.getJSONObject(i).getString("type")
                    val subject = jsonResponse.getJSONObject(i).getString("subject")
                    val text = jsonResponse.getJSONObject(i).getString("text")
                    val localDate = jsonResponse.getJSONObject(i).optString("localDate", "")
                    _supportRequests.add(SupportDTO(id, username, type, subject, text, localDate))
                }
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

                response.use {
                    if (response.isSuccessful) {
                        _supportRequests.removeIf { it.id == supportDTOId.toString() }
                        println("Richiesta di supporto eliminata con successo")
                    } else {
                        println("Problemi nell'eliminazione della richiesta di supporto: ${response.message}")
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }



}