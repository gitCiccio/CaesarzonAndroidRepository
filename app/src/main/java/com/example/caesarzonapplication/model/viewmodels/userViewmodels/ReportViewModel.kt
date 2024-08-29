package com.example.caesarzonapplication.model.viewmodels.userViewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caesarzonapplication.model.dto.AddressDTO
import com.example.caesarzonapplication.model.dto.ReportDTO
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

class ReportViewModel: ViewModel() {
    private val client = OkHttpClient()
    private val gson = Gson()

    private val _reports: MutableStateFlow<List<ReportDTO>> = MutableStateFlow(emptyList())
    val reports: StateFlow<List<ReportDTO>> = _reports


    fun sendReport(reportDTO: ReportDTO){
        viewModelScope.launch {
            try{
                doSendReport(reportDTO)
            }catch (e: Exception){
                e.printStackTrace()
            }

        }
    }

    suspend fun doSendReport(reportDTO: ReportDTO){
        val JSON = "application/json; charset=utf-8".toMediaType()
        val json = gson.toJson(reportDTO)
        val requestBody = json.toRequestBody(JSON)

        val manageUrl = URL("http://25.49.50.144:8090/notify-api/report")
        val request = Request.Builder().url(manageUrl).post(requestBody).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

        withContext(Dispatchers.IO){
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                    println("Chiamata fallita o risposta vuota, nell'invio del report. Codice di stato: ${response.code}")
                    return@withContext
                }

                _reports.value.toMutableList().add(reportDTO)
                println("Report inviato correttamente")
            }catch (e: Exception){
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
                return@withContext
            }
        }
    }

    suspend fun doDeleteReportFromProduct(reviewDTO: ReportDTO){
        val manageUrl = URL("http://25.49.50.144:8090/notify-api/user/report?review=${reviewDTO.id}")
        val request = Request.Builder().url(manageUrl).delete().addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

        withContext(Dispatchers.IO){

            try{

                _reports.update { reports -> reports.filter { it.id != reviewDTO.id } }
                println("Report eliminato con successo")

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                println("response code: ${response.code}")
                if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                }
                println("Risposta dal server: $responseBody")

            }catch (e: Exception){
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
            }
        }
    }
}