package com.example.caesarzonapplication.viewmodels

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caesarzonapplication.model.User
import com.example.caesarzonapplication.model.service.KeycloakService
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

    private val _searchResults = MutableStateFlow<List<User>>(emptyList())
    val searchResults: StateFlow<List<User>> get() = _searchResults

    // StateFlow per l'immagine del profilo
    private val _profileImage = MutableStateFlow<Bitmap?>(null)
    val profileImage = _profileImage

    // Metodo per impostare l'immagine del profilo
    fun setProfileImage(bitmap: Bitmap) {
        viewModelScope.launch {
            // Salva l'immagine nel database (da implementare)
            saveProfileImageToDatabase(bitmap)

            // Aggiorna lo stato dell'immagine nel ViewModel
            _profileImage.value = bitmap
        }
    }

    // Metodo per caricare l'immagine del profilo dal database
    fun loadProfileImageFromDatabase() {
        viewModelScope.launch {
            val byteArray = loadProfileImageByteArrayFromDatabase()
            byteArray?.let {
                _profileImage.value = byteArrayToBitmap(it)
            }
        }
    }

    // Metodo per salvare l'immagine nel database (da implementare)
    private suspend fun saveProfileImageToDatabase(bitmap: Bitmap) {
        // Implementa la logica per salvare l'immagine nel tuo database
        // Esempio di implementazione: salva `bitmap` come ByteArray
        // Utilizza una libreria o un approccio adatto al tuo database
    }

    // Metodo per caricare il ByteArray dell'immagine dal database (da implementare)
    private suspend fun loadProfileImageByteArrayFromDatabase(): ByteArray? {
        // Implementa la logica per caricare il ByteArray dell'immagine dal tuo database
        // Esempio di implementazione: carica il ByteArray salvato precedentemente
        return null // Sostituisci con la tua logica reale di caricamento
    }

    // Metodo di utilità per convertire ByteArray in Bitmap
    private fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }


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
                        email = tokenPayload.email,)
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

    fun searchUsers(query: String) {
        viewModelScope.launch {
            try {
                val users = KeycloakService.searchUsers(query)
                _searchResults.value = users
            } catch (e: Exception) {
                e.printStackTrace()
                _searchResults.value = emptyList()
            }
        }
    }
}
