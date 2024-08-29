package com.example.caesarzonapplication.model.viewmodels.userViewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.caesarzonapplication.model.dto.SupportDTO
import com.example.caesarzonapplication.model.repository.notificationRepository.SupportRepository
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

//Promosso
class SupportRequestsViewModel(private val supportRepository: SupportRepository): ViewModel() {
    private val client = OkHttpClient()
    val gson = Gson()

    private val _supports: MutableStateFlow<List<SupportDTO>> = MutableStateFlow(emptyList())
    val supports: StateFlow<List<SupportDTO>> = _supports

    //Prendi tutte le richieste di supporto
    fun getAllSupports(){
        viewModelScope.launch {
            try{
                doGetAllSupports()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun doGetAllSupports(){
        val managerUrl = URL("http://25.49.50.144:8090/notify-api/support?num=${0}")
        val request = Request.Builder().url(managerUrl).build()

        withContext(Dispatchers.IO){
            try{
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                    return@withContext
                }
                val supportsList = object : TypeToken<List<SupportDTO>>() {}.type
                _supports.value = gson.fromJson(responseBody, supportsList)
                println("Supports recuperate con successo: ${_supports.value.size}")
            }catch (e: Exception){
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
            }
        }
    }

    fun addSupport(support: SupportDTO){
        viewModelScope.launch {
            try{
                doAddSupport(support)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun doAddSupport(support: SupportDTO){
        val manageUrl = URL("http://25.49.50.144:8090/notify-api/support")
        val JSON = "application/json; charset=utf-8".toMediaType()
        val json = gson.toJson(support)
        val requestBody = json.toRequestBody(JSON)

        val request = Request.Builder().url(manageUrl).post(requestBody).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()
        withContext(Dispatchers.IO){
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
            }
            println("Risposta dal server: $responseBody")
            _supports.value.toMutableList().add(support)
            println("Richiesta di supporto aggiunta con successo")

        }
    }

    fun deleteSupport(supportId: String, explain: String){
        viewModelScope.launch {
            try{
                doDeleteSupport(supportId, explain)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun doDeleteSupport(supportId: String, explain: String){
        val manageUrl = URL("http://25.49.50.144:8090/notify-api/support?id=$supportId&explain=$explain")
        val request = Request.Builder().url(manageUrl).delete().addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

        withContext(Dispatchers.IO){
            try{
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                println("Risposta dal server: $responseBody")
                _supports.value.toMutableList().removeIf { it.id == supportId }
                println("Richiesta di supporto eliminata con successo")

                if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                }
            }catch (e: Exception){
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
            }
        }
    }
}
class SupportRequestsViewModelFactory(
    private val supportRepository: SupportRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SupportRequestsViewModel::class.java)) {
            return SupportRequestsViewModel(supportRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}