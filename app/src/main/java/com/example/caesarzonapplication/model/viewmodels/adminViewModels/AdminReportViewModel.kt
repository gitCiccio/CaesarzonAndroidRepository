package com.example.caesarzonapplication.model.viewmodels.adminViewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caesarzonapplication.model.dto.notificationDTO.ReportDTO
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

class AdminReportViewModel : ViewModel() {

    private val client = OkHttpClient()
    private val gson = Gson()
    private val num:Int = 0

    private val _reports: MutableStateFlow<List<ReportDTO>> = MutableStateFlow(emptyList())
    val reports: StateFlow<List<ReportDTO>> get() = _reports

    fun loadReports() {
        viewModelScope.launch {
            try{
                doSearchReports()
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    suspend fun doSearchReports(){
        val manageURL = URL("http://25.49.50.144:8090/notify-api/report?num=${num}");
        val request = Request.Builder().url(manageURL).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()
        _reports.value = emptyList()
        withContext(Dispatchers.IO){
            try{
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                if(!response.isSuccessful || responseBody.isNullOrEmpty()) {
                    println("Problemi nella richiesta di supporto: ${response.message}")
                    return@withContext
                }

                val listRepost = object : TypeToken<List<ReportDTO>>() {}.type
                val reports = gson.fromJson<List<ReportDTO>>(responseBody, listRepost)
                println(reports)
                _reports.value = reports
                num.inc()

            }catch (e: IOException){
                e.printStackTrace()
                println("Problemi nella richiesta di supporto: ${e.message}")
            }
        }
    }


    fun deleteReport(reportDTO: ReportDTO, decision: Boolean){
        viewModelScope.launch {
            try{
                doDeleteReport(reportDTO, decision)
            }catch (e: Exception){
                e.printStackTrace()
                println("Problemi nella richiesta di supporto: ${e.message}")
            }
        }
    }
    suspend fun doDeleteReport(reportDTO: ReportDTO, decision: Boolean){
        val manageURL = URL("http://25.49.50.144:8090/notify-api/admin/report?review_id=${reportDTO.reviewId}&accept=${decision}")
        val request = Request.Builder()
            .url(manageURL)
            .delete()
            .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
            .build()

        withContext(Dispatchers.IO) {

            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
                    println("Problemi nella richiesta di supporto: ${response.message}")
                    return@withContext
                }

                _reports.value = _reports.value.filter { it.id != reportDTO.id }

            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

}