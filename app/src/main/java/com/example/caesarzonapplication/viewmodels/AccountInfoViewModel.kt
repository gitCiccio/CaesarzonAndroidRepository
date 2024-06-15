package com.example.caesarzonapplication.viewmodels

import androidx.lifecycle.ViewModel
import com.example.caesarzonapplication.model.service.KeycloakService
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.Base64

data class AccountInfoData (
    val name: String="",
    val surname: String="",
    val username: String="",
    val email: String=""
)

data class TokenPayload(
    @SerializedName("name") val name: String,
    @SerializedName("surname") val surname: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String
)

class AccountInfoViewModel : ViewModel(){

    private val _accountInfoData = MutableStateFlow(AccountInfoData())
    val accountInfoData: StateFlow<AccountInfoData> get() = _accountInfoData.asStateFlow()

    init {
        getUserData()
    }

    private fun decodeJWT(jwt: String): TokenPayload {
        return try {
            val parts = jwt.split(".")
            if (parts.size == 3) {
                val payload = String(Base64.getUrlDecoder().decode(parts[1]))
                Gson().fromJson(payload, TokenPayload::class.java)
            } else{
                TokenPayload("", "", "", "")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            TokenPayload("", "", "", "")
        }
    }


    fun getUserData() {

        val tokenPayload = decodeJWT(KeycloakService.myToken.toString())

        _accountInfoData.value = AccountInfoData(
            name = tokenPayload.name,
            surname = tokenPayload.surname,
            username = tokenPayload.username,
            email = tokenPayload.email
        )

        val manageURL = URL("http://25.49.50.144:8090/user-api/user")
        val connection = manageURL.openConnection() as HttpURLConnection

        try{
            connection.requestMethod = "GET"
            connection.setRequestProperty("Authorization", "Bearer "+ KeycloakService.myToken)
            val responseCode = connection.responseCode
            println("Response Code: $responseCode")

            if(responseCode == HttpURLConnection.HTTP_OK){
                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?

                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                println("Response Code: $responseCode")
                println("Response Body: $response")

            }
            else
            {
                println("Error: ${connection.responseMessage}")
            }
        }
        catch (e: Exception){
            e.printStackTrace()
        }
        finally {
            connection.disconnect()
        }
    }
}