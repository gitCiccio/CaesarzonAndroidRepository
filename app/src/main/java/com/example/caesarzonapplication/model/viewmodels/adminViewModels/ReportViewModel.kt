package com.example.caesarzonapplication.model.viewmodels.adminViewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.caesarzonapplication.model.dto.ReportDTO
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

class ReportViewModel : ViewModel() {

    private val client = OkHttpClient()

    private val _reports = mutableStateListOf<ReportDTO>()
    val reports: List<ReportDTO> get() = _reports

    init{
        searchReports()
    }


    fun searchReports(){
        CoroutineScope(Dispatchers.IO).launch {
            val manageURL = URL("http://25.49.50.144:8090/notify-api/report?num=0");
            val request = Request.Builder().url(manageURL).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()
            try{
                val response = client.newCall(request).execute()
                if(!response.isSuccessful)
                    return@launch
                val responseBody = response.body?.string()
                val jsonResponse = JSONArray(responseBody)
                _reports.clear()
                for(i in 0 until jsonResponse.length()){
                    val id = jsonResponse.getJSONObject(i).getString("id")
                    val reportDate = jsonResponse.getJSONObject(i).getString("reportDate")
                    val reason = jsonResponse.getJSONObject(i).getString("reason")
                    val description = jsonResponse.getJSONObject(i).getString("description")
                    val usernameUser1 = jsonResponse.getJSONObject(i).getString("usernameUser1")
                    val usernameUser2 = jsonResponse.getJSONObject(i).getString("usernameUser2")
                    val reviewId = jsonResponse.getJSONObject(i).getString("reviewId")
                    _reports.add(ReportDTO(UUID.fromString(id).toString(), reportDate, reason, description, usernameUser1, usernameUser2, UUID.fromString(reviewId)))
                }


            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    fun deleteReport(reportDTO: ReportDTO, decision: Boolean){
        val manageURL = URL("http://25.49.50.144:8090/notify-api/admin/report?review_id=${reportDTO.reviewId}&accept=${decision}")

        CoroutineScope(Dispatchers.IO).launch {
            val request = Request.Builder()
                .url(manageURL)
                .delete()
                .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
                .build()

            try {
                val response = client.newCall(request).execute()
                response.use {
                    if (response.isSuccessful) {
                        _reports.removeIf { it.id == reportDTO.id}
                        println("Richiesta di supporto eliminata con successo")
                    } else {
                        println("Problemi nell'eliminazione della richiesta di supporto: ${response.message}")
                    }
                }

            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    fun loadReports() {
        TODO("Not yet implemented")
    }
}