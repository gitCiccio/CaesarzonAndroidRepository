package com.example.caesarzonapplication.model.viewmodels.adminViewModels

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caesarzonapplication.model.dto.notificationDTO.BanDTO
import com.example.caesarzonapplication.model.dto.userDTOS.UserFindDTO
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.basicToken
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import com.example.caesarzonapplication.model.utils.BitmapConverter
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import java.io.IOException
import java.net.URL
import java.time.LocalDate

class SearchAndBanUsersViewModel: ViewModel() {
    val client = OkHttpClient()
    val gson = Gson()
    val bitampConverter = BitmapConverter()


    private val _searchResults: MutableStateFlow<List<UserFindDTO>> = MutableStateFlow(emptyList())
    val searchResults: StateFlow<List<UserFindDTO>> get() = _searchResults


    private val _bans = mutableStateListOf<BanDTO>()
    val bans: List<BanDTO> get() = _bans

    private val str:Int = 0

    fun searchUsers() {
        viewModelScope.launch {
            doSearchUsers()
        }
    }



    suspend fun doSearchUsers(){
        val manageURL = URL("http://25.49.50.144:8090/user-api/users?str=${str}");
        val request = Request.Builder().url(manageURL)
            .addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

        withContext(Dispatchers.IO){
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
                    println("Errore nel caricamento degli utenti")
                    return@withContext
                }

                _searchResults.value = emptyList()
                val usersFindDTO = Gson().fromJson(responseBody, Array<UserFindDTO>::class.java)
                for (user in usersFindDTO) {
                    val image = loadUserImage(user.username)
                    _searchResults.value += UserFindDTO(user.username, image)
                }
                str.inc()

            } catch (e: IOException) {
                e.printStackTrace()
                println("Errore nel caricamento degli utenti")
            }
        }
    }


    suspend fun loadUserImage(username: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            val manageUrl = URL("http://25.49.50.144:8090/user-api/image/$username")
            val request = Request.Builder()
                .url(manageUrl)
                .addHeader("Authorization", "Bearer ${basicToken?.accessToken}")
                .build()

            try {
                val response = client.newCall(request).execute()
                if (!response.isSuccessful || response.body == null) {
                    println("Problemi nel caricamento della foto profilo" + response.message + " code: " + response.code)
                    return@withContext null
                }
                val imageByteArray = response.body?.byteStream()?.readBytes()

                if (imageByteArray != null) {
                    val profileImage = bitampConverter.converterByteArray2Bitmap(imageByteArray)
                    println("immagine presae: " + response.message + " " + response.code)
                    return@withContext profileImage
                } else {
                    println("Response body is null")
                }

            } catch (e: Exception) {
                e.printStackTrace()
                println("Errore nel caricamento dell'immagine")
            }
            return@withContext null
        }
    }

    /*
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
                if(response.isSuccessful) {
                    _searchResults.removeIf { it.username == ban.username}
                    println("Utente bannato con successo")
                }
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
                val manageURLProfilePic =
                    URL("http://25.49.50.144:8090/user-api/image/${ban.userUsername}")
                val responseImage = client.newCall(
                    Request.Builder().url(manageURLProfilePic)
                        .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
                        .build()
                ).execute()
                println("second response: "+responseImage.message)
                if (!responseImage.isSuccessful)
                    return@launch
                val responseBodyImage = responseImage.body?.string()
                val profilePictureBase64 = responseBodyImage ?: ""
                _searchResults.add(UserFindDTO(ban.userUsername, profilePictureBase64))
                _bans.remove(ban)
                println("Ban eliminato con successo")
            }catch (e:IOException){
                e.printStackTrace()
            }
        }
    }*/

}