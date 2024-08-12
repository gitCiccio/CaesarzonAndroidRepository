package com.example.caesarzonapplication.model.service

import com.example.caesarzonapplication.model.TokenResponse
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
    }

    val client = OkHttpClient()


    fun getAccessToken(username: String, password: String): TokenResponse? {
        val url =
            URL("http://25.24.244.170:8080/realms/CaesarRealm/protocol/openid-connect/token")
        val connection = url.openConnection() as HttpURLConnection

        connection.requestMethod = "POST"
        connection.doOutput = true
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

        val postData =
            "username=${URLEncoder.encode(username, "UTF-8")}" +
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

        val reader = BufferedReader(
            InputStreamReader(inputStream)
        )

        val response = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            response.append(line)
        }
        reader.close()
        connection.disconnect()
        val gson = Gson()
        myToken = gson.fromJson(response.toString(), TokenResponse::class.java)
        return myToken
    }

    fun getBasicToken() {
        CoroutineScope(Dispatchers.IO).launch {
            val manageUrl =
                "http://25.24.244.170:8080/realms/CaesarRealm/protocol/openid-connect/token"
            val passwordGuest = "CiaoSonoguest69!"

            // Encode the parameters
            val params = listOf(
                "client_id" to "caesar-app",
                "grant_type" to "password",
                "username" to "guest",
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
}
