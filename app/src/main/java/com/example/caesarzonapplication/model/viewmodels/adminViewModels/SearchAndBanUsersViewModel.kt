package com.example.caesarzonapplication.model.viewmodels.adminViewModels

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caesarzonapplication.model.dto.notificationDTO.BanDTO
import com.example.caesarzonapplication.model.dto.notificationDTO.UserSearchDTO
import com.example.caesarzonapplication.model.dto.userDTOS.SbanDTO
import com.example.caesarzonapplication.model.dto.userDTOS.UserFindDTO
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.basicToken
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import com.example.caesarzonapplication.model.utils.BitmapConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import java.io.IOException
import java.net.URL

class SearchAndBanUsersViewModel : ViewModel() {
    private val client = OkHttpClient()
    private val gson = Gson()
    private val bitmapConverter = BitmapConverter()

    private val _searchResults: MutableStateFlow<List<UserFindDTO>> = MutableStateFlow(emptyList())
    val searchResults: StateFlow<List<UserFindDTO>> get() = _searchResults

    private val _bannedUsers: MutableStateFlow<List<BanDTO>> = MutableStateFlow(emptyList())
    val bannedUsers: StateFlow<List<BanDTO>> get() = _bannedUsers

    private var str: Int = 0


    init {
        loadBannedUsers()
    }

    fun searchUsers(searchText: String) {
        viewModelScope.launch {
            doSearchUsers(searchText)
        }
    }

    private suspend fun doSearchUsers(searchText: String) {
        val manageURL = URL("http://25.49.50.144:8090/user-api/users?str=$str")
        val request = Request.Builder().url(manageURL)
            .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
            .build()

        withContext(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
                    println("Error loading users: ${response.message}")
                    response.close()
                    return@withContext
                }

                val usersFindDTO = gson.fromJson(responseBody, Array<UserFindDTO>::class.java)
                response.close()
                val existingUsers = _searchResults.value.associateBy { it.username }

                val newUsers = usersFindDTO.filterNot { existingUsers.containsKey(it.username) }.map { user ->
                    val image = loadUserImage(user.username)
                    UserFindDTO(user.username, image)
                }

                if (newUsers.isNotEmpty()) {
                    _searchResults.update { it + newUsers }
                }

                _searchResults.update { users ->
                    users.filter { it.username.contains(searchText, ignoreCase = true) }
                }

            } catch (e: IOException) {
                e.printStackTrace()
                println("Error loading users")
            }
        }
    }

    private suspend fun loadUserImage(username: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            val manageUrl = URL("http://25.49.50.144:8090/user-api/image/$username")
            val request = Request.Builder()
                .url(manageUrl)
                .addHeader("Authorization", "Bearer ${basicToken?.accessToken}")
                .build()

            try {
                val response = client.newCall(request).execute()
                response.use {
                    if (!response.isSuccessful) {
                        println("Error loading profile picture: ${response.message} code: ${response.code}")
                        return@withContext null
                    }
                    val imageByteArray = response.body?.byteStream()?.readBytes()
                    return@withContext imageByteArray?.let { bitmapConverter.converterByteArray2Bitmap(it) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                println("Error loading image")
                return@withContext null
            }
        }
    }

    fun banUser(ban: UserFindDTO) {
        viewModelScope.launch(Dispatchers.IO) {
            val manageURL = URL("http://25.49.50.144:8090/user-api/ban")
            val banDTO = BanDTO(
                reason = "Decisione dell'admin",
                startDate = "",
                endDate = "",
                userUsername = ban.username,
                adminUsername = "",
                confirmed = false
            )
            val json = gson.toJson(banDTO)
            val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
            val requestBody = json.toRequestBody(mediaType)
            val request = Request.Builder()
                .url(manageURL)
                .post(requestBody)
                .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
                .build()

            try {
                val response = client.newCall(request).execute()
                response.use {
                    if (response.isSuccessful) {
                        _searchResults.update { it.filter { user -> user.username != ban.username } }
                        println("User banned successfully")
                    } else {
                        println("Error banning user: ${response.message}")
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                println("Error banning user")
            }
        }
    }

    fun loadBannedUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            val manageURL = URL("http://25.49.50.144:8090/notify-api/bans?num=0")
            val request = Request.Builder()
                .url(manageURL)
                .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
                .build()

            try {
                val response = client.newCall(request).execute()
                response.use {
                    if (!response.isSuccessful) {
                        println("Error loading banned users: ${response.message}")
                        return@launch
                    }

                    val responseBody = response.body?.string()
                    val jsonResponse = JSONArray(responseBody)

                    val bansList = object : TypeToken<List<BanDTO>>() {}.type
                    _bannedUsers.value = gson.fromJson(jsonResponse.toString(), bansList)
                    _searchResults.value = emptyList()

                    for (ban in _bannedUsers.value) {
                        println("Banned user: ${ban.userUsername}")
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                println("Error loading banned users")
            }
        }
    }

    fun deleteBan(ban: BanDTO) {
        viewModelScope.launch(Dispatchers.IO) {
            val sbanDTO = SbanDTO(username = ban.userUsername)
            val manageURL = URL("http://25.49.50.144:8090/user-api/sban")
            val json = gson.toJson(sbanDTO)
            val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
            val requestBody = json.toRequestBody(mediaType)

            val request = Request.Builder()
                .url(manageURL)
                .put(requestBody)
                .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
                .build()

            try {
                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        println("Error deleting ban: ${response.message}")
                        return@launch
                    }

                    _bannedUsers.update { currentBans ->
                        currentBans.filter { banUser -> banUser.userUsername != ban.userUsername }
                    }

                    println("risposta: ${response.message}"+response.code)
                    println("Ban deleted successfully")
                }
            } catch (e: IOException) {
                e.printStackTrace()
                println("Error deleting ban")
            }
        }
    }

}
