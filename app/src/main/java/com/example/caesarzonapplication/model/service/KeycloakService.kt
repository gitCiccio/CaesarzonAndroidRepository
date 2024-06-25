package com.example.caesarzonapplication.model.service

import com.example.caesarzonapplication.model.TokenResponse
import com.example.caesarzonapplication.model.User
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class KeycloakService {

    companion object {
        var myToken: TokenResponse? = null

        fun searchUsers(query: String): List<User> {
            // Utenti fittizi
            val dummyUsers = listOf(
                User("1", "John", "Doe", "john_doe", "1234567890", "john.doe@example.com", isFollower = true, isFriend = false),
                User("2", "Jane", "Smith", "jane_smith", "0987654321", "jane.smith@example.com", isFollower = false, isFriend = true),
                User("3", "Emily", "Johnson", "emily_johnson", "1122334455", "emily.johnson@example.com", isFollower = true, isFriend = true),
                User("4", "Michael", "Brown", "michael_brown", "6677889900", "michael.brown@example.com", isFollower = false, isFriend = false),
                User("5", "Sarah", "Williams", "sarah_williams", "5566778899", "sarah.williams@example.com", isFollower = true, isFriend = true)
            )

            return dummyUsers.filter { it.username.contains(query, ignoreCase = true) }
        }


        /*
        fun searchUsers(query: String): List<User> {
            val url = URL("http://25.49.50.144:8090/user-api/search?query=$query")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Authorization", "Bearer " + myToken?.accessToken)

            return try {
                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val response = reader.readText()
                    reader.close()
                    Gson().fromJson(response, Array<User>::class.java).toList()
                } else {
                    emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            } finally {
                connection.disconnect()
            }
        }
         */
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
