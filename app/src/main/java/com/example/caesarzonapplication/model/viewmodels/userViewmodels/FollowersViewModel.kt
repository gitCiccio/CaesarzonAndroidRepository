package com.example.caesarzonapplication.model.viewmodels.userViewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.caesarzonapplication.model.database.AppDatabase
import com.example.caesarzonapplication.model.dto.FollowerDTO
import com.example.caesarzonapplication.model.dto.UserSearchDTO
import com.example.caesarzonapplication.model.entities.userEntity.Follower
import com.example.caesarzonapplication.model.repository.userRepository.FollowerRepository
import com.example.caesarzonapplication.model.repository.userRepository.ProfileImageRepository
import com.example.caesarzonapplication.model.repository.userRepository.UserRepository
import com.example.caesarzonapplication.model.service.KeycloakService
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class FollowersViewModel(private val followerRepository: FollowerRepository): ViewModel() {

    private lateinit var getAllFollowers: LiveData<List<Follower>>

    val client = OkHttpClient()

    init {
        viewModelScope.launch{
            getAllFollowers = followerRepository.getAllFollowers()
        }
    }

    fun addFollower(follower: FollowerDTO){
        viewModelScope.launch{
            followerRepository.addFollower(follower)
        }
    }


    private var _users = mutableStateListOf<UserSearchDTO>()

    //User inviati da aldo, quando li cerco
    val users: List<UserSearchDTO> get() = _users

    //FollowerDTO, gli utenti che aldo mi manda al caricamento della pagina
    private var _followers = mutableStateListOf<UserSearchDTO>()
    val followers: List<UserSearchDTO> get() = _followers

    private var _friends = mutableStateListOf<UserSearchDTO>()
    val friends: List<UserSearchDTO> get() = _friends

    private val _newFollowersAndFriends = mutableListOf<UserSearchDTO>()
    //capire come fare
    //private val _deletedFollowersAndFriends = mutableListOf<FollowerDTO>()
    //Lista degli amici che mi gestisco dopo che ho i follower


    init {
        viewModelScope.launch {
            loadFollowersAndFriends(0, false)
            loadFollowersAndFriends(0, true)
        }
        //loadMyFakeUsers()
    }


    //Aggiunta del follower ok
    fun addFollower(follower: UserSearchDTO) {
        // Update the state of the users who become followers
        val user = _users.find { it.username == follower.username }
        user?.let {
            it.follower = true

            val existingFollower = _followers.find { it.username == follower.username }
            if (existingFollower == null) {
                _followers.add(it)
                _newFollowersAndFriends.add(it)
            }
        }
        viewModelScope.launch {
            doAddFollower(follower)
        }
    }

    //Da provare domani
    suspend fun doAddFollower(follower: UserSearchDTO){
        val manageURL = URL("http://25.49.50.144:8090/user-api/followers")
        val JSON = "application/json; charset=utf-8".toMediaType()

        val gson = Gson()
        val json = gson.toJson(follower)
        val requestBody = json.toRequestBody(JSON)
        val request = Request.Builder().url(manageURL).post(requestBody).addHeader("Authorization", "Bearer ${KeycloakService.myToken}").build()

        withContext(Dispatchers.IO){
            println("Snono nel try dell'aggiunta del follower")
            try{
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                println("response code: ${response.code}")
                if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                }
                println("Risposta dal server: $responseBody")
                println("follower Aggiunto con successo")
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }



    fun removeFollower(follower: UserSearchDTO) {
        // Trova l'utente con lo stesso username del follower
        val user = _users.find { it.username == follower.username }
        if (user != null) {
            println("Username: ${user.username}, status follower: ${user.follower}, status friend: ${user.friendStatus}")
        }
        // Se l'utente è trovato, esegui le operazioni richieste
        user?.let {
            // Controlla e aggiorna il friend status se necessario
            if (it.friendStatus) {
                println("Username: ${user.username}, status follower: ${user.follower}, status friend: ${user.friendStatus}")
                it.friendStatus = false
                _friends.removeIf { friend -> friend.username == it.username }
            }

            // Aggiorna il follower status
            it.follower = false
            println("Username: ${user.username}, new status follower: ${user.follower}, new status friend: ${user.friendStatus}")
            _followers.removeIf { follower -> follower.username == it.username }

            // Aggiungi l'utente aggiornato a _newFollowersAndFriends
            _newFollowersAndFriends.add(it)
        }
        viewModelScope.launch {
            doRemoveFollower(follower)
        }
    }

    suspend fun doRemoveFollower(follower: UserSearchDTO){
        val manageURL = URL("http://25.49.50.144:8090/user-api/followers/${follower.username}")
        val request = Request.Builder().url(manageURL).delete().addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()
        withContext(Dispatchers.IO){
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                }
                println("Risposta dal server: $responseBody")
                println("Follower eliminato con successo")
                followerRepository.deleteFollowerByUsername(follower.username)
            }catch (e: Exception){
                e.printStackTrace()
                println("Errore nell'eliminazione del follower")
            }
        }
    }

    fun toggleFriendStatus(follower: UserSearchDTO) {
        println("Inside toggleFriendStatus")
        println("Username: ${follower.username}, status follower: ${follower.follower}, status friend: ${follower.friendStatus}")
        val user = _users.find { it.username == follower.username }
        if(user != null){
            follower.friendStatus = !follower.friendStatus
            println("Username: ${follower.username}, status follower: ${follower.follower}, status friend: ${follower.friendStatus}")

            if(follower.friendStatus){
                if(!_friends.contains(follower))
                    _friends.add(follower)
            }else{
                _friends.remove(follower)
            }
        }
        _newFollowersAndFriends.add(follower)
    }
    //da vedere domani
    fun searchUsers(username: String) {
        _users.clear()
        if(username.isEmpty()) return
        val manageURL = URL("http://25.49.50.144:8090/search-api/search/users?username=$username")
        val connection = manageURL.openConnection() as HttpURLConnection

        try{
            connection.requestMethod = "GET"

            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = StringBuilder()
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            reader.close()

            println("Response Code: ${connection.responseCode}")
            if(connection.responseCode == HttpURLConnection.HTTP_OK){
                val jsonResponse = JSONArray(response.toString())
                println("Response Body: $jsonResponse")
                for(i in 0 until jsonResponse.length()){
                    val jsonObject = jsonResponse.getJSONObject(i)
                    val username = jsonObject.getString("username")
                    //val profilePictureBase64 = jsonObject.getString("profilePicture") prendere in una seconda chiamata
                    val isFriend = jsonObject.getBoolean("isFriend")
                    val follower = jsonObject.getBoolean("follower")

                    //val profilePicBytes = Base64.decode(profilePictureBase64)

                    //val profilePicture: Bitmap = BitmapFactory.decodeByteArray(profilePicBytes, 0, profilePicBytes.size)
                    val userSearchDTO = UserSearchDTO(
                        username,
                        "profilePicture",
                        isFriend,
                        follower
                    )
                    _users.add(userSearchDTO)
                }
            }else{
                println("Error: ${connection.responseMessage}")
            }

        }finally {
            connection.disconnect()
        }
    }

    //Inserire nell'init per il caricamento dei dati
    //TODO GET PEr ottenere la lista di username dei follower, booleana per indicare se un follower è amico o meno e immagine di profilo, sarà una loadDeiFollower
    suspend fun loadFollowersAndFriends(flw: Int, friend: Boolean){//flw è l'indice, mentre friend indica se voglio i follower o meno
            val manageURL = URL("http://25.49.50.144:8090/user-api/followers?flw=$flw&friend=$friend")//FIXME da vedere se l'api è giusta
            val request = Request.Builder().url(manageURL).addHeader("Authorization", "Bearer ${myToken?.accessToken ?: ""}") // Assicurati che il token non sia nullo
                .build()

            withContext(Dispatchers.IO){
                try{
                    val response = client.newCall(request).execute()
                    val responseBody = response.body?.string()
                    println("response code: ${response.code}")

                    if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                        println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                    }else{
                        val jsonResponse = JSONArray(responseBody)
                        println("Response Body: $jsonResponse")

                        for (i in 0 until jsonResponse.length()) {
                            val jsonObject = jsonResponse.getJSONObject(i)
                            val username = jsonObject.getString("username")
                            val friendStatus = jsonObject.getBoolean("isFriend")
                            val profilePictureBase64 = jsonObject.getString("profilePicture")

                            if (friend) {
                                _friends.add(UserSearchDTO(username, profilePictureBase64, friendStatus, true))
                            } else {
                                _followers.add(UserSearchDTO(username, profilePictureBase64, friendStatus, true))
                            }
                        }
                    }

                }catch (e: Exception){
                    e.printStackTrace()
                    println("Errore nel caricamento dei follower e amici")
                }
            }
    }

        //Fai domani
        //TODO POST per modificare lo stato di un follower, amico o non amico, passiamo username
        fun updateFollowerStatus(followesAndFriends: List<UserSearchDTO>) {
            val manageURL = URL("http://25.49.50.144:8090/user-api/followers")
            val connection = manageURL.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.doOutput = true
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accept", "application/json")
            connection.setRequestProperty("Authorization", "Bearer ${KeycloakService.myToken}")

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val gson = Gson()
                    val jsonInputString = gson.toJson(followesAndFriends)

                    connection.outputStream.use { outputStream ->
                        val writer = OutputStreamWriter(outputStream, "UTF-8")
                        writer.write(jsonInputString)
                        writer.flush()
                    }

                    val responseCode = connection.responseCode
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        println("Follower status updated successfully")
                    } else {
                        println("Error updating follower status: ${connection.responseMessage}")
                    }
                } catch (e: Exception) {
                    println("Error: ${e.message}")
                } finally {
                    connection.disconnect()
                }
            }
        }


        //TODO Fake medotdhs
        fun loadMyFakeUsers() {
            val fakeUsers = listOf(
                UserSearchDTO("fakeUser1", "Ciao", false, false),
                UserSearchDTO("fakeUser2", "Ciao", false, false),
                UserSearchDTO("fakeUser3", "Ciao", false, false),
                UserSearchDTO("fakeUser4", "Ciao", false, false),
                UserSearchDTO("fakeUser5", "Ciao", false, false),
                UserSearchDTO("fakeUser6", "Ciao", false, false),
                UserSearchDTO("fakeUser7", "Ciao", false, false),
                UserSearchDTO("fakeUser8", "Ciao", false, false),
                UserSearchDTO("fakeUser9", "Ciao", false, false),
                UserSearchDTO("fakeUser10", "Ciao", false, false),
                UserSearchDTO("fakeUser11", "Ciao", false, false),
                UserSearchDTO("fakeUser12", "Ciao", false, false),
                UserSearchDTO("fakeUser13", "Ciao", false, false),
                UserSearchDTO("fakeUser14", "Ciao", false, false),
                UserSearchDTO("fakeUser15", "Ciao", false, false),
                UserSearchDTO("fakeUser16", "Ciao", false, false),
                UserSearchDTO("fakeUser17", "Ciao", false, false),
                UserSearchDTO("fakeUser18", "Ciao", false, false),
                UserSearchDTO("fakeUser19", "Ciao", false, false),
                UserSearchDTO("fakeUser20", "Ciao", false, false),
                UserSearchDTO("fakeUser21", "Ciao", false, false)
            )

            _users.addAll(fakeUsers)
        }
}
class FollowersViewModelFactory(
    private val followerRepository: FollowerRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FollowersViewModel::class.java)) {
            return FollowersViewModel(followerRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}