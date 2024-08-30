package com.example.caesarzonapplication.model.viewmodels.userViewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.caesarzonapplication.model.dto.userDTOS.FollowerDTO
import com.example.caesarzonapplication.model.dto.notificationDTO.UserSearchDTO
import com.example.caesarzonapplication.model.entities.userEntity.Follower
import com.example.caesarzonapplication.model.repository.userRepository.FollowerRepository
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import java.net.URL


//vedere come aggiungere i follower
//capire perché i dati non sono salvati subito
//capire perché non cambia lo stato
class FollowersViewModel(private val followerRepository: FollowerRepository): ViewModel() {

    private lateinit var getAllFollowers: LiveData<List<Follower>>

    val client = OkHttpClient()
    val gson = Gson()

    init {
        viewModelScope.launch{
            getAllFollowers = followerRepository.getAllFollowers()
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

    private val newFollowers = mutableListOf<UserSearchDTO>()

    fun addFollower(follower: UserSearchDTO) {
        val user = _users.find { it.username == follower.username }
        user?.let {
            it.follower = true

            val existingFollower = _followers.find { it.username == follower.username }
            if (existingFollower == null) {
                _followers.add(it)
                newFollowers.add(it)
            }
        }
    }

    suspend fun doAddFollower(){
        val manageURL = URL("http://25.49.50.144:8090/user-api/followers")
        val jsonType = "application/json; charset=utf-8".toMediaType()

        val gson = Gson()
        val json = gson.toJson(newFollowers)
        val requestBody = json.toRequestBody(jsonType)
        val request = Request.Builder().url(manageURL).post(requestBody).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

        withContext(Dispatchers.IO){
            try{
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                println("response code: ${response.code}")
                if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun removeFollower(follower: UserSearchDTO) {
        if(follower in _followers){
            _followers.remove(follower)
            if(follower in _friends) {
                _friends.remove(follower)
            }
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
        follower.friend = !follower.friend
        if(follower in _friends){
            _friends.remove(follower)
        }
        else{
            _friends.add(follower)
        }
    }

    fun searchUsers(username: String) {
        _users.clear()
        if (username.isEmpty()) return

        val manageURL = URL("http://25.49.50.144:8090/user-api/users/$username")
        val request = Request.Builder().url(manageURL).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()
        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if(!response.isSuccessful){
                println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
            }

            val listUsername = object : TypeToken<List<String>>() {}.type
            for (user in gson.fromJson<List<String>>(responseBody, listUsername)) {
                val userSearchDto = UserSearchDTO(user,  follower = false, friend = false)
                _users.add(userSearchDto)
            }
        }
        catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    fun loadAllFollowers()
    {
        viewModelScope.launch {
            doLoadFollowersAndFriends(0, false)
        }
    }

    fun loadAllFriends()
    {
        viewModelScope.launch {
            doLoadFollowersAndFriends(0, true)
        }
    }

    suspend fun doLoadFollowersAndFriends(flw: Int, friend: Boolean) {
        val manageURL = URL("http://25.49.50.144:8090/user-api/followers?flw=$flw&friend=$friend")
        val request = Request.Builder().url(manageURL).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()
        _friends.clear()
        _followers.clear()

        withContext(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (!response.isSuccessful) {
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                } else {
                    val jsonResponse = JSONArray(responseBody)

                    val listUsers = object : TypeToken<List<UserSearchDTO>>() {}.type
                    val users: List<UserSearchDTO> = gson.fromJson(jsonResponse.toString(), listUsers)

                    if (friend) {
                        _friends.addAll(users)
                    } else {
                        _followers.addAll(users)
                    }

                    // Foreach per inserire gli utenti usando followerRepository
                    for (user in users) {
                        followerRepository.addFollower(FollowerDTO("","",user.username, user.friend))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                println("Errore nel caricamento dei follower e amici")
            }
        }
    }

    suspend fun doUpdateFollowerStatus() {
        val manageURL = "http://25.49.50.144:8090/user-api/followers"
        for (follower in followers) {
            println("Username: ${follower.username}, status follower: ${follower.follower}, status friend: ${follower.friend}")
        }
        val followersAndFriends = followers + friends
        withContext(Dispatchers.IO) {
            try {
                val jsonInputString = gson.toJson(followersAndFriends)
                val mediaType = "application/json; charset=utf-8".toMediaType()
                val requestBody = jsonInputString.toRequestBody(mediaType)

                val request = Request.Builder()
                    .url(manageURL)
                    .post(requestBody)
                    .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build()

                val response: Response = client.newCall(request).execute()
                val responseCode = response.code

                if (responseCode == 200) {
                    println("Follower status updated successfully")
                } else {
                    println("Error updating follower status: ${response.message}")
                }
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
        }
    }

    fun saveChanges() {
        viewModelScope.launch {
            if (newFollowers.isNotEmpty()){
                doAddFollower()
                newFollowers.clear()
            }
            doUpdateFollowerStatus()
            println("Salvataggio effettuato")
        }
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