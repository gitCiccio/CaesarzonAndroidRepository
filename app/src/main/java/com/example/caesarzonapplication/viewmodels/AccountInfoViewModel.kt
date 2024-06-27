package com.example.caesarzonapplication.viewmodels

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caesarzonapplication.model.dto.UserDTO
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.URL

class AccountInfoViewModel : ViewModel() {

    private var _accountInfoData = MutableStateFlow(UserDTO("", "", "", "", "", ""))
    var accountInfoData: StateFlow<UserDTO> = _accountInfoData

    // StateFlow per l'immagine del profilo
    private val _profileImage = MutableStateFlow<Bitmap?>(null)
    val profileImage = _profileImage
    private val client = OkHttpClient()

    init {
        getUserData()
    }

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

    fun getUserData() {
        CoroutineScope(Dispatchers.IO).launch {
            println("Sto prendendo i dati dell'utente")
            val manageURL = URL("http://25.49.50.144:8090/user-api/user");
            val request = Request.Builder().url(manageURL)
                .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
                .build()
            try {
                val response = client.newCall(request).execute()
                println(response.message)
                if (!response.isSuccessful) {
                    return@launch
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
                _accountInfoData.value = userDTO
                println("Ho preso i dati dell'utente")
                println(accountInfoData.value.username)
            } catch (e: IOException) {
                e.printStackTrace()
                return@launch
            }
        }
    }
}
