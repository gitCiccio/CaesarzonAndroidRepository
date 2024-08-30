package com.example.caesarzonapplication.model.service

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT
import com.example.caesarzonapplication.model.TokenResponse
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


class KeycloakService {

    companion object {
        var myToken: TokenResponse? = null
        var basicToken: TokenResponse? = null
        var isAdmin = mutableStateOf(false)
        var logged = mutableStateOf(false)
        var globalUsername = mutableStateOf("")
    }

    val client = OkHttpClient()
    private val keycloakRealm = "CaesarRealm"
    private val keycloakUrl = "http://25.24.244.170:8080"

    suspend fun getAccessToken(username: String, password: String) {
        withContext(Dispatchers.IO) {
            try {
                val url = URL("http://25.24.244.170:8080/realms/CaesarRealm/protocol/openid-connect/token")
                val connection = url.openConnection() as HttpURLConnection

                connection.requestMethod = "POST"
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

                val postData = "username=${URLEncoder.encode(username, "UTF-8")}" +
                        "&password=${URLEncoder.encode(password, "UTF-8")}" +
                        "&grant_type=password" +
                        "&client_id=caesar-app"
                val outputStream = OutputStreamWriter(connection.outputStream)
                outputStream.write(postData)
                outputStream.flush()

                val responseCode = connection.responseCode
                val inputStream: InputStream = if (responseCode == HttpURLConnection.HTTP_OK) {
                    connection.inputStream
                } else {
                    connection.errorStream
                }

                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                reader.forEachLine { line -> response.append(line) }
                reader.close()
                connection.disconnect()

                val gson = Gson()
                globalUsername = mutableStateOf(username)
                myToken = gson.fromJson(response.toString(), TokenResponse::class.java)
                myToken?.accessToken?.let {
                    decodeTokenMio(it)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }



    fun getBasicToken() {
        println("Sono nella funzione per prendere il basic token")
        CoroutineScope(Dispatchers.IO).launch {
            val manageUrl =
                "http://25.24.244.170:8080/realms/CaesarRealm/protocol/openid-connect/token"
            val passwordGuest = "Mascalzone1"

            // Encode the parameters
            val params = listOf(
                "client_id" to "caesar-app",
                "grant_type" to "password",
                "username" to "Guest",
                "password" to passwordGuest
            ).joinToString("&") { (key, value) ->
                "${URLEncoder.encode(key, StandardCharsets.UTF_8.name())}=${
                    URLEncoder.encode(
                        value,
                        StandardCharsets.UTF_8.name()
                    )
                }"
            }
            val requestBody =
                params.toRequestBody("application/x-www-form-urlencoded".toMediaType())
            val request = Request.Builder()
                .url(manageUrl)
                .post(requestBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build()
            try {
                val response = client.newCall(request).execute()
                println("Response code: ${response.code}")
                println("Response message: ${response.message}")

                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    println("Response body: $responseBody")

                    if (responseBody != null) {
                        val json = JSONObject(responseBody)
                        val accessToken = json.getString("access_token")
                        val refreshToken = json.getString("refresh_token")
                        basicToken = TokenResponse(accessToken, refreshToken)
                        // Decode the basic token if needed
                        basicToken?.accessToken?.let {
                            decodeTokenMio(it)
                        }
                    } else {
                        println("Null response body")
                    }
                } else {
                    println("Error in the request: ${response.body?.string()}")
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    fun decodeTokenMio(accessToken: String) {
        try {
            val decodedJWT: DecodedJWT = JWT.decode(accessToken)
            val roles = mutableListOf<String>()

            decodedJWT.getClaim("realm_access")?.asMap()?.get("roles")?.let { roles.addAll(it as List<String>) }
            decodedJWT.getClaim("resource_access")?.asMap()?.get("caesar-app")?.let { resourceAccess ->
                (resourceAccess as Map<*, *>)["roles"]?.let { roles.addAll(it as List<String>) }
            }

            // Check if the roles contain 'admin' and update isAdmin accordingly
            isAdmin.value = roles.contains("admin")
        } catch (e: Exception) {
            e.printStackTrace()
            // Ensure isAdmin is false if there's an error decoding the token
            isAdmin.value = false
        }
    }



}
