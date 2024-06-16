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
/*COSE FATTE:
  - Chiamata per caaricare amici e follower,
  - Chiamata per cercare gli utenti
 */
class FollowersAndFriendsViewModel: ViewModel() {

    val keycloak = KeycloakService()

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
        //loadFollowers(0, false)
        //loadFriends(0, true)
        loadMyFakeUsers()
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
            if (it.friendStatus == true) {
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

        /*
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


    //TODO GET PEr ottenere la lista di username dei follower, booleana per indicare se un follower è amico o meno e immagine di profilo, sarà una loadDeiFollower
    fun loadFollowers(flw: Int, friend: Boolean){//flw è l'indice, mentre friend indica se voglio i follower o meno
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
                    println("Response Body: $jsonResponse")
                    for(i in 0 until jsonResponse.length()){
                        val jsonObject = jsonResponse.getJSONObject(i)
                        val username = jsonObject.getString("username")
                        val friendStatus = jsonObject.getBoolean("isFriend")
                        val profilePictureBase64 = jsonObject.getString("profilePicture")

                        _followers.add(FollowerDTO(UUID.randomUUID(), "myUsername", username,friendStatus))
                    }
                }else{
                    println("Error: ${connection.responseMessage}")
                }

            }finally {
                connection.disconnect()
            }
        }
    }

    fun loadFriends(flw: Int, friend: Boolean){
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

                        _followers.add(FollowerDTO(UUID.randomUUID(), "myUsername", username,friendStatus))
                    }
                }else{
                    println("Error: ${connection.responseMessage}")
                }

            }finally {
                connection.disconnect()
            }
        }
    }*/

        //TODO POST per modificare lo stato di un follower, amico o non amico, passiamo username


        //TODO DELETE per rimuovere un follower, passiamo username

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
                UserSearchDTO("fakeUser12", "Ciao", false, false)
            )

            _users.addAll(fakeUsers)
        }
}

