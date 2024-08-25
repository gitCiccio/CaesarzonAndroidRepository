package com.example.caesarzonapplication.model.viewmodels.userViewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.caesarzonapplication.model.dto.PasswordChangeDTO
import com.example.caesarzonapplication.model.dto.UserDTO
import com.example.caesarzonapplication.model.dto.UserRegistrationDTO
import com.example.caesarzonapplication.model.entities.userEntity.ProfileImage
import com.example.caesarzonapplication.model.repository.userRepository.ProfileImageRepository
import com.example.caesarzonapplication.model.repository.userRepository.UserRepository
import com.example.caesarzonapplication.model.service.KeycloakService
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.logged
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import com.example.caesarzonapplication.model.utils.BitmapConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.URL


class AccountInfoViewModel(private val userRepository: UserRepository, private val imageRepository: ProfileImageRepository) : ViewModel() {

    var userData: UserDTO? = null

    //Carico l'immagine profilo
    var profileImage: LiveData<ProfileImage?> = imageRepository.getProfileImage()

    private val client = OkHttpClient()


    fun addUserData(user: UserRegistrationDTO) {
        viewModelScope.launch {
            userRepository.addUser(user)
        }
    }


    //Fase di modifica dei dati, funziona

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
                val result = doModifyUser(firstName, lastName, username, email, phoneNumber)
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
        val newUserDTO = UserDTO(username, firstName, lastName, email, phoneNumber)
        val jsonObject = JSONObject()
            .put("username", newUserDTO.username)
            .put("firstName", newUserDTO.firstName)
            .put("lastName", newUserDTO.lastName)
            .put("username", newUserDTO.username)
            .put("phoneNumber", newUserDTO.phoneNumber)
            .put("email", newUserDTO.email)


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
                val oldUser = userRepository.getUserData(username)
                oldUser.firstName = newUserDTO.firstName
                oldUser.lastName = newUserDTO.lastName
                oldUser.email = newUserDTO.email
                oldUser.phoneNumber = newUserDTO.phoneNumber
                userRepository.updateUser(oldUser)
                "success"
            } catch (e: IOException) {
                e.printStackTrace()
                "error: ${e.message}"
            }
        }
    }

    //Fase di registrazione funziona
    fun registerUser(
        username: String,
        firstName: String,
        lastName: String,
        email: String,
        credentialValue: String,
        callback: (result: String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                doRegistration(username, firstName, lastName, email, credentialValue)
                val user = UserRegistrationDTO(
                    firstName = firstName,
                    lastName = lastName,
                    username = username,
                    email = email,
                    credentialValue = credentialValue
                )
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
                .put("firstName", newUserDTO.firstName)
                .put("email", newUserDTO.email)
                .put("username", newUserDTO.username)
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

    fun verifyOTP(
        otp: String,
        password: String,
        username: String,
        callback: (result: String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                doVerifyOTP(otp, password, username)
                callback("success")
            } catch (e: Exception) {
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

    //con recovy 1 tutto ok
    //con recovery 0 invio otp
    //con recovery 2 devo aspettare aldo
    fun changePassword(
        password: String,
        username: String,
        recovery: Int,
        callback: (result: String) -> Unit
    ): String {
        viewModelScope.launch {
            if (doChangePassword(password, username, recovery) == "success")
                callback("success")
        }
        return "error"
    }

    suspend fun doChangePassword(password: String, username: String, recovery: Int): String {
        val passwordChangeDTO = PasswordChangeDTO(password, username)
        val jsonObject = JSONObject()
            .put("password", passwordChangeDTO.password)

        val json = jsonObject.toString()
        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = json.toRequestBody(mediaType)

        val manageURL = URL("http://25.49.50.144:8090/user-api/password?recovery=$recovery")
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

    //Funziona
    fun getUserData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                println("sono nel try del get data")
                val manageURL = URL("http://25.49.50.144:8090/user-api/user")
                val request = Request.Builder()
                    .url(manageURL)
                    .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                    return@launch
                }

                println("Risposta dal server: $responseBody")

                val jsonObject = JSONObject(responseBody)

                val firstName = jsonObject.optString("firstName", "")
                val lastName = jsonObject.optString("lastName", "")
                val username = jsonObject.optString("username", "")
                val email = jsonObject.optString("email", "")
                val phoneNumber = jsonObject.optString("phoneNumber", "")

                userData = UserDTO(username, firstName, lastName, phoneNumber, email)
                addUserData(UserRegistrationDTO(firstName, lastName, username, email, ""))
                println("Dati utente recuperati con successo: ${userData?.username}")
            } catch (e: IOException) {
                e.printStackTrace()
                println("Errore di rete o I/O: ${e.message}")
            } catch (e: JSONException) {
                e.printStackTrace()
                println("Errore nel parsing della risposta JSON: ${e.message}")
            }
        }
    }


    //funziona
    suspend fun login(username: String, password: String): Boolean {
        // Resetta il token all'inizio del login
        myToken = null

        // Attende la risposta della chiamata per ottenere il token
        KeycloakService().getAccessToken(username, password)

        println("$myToken")
        return if (myToken?.accessToken != null) {
            getUserData() // Questa dovrebbe anche essere una funzione sospesa se fa operazioni di rete
            logged.value = true
            true
        } else {
            println("Errore di login: credenziali non valide.")
            false
        }
    }

    //Caricamento immagine profilo
    fun updateImageProfile(image: Bitmap) {
        viewModelScope.launch {
            imageRepository.insertProfileImage(
                ProfileImage(
                    username = userData!!.username,
                    profilePicture = image
                )
            )
        }
    }

}
class AccountInfoViewModelFactory(
    private val userRepository: UserRepository,
    private val imageRepository: ProfileImageRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountInfoViewModel::class.java)) {
            return AccountInfoViewModel(userRepository, imageRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
