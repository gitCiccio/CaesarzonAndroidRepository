package com.example.caesarzonapplication.viewmodels

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModel
import com.example.caesarzonapplication.R
import com.example.caesarzonapplication.model.dto.FollowerDTO
import com.example.caesarzonapplication.model.dto.UserSearchDTO
import com.example.caesarzonapplication.model.service.KeycloakService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

//TODO capire come gestire la logica delle liste con i nuovi dati
class FollowersAndFriendsViewModel: ViewModel() {

    val keycloak = KeycloakService()

    private var _users = mutableStateListOf<UserSearchDTO>()
    //User inviati da aldo, quando li cerco
    val users: List<UserSearchDTO> get() = _users
    //FollowerDTO, gli utenti che aldo mi manda al caricamento della pagina
    private var _followers = mutableStateListOf<FollowerDTO>()
    val followers: List<FollowerDTO> get() = _followers

    private var _friends = mutableStateListOf<FollowerDTO>()
    val friends: List<FollowerDTO> get() = _friends

    private val _newFollowersAndFriends = mutableListOf<FollowerDTO>()
    //capire come fare
    private val _deletedFollowersAndFriends = mutableListOf<FollowerDTO>()
    //Lista degli amici che mi gestisco dopo che ho i follower


    init {
        loadFollowersAndFriends(0, false)
    }

    //Aggiunta del follower ok
    fun addFollower(follower: UserSearchDTO) {
        // Update the state of the users who become followers
        _users.find { it.username == follower.username }?.let {
            it.follower = true
        }

        // Add to followers if not already present
        val newFollower = FollowerDTO(UUID.randomUUID(), "myUsername", follower.username, false)
        if (!_followers.any { it.userUsername2 == follower.username }) {
            _followers.add(newFollower)
            _newFollowersAndFriends.add(newFollower)
            println("Aggiunto follower")
        }
    }



    fun removeFollower(follower: FollowerDTO) {
        // Update the state of the user
        _users.find { it.username == follower.userUsername2 }?.let {
            it.follower = false
        } ?: run {
            // If user is not found, add a new user with follower status false
            _users.add(UserSearchDTO(follower.userUsername2, "foto_prolifo", false, false))
        }

        println("Vuoi eliminare il follower " + follower.userUsername2)

        // Remove follower from the followers list
        if (_followers.removeIf { it.userUsername2 == follower.userUsername2 }) {
            println("Follower eliminato da followers")
        }

        // Remove follower from the friends list
        if (_friends.removeIf { it.userUsername2 == follower.userUsername2 }) {
            println("Follower eliminato da friends")
        }

        // Remove follower from new followers and friends list
        if (_newFollowersAndFriends.removeIf { it.userUsername2 == follower.userUsername2 }) {
            println("Follower eliminato da newFollowersAndFriends")
        }
    }


    fun toggleFriendStatus(follower: FollowerDTO) {
        println("FriendStatus prima: " + follower.friendStatus.toString())

        val updatedFollower = follower.copy(friendStatus = !follower.friendStatus)

        println("FriendStatus dopo: " + updatedFollower.friendStatus.toString())

        if (updatedFollower.friendStatus) {
            if (!_friends.contains(updatedFollower)) {
                _friends.add(updatedFollower)
            }
            _followers.removeAll { it.userUsername2 == updatedFollower.userUsername2 }
        } else {
            _friends.removeAll { it.userUsername2 == updatedFollower.userUsername2 }
            if (!_followers.any { it.userUsername2 == updatedFollower.userUsername2 }) {
                _followers.add(updatedFollower.copy(friendStatus = false))
            }
        }
    }


    /*fun getUserData() {
        val manageURL = URL("http://25.49.50.144:8090/user-api/user")
        val connection = manageURL.openConnection() as HttpURLConnection

        try{
            connection.requestMethod = "GET"
            connection.setRequestProperty("Authorization", "Bearer "+keycloakService.myToken?.accessToken)
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
            } else {
                println("Error: ${connection.responseMessage}")
            }
        }finally {

            connection.disconnect()
        }
    }*/

