package com.example.caesarzonapplication.model.viewmodels

import androidx.lifecycle.ViewModel
import com.example.caesarzonapplication.model.dto.PasswordChangeDTO
import com.example.caesarzonapplication.model.service.KeycloakService
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.net.URL

class AuthViewModel: ViewModel() {

    private val client = OkHttpClient()

    suspend fun retrieveForgottenPassword(username: String): String {
        val passwordChangeDTO = PasswordChangeDTO("", username)
        val jsonObject = JSONObject()
            .put("username", passwordChangeDTO.username)

        val json = jsonObject.toString()
        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = json.toRequestBody(mediaType)

        val manageURL = URL("http://25.49.50.144:8090/user-api/password?recovery=true")
        val request = Request.Builder()
            .url(manageURL)
            .put(requestBody)
            .addHeader("Authorization", "Bearer ${KeycloakService.basicToken?.accessToken}")
            .build()

        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            try {
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    return@withContext "${response.message} ${response.code}"
                }
                "success"
            } catch (e: IOException) {
                e.printStackTrace()
                "error"
            }
        }
    }

    suspend fun verifyOTP(otp: String, password: String, username: String): String {
        val passwordChangeDTO = PasswordChangeDTO(password, username)
        val body = JSONObject().apply {
            put("username", passwordChangeDTO.username)
        }
        val json = body.toString()
        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = json.toRequestBody(mediaType)
        val manageURL = URL("http://25.49.50.144:8090/user-api/otp/$otp")
        val request = Request.Builder()
            .url(manageURL)
            .post(requestBody)
            .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
            .build()

        return withContext(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    return@withContext "${response.message} ${response.code}"
                }
                "success"
            } catch (e: IOException) {
                e.printStackTrace()
                "error"
            }
        }
    }
}