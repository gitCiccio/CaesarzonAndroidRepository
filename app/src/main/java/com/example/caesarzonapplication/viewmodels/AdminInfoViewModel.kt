package com.example.caesarzonapplication.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caesarzonapplication.model.Ban
import com.example.caesarzonapplication.model.Report
import com.example.caesarzonapplication.model.SupportRequest
import com.example.caesarzonapplication.model.UserFindDTO
import com.example.caesarzonapplication.model.dto.UserDTO
import com.example.caesarzonapplication.model.service.KeycloakService
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class AdminInfoViewModel : ViewModel() {

    val client = OkHttpClient()
    private val _searchResults = mutableStateListOf<UserFindDTO>()
    val searchResults: List<UserFindDTO> get() = _searchResults

    private val _reports = mutableStateListOf<Report>()
    val reports: List<Report> get() = _reports

    private val _supportRequests = mutableStateListOf<SupportRequest>()
    val supportRequests: List<SupportRequest> get() = _supportRequests

    private val _bans = mutableStateListOf<Ban>()
    val bans: List<Ban> get() = _bans

    init {
        searchUsers()
    }

    fun searchUsers() {
        CoroutineScope(Dispatchers.IO).launch {
            println("Sono la funzione di ricerca di tutti gli utenti")
            val manageURL = URL("http://25.49.50.144:8090/user-api/users?str=1");
            val request = Request.Builder().url(manageURL).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()
            try {
                println("Sono nel try")
                val response = client.newCall(request).execute()
                println(response.message)
                if(!response.isSuccessful){
                    println("Errore nella ricerca degli utenti")
                    return@launch
                }
                val responseBody = response.body?.string()
                val jsonResponse = JSONArray(responseBody)
                _searchResults.clear()
                for (i in 0 until jsonResponse.length()) {
                    val username = jsonResponse.getJSONObject(i).getString("username")
                    val profilePictureBase64 = jsonResponse.getJSONObject(i).optString("profilePicture", "")
                    _searchResults.add(UserFindDTO(username, profilePictureBase64))
                }
                println(_searchResults.toString())


            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}