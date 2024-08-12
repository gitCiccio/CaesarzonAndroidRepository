package com.example.caesarzonapplication.model.viewmodels

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caesarzonapplication.model.dto.PasswordChangeDTO
import com.example.caesarzonapplication.model.dto.UserDTO
import com.example.caesarzonapplication.model.dto.UserRegistrationDTO
import com.example.caesarzonapplication.model.service.KeycloakService
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import java.io.IOException
import java.net.URL


class AccountInfoViewModel : ViewModel() {

    object UserData{
            private var _accountInfoData = MutableStateFlow(UserDTO("", "", "", "", "", ""))
            var accountInfoData: StateFlow<UserDTO> = _accountInfoData

            fun updateUserData(newUserData: UserDTO){
                _accountInfoData.value = newUserData
            }
        }

    // StateFlow per l'immagine del profilo
    private val _profileImage = MutableStateFlow<Bitmap?>(null)
    val profileImage = _profileImage
    private val client = OkHttpClient()

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

    suspend fun changePassword(password: String): String {
        val passwordChangeDTO = PasswordChangeDTO(password, "")
        val jsonObject = JSONObject()
            .put("password", passwordChangeDTO.password)

        val json = jsonObject.toString()
        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = json.toRequestBody(mediaType)

        val manageURL = URL("http://25.49.50.144:8090/user-api/password?recovery=false")
        val request = Request.Builder()
            .url(manageURL)
            .put(requestBody)
            .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
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
        val manageURL = URL("http://25.49.50.144:8090/user-api/otp/"+otp)
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

    suspend fun modifyUser(id: String, firstName: String, lastName: String, username: String, email: String, phoneNumber: String): String {
        val newUserDTO = UserDTO( id, username, firstName, lastName, phoneNumber,email )
        val jsonObject = JSONObject()
            .put("firstName", newUserDTO.firstName)
            .put("lastName", newUserDTO.lastName)
            .put("username", newUserDTO.username)
            .put("email", newUserDTO.email)
            .put("phoneNumber", newUserDTO.phoneNumber)

        val json = jsonObject.toString()

        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = json.toRequestBody(mediaType)

        val manageURL = URL("http://25.49.50.144:8090/user-api/user");
        val request = Request.Builder().url(manageURL).put(requestBody).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()
        return withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    return@withContext response.message + " " + response.code
                }
                "success"
            } catch (e: IOException) {
                e.printStackTrace()
                "error"
            }
        }
    }

    suspend fun registerUser(firstName: String, lastName: String, username: String, email: String, credentialValue: String): String {
        val newUserDTO = UserRegistrationDTO( firstName, lastName, username, email, credentialValue)
        val jsonObject = JSONObject()
            .put("firstName", newUserDTO.firstName)
            .put("lastName", newUserDTO.lastName)
            .put("username", newUserDTO.username)
            .put("email", newUserDTO.email)
            .put("credentialValue", newUserDTO.credentialValue)

        val json = jsonObject.toString()

        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = json.toRequestBody(mediaType)

        val manageURL = URL("http://25.49.50.144:8090/user-api/user");
        val request = Request.Builder().url(manageURL).post(requestBody).addHeader("Authorization", "Bearer ${KeycloakService.basicToken?.accessToken}").build()
        return withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    return@withContext response.message + " " + response.code
                }
                "success"
            } catch (e: IOException) {
                e.printStackTrace()
                "error"
            }
        }
    }

    suspend fun getUserData(): String {
        val manageURL = URL("http://25.49.50.144:8090/user-api/user");
        val request = Request.Builder().url(manageURL).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()
        return withContext(Dispatchers.IO)  {
            try {
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    return@withContext response.message
                }
                val responseBody = response.body?.string()
                val jsonObject = JSONObject(responseBody)

                val id = jsonObject.optString("id", "")
                val firstName = jsonObject.optString("firstName", "")
                val lastName = jsonObject.optString("lastName", "")
                val username = jsonObject.optString("username", "")
                val email = jsonObject.optString("email", "")
                val phoneNumber = jsonObject.optString("phoneNumber", "")

                val userDTO = UserDTO(
                    id = id,
                    firstName = firstName,
                    lastName = lastName,
                    username = username,
                    email = email,
                    phoneNumber = phoneNumber
                )
                UserData.updateUserData(userDTO)
                "success"
            } catch (e: IOException) {
                e.printStackTrace()
                "error"
            }
        }
    }
}

