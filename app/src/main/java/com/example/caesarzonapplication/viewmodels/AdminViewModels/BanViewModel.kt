package com.example.caesarzonapplication.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.caesarzonapplication.model.dto.BanDTO
import com.example.caesarzonapplication.model.dto.UserFindDTO
import com.example.caesarzonapplication.model.dto.UserSearchDTO
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import java.io.IOException
import java.net.URL
import java.time.LocalDate

class BanViewModel : ViewModel() {


    val client = OkHttpClient()

    private val _bans = mutableStateListOf<BanDTO>()
    val bans: List<BanDTO> get() = _bans
    //Rendere i numeri per le chiamate dinamici

    //da capire come fare l'init
    init {
        loadBannedUser()
    }

    fun banUser(ban: UserFindDTO){
        CoroutineScope(Dispatchers.IO).launch {
            val manageURL = URL("http://25.49.50.144:8090/user-api/ban")
            val banDTO = BanDTO(reason = "Decisione dell'admin", startDate = LocalDate.now().toString(), endDate = "", userUsername = ban.username, adminUsername = "cesare")
            val gson = Gson()
            val json = gson.toJson(banDTO)
            val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
            val requestBody = json.toRequestBody(mediaType)
            val request = Request
                .Builder()
                .url(manageURL)
                .post(requestBody)
                .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
                .build()
            try{
                val response = client.newCall(request).execute()
                println("messaggio: "+response.message)
                if(response.isSuccessful)
                    println("Utente bannato con successo")
                else{
                    println("Errore nella bannatura dell'utente")
                    return@launch
                }
            }catch (e: IOException){
                e.printStackTrace()
                println("Errore nella bannatura dell'utente")
            }
        }
    }
    fun loadBannedUser(){
        CoroutineScope(Dispatchers.IO).launch {
            val manageURL = URL("http://25.49.50.144:8090/user-api/bans?str=0")
            val request = Request
                .Builder()
                .url(manageURL)
                .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
                .build()
            try{
                val response = client.newCall(request).execute()
                println("response ban"+response.message)
                if(!response.isSuccessful)
                    return@launch
                _bans.clear()
                val responseBody = response.body?.string()
                val jsonResponse = JSONArray(responseBody)
                for(i in 0 until jsonResponse.length()){
                    val username = jsonResponse.getJSONObject(i).getString("username")
                    _bans.add(BanDTO("", "", "", username, ""))
                }
                for(ban in _bans)
                    println(ban.userUsername)
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    fun deleteBan(ban: BanDTO){
        CoroutineScope(Dispatchers.IO).launch {
            val manageURL = URL("http://25.49.50.144:8090/user-api/ban/"+ban.userUsername)
            val request = Request
                .Builder()
                .url(manageURL)
                .put("".toRequestBody())
                .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
                .build()
            try{
                val response = client.newCall(request).execute()
                println("messaggio: "+response.message)
                if(!response.isSuccessful)
                    return@launch
                _bans.remove(ban)
                println("Ban eliminato con successo")
            }catch (e:IOException){
                e.printStackTrace()
            }
        }
    }
}