            //Questa funzione è ok
    @OptIn(ExperimentalEncodingApi::class)
    fun searchUsers(username: String) {
        _users.clear()
        if(username.isEmpty()) return
        val manageURL = URL("http://25.49.50.144:8090/search-api/search/users?username="+username)
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
                println("Response Body: "+jsonResponse)
                for(i in 0 until jsonResponse.length()){
                    val jsonObject = jsonResponse.getJSONObject(i)
                    val username = jsonObject.getString("username")
                    val profilePictureBase64 = jsonObject.getString("profilePicture")
                    val isFriend = jsonObject.getBoolean("isFriend")
                    val follower = jsonObject.getBoolean("follower")

                    val profilePicBytes = Base64.decode(profilePictureBase64)

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


    fun saveNewFollowersAndFriendsOnDB(){
        CoroutineScope(Dispatchers.IO).launch {
            val manageURL = URL("http://25.49.50.144:8090/user-api/followers")//FIXME da vedere se l'api è giusta
            val connection = manageURL.openConnection() as HttpURLConnection

            try{
                connection.requestMethod = "POST"
                connection.doOutput = true
                connection.setRequestProperty("Authorization", "Bearer "+KeycloakService.myToken)
                connection.setRequestProperty("Content-Type", "application/json")
            }catch (e: Exception){
                println("Errore: $e")
            }
        }
    }

    //TODO GET PEr ottenere la lista di username dei follower, booleana per indicare se un follower è amico o meno e immagine di profilo, sarà una loadDeiFollower
    fun loadFollowersAndFriends(flw: Int, friend: Boolean){
        CoroutineScope(Dispatchers.IO).launch {
            val friendStatus = friend
            val manageURL = URL("http://25.49.50.144:8090/user-api/followers?flw=$flw&friend=$friendStatus")//FIXME da vedere se l'api è giusta
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
                    println("Response Body: "+jsonResponse)
                    for(i in 0 until jsonResponse.length()){
                        val jsonObject = jsonResponse.getJSONObject(i)
                        val username = jsonObject.getString("username")
                        val friendStatus = jsonObject.getBoolean("isFriend")
                        val profilePictureBase64 = jsonObject.getString("profilePicture")


                        if(friendStatus) {
                            _friends.add(FollowerDTO(UUID.randomUUID(), "myUsername", username,friendStatus))
                        }
                        else{
                            _followers.add(FollowerDTO(UUID.randomUUID(), "myUsername", username,friendStatus))
                        }
                    }
                }else{
                    println("Error: ${connection.responseMessage}")
                }

            }finally {
                connection.disconnect()
            }
        }
    }

    //TODO PUT per modificare lo stato di un follower, amico o non amico, passiamo username


    //TODO POST per aggiungere un follower, passiamo username

    //TODO DELETE per rimuovere un follower, passiamo username

    //TODO Fake medotdhs
    fun loadMyFakeUsers() {
        val fakeUsers = listOf(
            UserSearchDTO("fakeUser1", "Ciao", false, false),
            UserSearchDTO("fakeUser2", "Ciao", false, false),
            UserSearchDTO("fakeUser3", "Ciao", false, false),
            UserSearchDTO("fakeUser4", "Ciao", true, false)
        )

        _users.addAll(fakeUsers)
    }

    fun loadFakeFollowers() {
        // Simulazione di dati fittizi per i follower
        val fakeFollowers = listOf(
            FollowerDTO(UUID.randomUUID(), "myUsername", "follower1", false),
            FollowerDTO(UUID.randomUUID(), "myUsername", "follower2", false),
            FollowerDTO(UUID.randomUUID(), "myUsername", "follower3", false),
            FollowerDTO(UUID.randomUUID(), "myUsername", "follower4", false)
        )
        _followers.addAll(fakeFollowers)
    }

    fun loadFakeFriends() {
        // Simulazione di dati fittizi per gli amici
        val fakeFriends = listOf(
            FollowerDTO(UUID.randomUUID(), "myUsername", "friend1", true),
            FollowerDTO(UUID.randomUUID(), "myUsername", "friend2", true),
            FollowerDTO(UUID.randomUUID(), "myUsername", "friend3", true)
        )
        _friends.addAll(fakeFriends)
    }
}
