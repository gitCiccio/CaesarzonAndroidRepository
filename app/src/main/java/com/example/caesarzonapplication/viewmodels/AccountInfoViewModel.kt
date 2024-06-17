package com.example.caesarzonapplication.viewmodels

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.lifecycle.ViewModel
import com.example.caesarzonapplication.model.service.KeycloakService
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

data class AccountInfoData (
    val name: String = "",
    val surname: String = "",
    val username: String = "",
    val email: String = "",
    val profileImageBase64: String? = null // Aggiungiamo qui l'immagine del profilo
)

data class TokenPayload(
    @SerializedName("name") val name: String,
    @SerializedName("surname") val surname: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String
)

class AccountInfoViewModel : ViewModel() {

    private val _accountInfoData = MutableStateFlow(AccountInfoData())
    val accountInfoData: StateFlow<AccountInfoData> get() = _accountInfoData.asStateFlow()

    private fun decodeJWT(jwt: String): TokenPayload {
        return try {
            val parts = jwt.split(".")
            if (parts.size == 3) {
                val payload = String(java.util.Base64.getUrlDecoder().decode(parts[1]))
                Gson().fromJson(payload, TokenPayload::class.java)
            } else {
                TokenPayload("", "", "", "")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            TokenPayload("", "", "", "")
        }
    }

    fun getUserData() {
        println("sono nel getUserData")
        val tokenPayload = decodeJWT(KeycloakService.myToken.toString())

        val manageURL = URL("http://25.49.50.144:8090/user-api/user")
        val connection = manageURL.openConnection() as HttpURLConnection

        try {
            connection.requestMethod = "GET"
            connection.setRequestProperty("Authorization", "Bearer " + KeycloakService.myToken?.accessToken)
            println("Token Payload: ${KeycloakService.myToken?.accessToken}")
            val responseCode = connection.responseCode
            println("Response Code: $responseCode")

            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?

                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                println("Response Code: $responseCode")
                println("Response Body: $response")

                _accountInfoData.value =
                    AccountInfoData(
                        name = tokenPayload.name,
                        surname = tokenPayload.surname,
                        username = tokenPayload.username,
                        email = tokenPayload.email,
                        profileImageBase64 = _accountInfoData.value.profileImageBase64 // Manteniamo l'immagine attuale
                    )
                println("Account Info: ${_accountInfoData.value.name}")
            } else {
                println("Error: ${connection.responseMessage}")
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection.disconnect()
        }
    }

    fun updateProfileImage(bitmap: Bitmap) {
        val base64 = bitmap.toBase64()
        _accountInfoData.value = _accountInfoData.value.copy(profileImageBase64 = base64)
    }

    private fun Bitmap.toBase64(): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun loadProfileImageFromBase64(base64: String): Bitmap {
        val decodedByteArray = Base64.decode(base64, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.size)
    }
}
