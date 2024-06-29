package com.example.caesarzonapplication.viewmodels.AdminViewModels

import androidx.compose.runtime.mutableStateListOf
import com.example.caesarzonapplication.model.dto.UserFindDTO
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.io.IOException
import java.net.URL

class SearchUsersViewModel {
    val client = OkHttpClient()

    private val _searchResults = mutableStateListOf<UserFindDTO>()
    val searchResults: List<UserFindDTO> get() = _searchResults

    init{
        searchUsers()
    }

    fun searchUsers() {
        CoroutineScope(Dispatchers.IO).launch {
            val manageURL = URL("http://25.49.50.144:8090/user-api/users?str=0");
            val request = Request.Builder().url(manageURL)
                .addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()
            try {
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    return@launch
                }
                val responseBody = response.body?.string()
                val jsonResponse = JSONArray(responseBody)
                _searchResults.clear()
                for (i in 0 until jsonResponse.length()) {
                    val username = jsonResponse.getJSONObject(i).getString("username")
                    val profilePictureBase64 =
                        jsonResponse.getJSONObject(i).optString("profilePicture", "")
                    _searchResults.add(UserFindDTO(username, profilePictureBase64))
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun searchSpecifcUsers(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            println("Chiamo la specifc User")
            var username = ""
            val manageURLUsername = URL("http://25.49.50.144:8090/user-api/users/$query")
            val request = Request.Builder().url(manageURLUsername)
                .addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()
            try {
                val response = client.newCall(request).execute()
                print("response: "+response.message)
                if (!response.isSuccessful) {
                    return@launch
                }

                val responseBody = response.body?.string()

                try {
                    val jsonResponse = JSONArray(responseBody)
                    _searchResults.clear()
                    for (i in 0 until jsonResponse.length()) {
                        username = jsonResponse.getString(i)
                        println("username: "+username)
                        val manageURLProfilePic =
                            URL("http://25.49.50.144:8090/user-api/image/$username")
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
                        _searchResults.add(UserFindDTO(username, profilePictureBase64))
                    }
                    for(search in _searchResults){
                        println(search.username+" "+search.safeImageUrl)
                    }
                } catch (e: org.json.JSONException) {
                    println("Errore nel parsing del JSON: ${e.message}")
                }


            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}