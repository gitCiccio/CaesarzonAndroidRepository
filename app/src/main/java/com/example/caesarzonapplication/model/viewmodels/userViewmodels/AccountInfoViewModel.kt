package com.example.caesarzonapplication.model.viewmodels.userViewmodels

import android.graphics.Bitmap
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.caesarzonapplication.model.dto.authDTOS.PasswordChangeDTO
import com.example.caesarzonapplication.model.dto.userDTOS.UserDTO
import com.example.caesarzonapplication.model.dto.authDTOS.UserRegistrationDTO
import com.example.caesarzonapplication.model.dto.userDTOS.LogoutDTO
import com.example.caesarzonapplication.model.entities.userEntity.ProfileImage
import com.example.caesarzonapplication.model.repository.userRepository.ProfileImageRepository
import com.example.caesarzonapplication.model.repository.userRepository.UserRepository
import com.example.caesarzonapplication.model.service.KeycloakService
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.basicToken
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.isAdmin
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.logged
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
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

    companion object {
        var userData: UserDTO? = null
        var userDataForAdmin: UserDTO? = null
    }

    var profileImage: LiveData<ProfileImage?> = imageRepository.getProfileImage()

    private val client = OkHttpClient()

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading


    fun addUserData(user: UserRegistrationDTO) {
        viewModelScope.launch {
            userRepository.addUser(user)
        }
    }

    fun modifyUserData(
        firstName: String,
        lastName: String,
        username: String,
        email: String,
        phoneNumber: String,
        callback: (result: String) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = doModifyUser(firstName, lastName, username, email, phoneNumber)
                callback(result)
            } catch (e: Exception) {
                e.printStackTrace()
                callback("error: ${e.message}")
            }
            _isLoading.value = false
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
                "success"
            } catch (e: IOException) {
                e.printStackTrace()
                "error: ${e.message}"
            }
        }
    }

    fun registerUser(
        username: String,
        firstName: String,
        lastName: String,
        email: String,
        credentialValue: String,
        callback: (result: String) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
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
            _isLoading.value = false
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

    fun retrieveForgottenPassword(username: String, callback: (result: String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = doRetrieveForgottenPassword(username)
                callback(result)
            } catch (e: Exception) {
                e.printStackTrace()
                callback("error: ${e.message}")
            }
            _isLoading.value = false
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

    fun verifyOTP(
        otp: String,
        password: String,
        username: String,
        callback: (result: String) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                doVerifyOTP(otp, password, username)
                callback("success")
            } catch (e: Exception) {
                e.printStackTrace()
                callback("error: ${e.message}")
            }
            _isLoading.value = false
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
            _isLoading.value = true
            if (doChangePassword(password, username, recovery) == "success") {
                _isLoading.value = false
                callback("success")
            } else {
                _isLoading.value = false
                return@launch
            }
        }
        _isLoading.value = false
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

    fun getUserData() {
        CoroutineScope(Dispatchers.IO).launch {
            _isLoading.value = true
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
            _isLoading.value = false
        }
    }

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

    fun deleteAccount() {

        CoroutineScope(Dispatchers.IO).launch {
            val manageURL = URL("http://25.49.50.144:8090/user-api/user")
            val request = Request.Builder()
                .url(manageURL)
                .delete()
                .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
                .build()

            try {
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    println("Chiamata fallita. Codice di stato: ${response.code}")
                    return@launch
                }
                println("Chiamata avvenuta con successo. Codice di stato: ${response.code}")

            } catch (e: IOException) {
                e.printStackTrace()
                println("Errore di rete o I/O: ${e.message}")
            }
        }
    }

    fun logout() {

        isAdmin.value = false
        logged.value = false

        viewModelScope.launch {
            val logoutDTO = LogoutDTO(true)
            val jsonObject = JSONObject()
                .put("logout", logoutDTO.logout)

            val json = jsonObject.toString()
            val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
            val requestBody = json.toRequestBody(mediaType)

            try {
                val manageURL = URL("http://25.49.50.144:8090/user-api/logout")
                val request = Request.Builder()
                    .url(manageURL)
                    .put(requestBody)
                    .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
                    .build()

                withContext(Dispatchers.IO) {
                    val response = client.newCall(request).execute()
                    if (!response.isSuccessful) {
                        println("Chiamata fallita. Codice di stato: ${response.code}")
                        return@withContext
                    }
                    println("Chiamata avvenuta con successo. Codice di stato: ${response.code}")
                }

            } catch (e: IOException) {
                e.printStackTrace()
                println("Errore di rete o I/O: ${e.message}")
            }
            finally {
                KeycloakService().getBasicToken()
            }
        }
    }

    fun getUserDataByAdmin(username: String) {
        CoroutineScope(Dispatchers.IO).launch {
            _isLoading.value = true
            try {
                println("sono nel try del get data")
                val manageURL = URL("http://25.49.50.144:8090/user-api/user/$username")
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
                val userUsername = jsonObject.optString("username", "")
                val email = jsonObject.optString("email", "")
                val phoneNumber = jsonObject.optString("phoneNumber", "")

                userDataForAdmin = UserDTO(userUsername, firstName, lastName, phoneNumber, email)
                addUserData(UserRegistrationDTO(firstName, lastName, userUsername, email, ""))
                println("Dati utente recuperati con successo: ${userDataForAdmin?.username}")
            } catch (e: IOException) {
                e.printStackTrace()
                println("Errore di rete o I/O: ${e.message}")
            } catch (e: JSONException) {
                e.printStackTrace()
                println("Errore nel parsing della risposta JSON: ${e.message}")
            }
            _isLoading.value = false
        }
    }

    fun modifyUserDataByAdmin(
        firstName: String,
        lastName: String,
        username: String,
        email: String,
        phoneNumber: String,
        callback: (result: String) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = doModifyUserByAdmin(firstName, lastName, username, email, phoneNumber)
                callback(result)
            } catch (e: Exception) {
                e.printStackTrace()
                callback("error: ${e.message}")
            }
            _isLoading.value = false
        }
    }

    suspend fun doModifyUserByAdmin(
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
            .put("email", newUserDTO.phoneNumber)
            .put("phoneNumber", newUserDTO.email)

        println(jsonObject.toString())
        println(email)

        val json = jsonObject.toString()
        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = json.toRequestBody(mediaType)

        val manageURL = URL("http://25.49.50.144:8090/user-api/user/$username")
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

