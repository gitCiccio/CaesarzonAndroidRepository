package com.example.caesarzonapplication.model.service

import com.example.caesarzonapplication.model.TokenResponse
import com.example.caesarzonapplication.model.User
import com.example.caesarzonapplication.model.dto.UserDTO
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class KeycloakService {

    companion object {
        var myToken: TokenResponse? = null

        suspend fun searchUsers(query: String = ""): List<UserDTO> {
            return withContext(Dispatchers.IO) {
                val users = mutableListOf<UserDTO>()
                val url = URL("http://25.49.50.144:8090/user-api/users")
                val connection = url.openConnection() as HttpURLConnection

                try {
                    connection.requestMethod = "GET"
                    connection.setRequestProperty("Authorization", "Bearer " + myToken?.accessToken)
                    println("Token Payload: ${myToken?.accessToken}")
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val response = StringBuilder()
                    var line: String?

                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    reader.close()

                    println("Response code: ${connection.responseCode}")
                    if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                        val jsonResponse = JSONArray(response.toString())
                        println("Response body: $jsonResponse")
                        for (i in 0 until jsonResponse.length()) {
                            val jsonObject = jsonResponse.getJSONObject(i)
                            val username = jsonObject.getString("username")
                            val firstName = jsonObject.getString("firstName")
                            val lastName = jsonObject.getString("lastName")
                            val phoneNumber = jsonObject.getString("phoneNumber")
                            val email = jsonObject.getString("email")

                            val user = UserDTO(
                                username,
                                firstName,
                                lastName,
                                phoneNumber,
                                email
                            )
                            users.add(user)
                        }
                    } else {
                        println("Error: ${connection.responseMessage}")
                        // Gestire l'errore, ad esempio lanciando un'eccezione personalizzata
                        throw IOException("HTTP error code: ${connection.responseCode}")
                    }
                } catch (e: IOException) {
                    println("Exception: ${e.message}")
                    // Propagare l'eccezione per gestirla nell'ViewModel
                    throw e
                } finally {
                    connection.disconnect()
                }
                return@withContext users
            }
        }
    }


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
    }
