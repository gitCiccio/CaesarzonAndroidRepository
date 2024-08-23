package com.example.caesarzonapplication.model.viewmodels

import android.graphics.Bitmap
import android.media.Image
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.caesarzonapplication.model.dto.PasswordChangeDTO
import com.example.caesarzonapplication.model.dto.UserDTO
import com.example.caesarzonapplication.model.dto.UserRegistrationDTO
import com.example.caesarzonapplication.model.entities.userEntity.ProfileImage
import com.example.caesarzonapplication.model.entities.userEntity.User
import com.example.caesarzonapplication.model.repository.userRepository.AddressRepository
import com.example.caesarzonapplication.model.repository.userRepository.CardRepository
import com.example.caesarzonapplication.model.repository.userRepository.ProfileImageRepository
import com.example.caesarzonapplication.model.repository.userRepository.UserRepository
import com.example.caesarzonapplication.model.service.KeycloakService
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.logged
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.net.URL


class AccountInfoViewModel(private val userRepository: UserRepository, private val cardRepository: CardRepository, private val addressRepository: AddressRepository,
    private val profileImageRepository: ProfileImageRepository) : ViewModel() {

    var userData: UserDTO? = null

     var profileImage: Bitmap? = null

    private val client = OkHttpClient()


    fun loadUSerData(username: String) {
        viewModelScope.launch {
            try {
                userData = userRepository.getUserData(username)
                cardRepository.getAllCards()
                addressRepository.getAllAddresses()
                //profileImage = profileImageRepository.getProfileImage()
            } catch (e: Exception) {
                // Gestisci eccezioni come desideri
                Log.e("AccountInfoViewModel", "Errore nel caricamento dei dati utente", e)
            }
        }
    }


    fun addUserData(user: UserRegistrationDTO){
        viewModelScope.launch {
            userRepository.addUser(user)
        }
    }

    // Metodo per salvare l'immagine nel database (da implementare)
    private suspend fun saveProfileImageToDatabase(bitmap: Bitmap) {
        // Implementa la logica per salvare l'immagine nel tuo database
        // Esempio di implementazione: salva `bitmap` come ByteArray
        // Utilizza una libreria o un approccio adatto al tuo database
    }


    //Fase di modifica dei dati
    fun modifyUserData(
        firstName: String,
        lastName: String,
        username: String,
        email: String,
        phoneNumber: String,
        callback: (result: String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val result = doModifyUser(username, firstName, lastName, phoneNumber, email)
                callback(result)
            } catch (e: Exception) {
                e.printStackTrace()
                callback("error: ${e.message}")
            }
        }
    }


    suspend fun doModifyUser(
        firstName: String,
        lastName: String,
        username: String,
        email: String,
        phoneNumber: String,
    ): String {
        val newUserDTO = UserDTO(username, firstName, lastName, phoneNumber, email)
        val jsonObject = JSONObject()
            .put("firstName", newUserDTO.firstName)
            .put("lastName", newUserDTO.lastName)
            .put("username", newUserDTO.username)
            .put("email", newUserDTO.email)
            .put("phoneNumber", newUserDTO.phoneNumber)

        val json = jsonObject.toString()
        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = json.toRequestBody(mediaType)

        val manageURL = URL("http://25.49.50.144:8090/user-api/user")
        val request = Request.Builder()
            .url(manageURL)
            .put(requestBody)
            .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
            .build()

        return withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    return@withContext "error: ${response.message} ${response.code}"
                }
                "success"
            } catch (e: IOException) {
                e.printStackTrace()
                "error: ${e.message}"
            }
        }
    }
    //Fine modifica dei dati

    //Fase di registrazione
    fun registerUser(username: String,firstName: String, lastName: String, email: String, credentialValue: String,callback: (result: String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                doRegistration(username,firstName, lastName, email, credentialValue)
                val user = UserRegistrationDTO(firstName = firstName, lastName =  lastName, username =  username, email =  email,credentialValue = credentialValue)
                addUserData(user)
                callback("success")
            } catch (e: Exception) {
                e.printStackTrace()
                callback("error: ${e.message}")
            }
        }
    }


    private suspend fun doRegistration(
        firstName: String,
        lastName: String,
        username: String,
        email: String,
        password: String,
    ) {
        withContext(Dispatchers.IO) {
            val newUserDTO = UserRegistrationDTO(firstName, lastName, username, email, password)
            val jsonObject = JSONObject()
                .put("username", newUserDTO.username)
                .put("email", newUserDTO.email)
                .put("firstName", newUserDTO.firstName)
                .put("lastName", newUserDTO.lastName)
                .put("credentialValue", newUserDTO.credentialValue)

            val json = jsonObject.toString()

            val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
            val requestBody = json.toRequestBody(mediaType)

            val manageURL = URL("http://25.49.50.144:8090/user-api/user")
            val request = Request.Builder()
                .url(manageURL)
                .post(requestBody)
                .addHeader("Authorization", "Bearer ${KeycloakService.basicToken?.accessToken}")
                .build()

            val client = OkHttpClient()
            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                throw IOException("Errore durante la registrazione: ${response.message} ${response.code}")
            }
        }
    }
    //Fine fase di registrazione



    //Inizio recupero password
    fun retrieveForgottenPassword(username: String, callback: (result: String) -> Unit) {
        viewModelScope.launch {
            try {
                val result = doRetrieveForgottenPassword(username)
                callback(result)
            } catch (e: Exception) {
                e.printStackTrace()
                callback("error: ${e.message}")
            }
        }
    }


    suspend fun doRetrieveForgottenPassword(username: String): String {
        val passwordChangeDTO = PasswordChangeDTO("", username)
        val jsonObject = JSONObject().put("username", passwordChangeDTO.username)

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
            try {
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    return@withContext "error: ${response.message} ${response.code}"
                }
                "success"
            } catch (e: IOException) {
                e.printStackTrace()
                "error: ${e.message}"
            }
        }
    }
    //Fine recupero password

    //verifica otp

    fun verifyOTP(otp: String, password: String, username: String, callback: (result: String) -> Unit){
        viewModelScope.launch {
            try{
                doVerifyOTP(otp, password, username)
                callback("success")
            }catch (e: Exception){
                e.printStackTrace()
                callback("error: ${e.message}")
            }
        }
    }

    suspend fun doVerifyOTP(otp: String, password: String, username: String): String {
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

    fun changePassword(password: String, username: String, callback: (result: String) -> Unit): String {
        viewModelScope.launch {
            if(doChangePassword(password, username) == "success")
                callback("success")
        }
        return "error"
    }

    suspend fun doChangePassword(password: String, username: String): String {
        val passwordChangeDTO = PasswordChangeDTO(password, username)
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

    suspend fun getUserData(): String {
        return withContext(Dispatchers.IO) {
            try {
                val manageURL = URL("http://25.49.50.144:8090/user-api/user")
                val request = Request.Builder()
                    .url(manageURL)
                    .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
                    .build()

                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    return@withContext "failed"
                }

                val responseBody = response.body?.string()
                val jsonObject = JSONObject(responseBody)

                val firstName = jsonObject.optString("firstName", "")
                val lastName = jsonObject.optString("lastName", "")
                val username = jsonObject.optString("username", "")
                val email = jsonObject.optString("email", "")
                val phoneNumber = jsonObject.optString("phoneNumber", "")

                val userDTO = UserRegistrationDTO(username, firstName, lastName, phoneNumber, email)
                userRepository.addUser(userDTO)

                "success"
            } catch (e: IOException) {
                e.printStackTrace()
                "error"
            }
        }
    }



    suspend fun login(username: String, password: String): Boolean {
        // Resetta il token all'inizio del login
        myToken = null

        // Attende la risposta della chiamata per ottenere il token
        val result = withContext(Dispatchers.IO) {
            KeycloakService().getAccessToken(username, password)
        }

        return if (result != null && result.accessToken.isNotBlank()) {
            getUserData()
            logged.value = true
            true
        } else {
            println("Errore di login: credenziali non valide.")
            false
        }
    }

}

class AccountInfoViewModelFactory(
    private val userRepository: UserRepository,
    private val cardRepository: CardRepository,
    private val addressRepository: AddressRepository,
    private val profileImageRepository: ProfileImageRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountInfoViewModel::class.java)) {
            return AccountInfoViewModel(userRepository, cardRepository, addressRepository, profileImageRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
