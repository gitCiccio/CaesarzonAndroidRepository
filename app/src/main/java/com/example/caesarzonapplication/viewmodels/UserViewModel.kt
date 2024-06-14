package com.example.caesarzonapplication.viewmodels

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.caesarzonapplication.model.User
import com.example.caesarzonapplication.model.dto.FollowerDTO
import com.example.caesarzonapplication.model.dto.UserSearchDTO
import com.google.gson.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


class UserViewModel: ViewModel() {


    private var _users = mutableStateListOf<UserSearchDTO>()
    //User inviati da aldo, quando li cerco
    val users: List<UserSearchDTO> get() = _users
    //FollowerDTO, gli utenti che aldo mi manda al caricamento della pagina
    private val _followers = mutableStateListOf<FollowerDTO>()
    val followers: List<FollowerDTO> get() = _followers

    private val _newFollowersAndFriends = mutableListOf<FollowerDTO>()
    val newFollowersAndFriends: List<FollowerDTO> get() = _newFollowersAndFriends
    //Lista degli amici che mi gestisco dopo che ho i follower
    private val _friends = mutableStateListOf<User>()
    val friends: List<User> get() = _friends

    init {
        //loadFollowersAndFriends()
    }


    fun addFollower(follower: FollowerDTO){
        if(!_followers.contains(user)){
            _followers.add(user)
            _newFollowersAndFriends.add(user.copy(id = user.id,userUsername1 = user.userUsername1, userUsername2 =  user.userUsername2, friendStatus = false))
        }
    }

    fun removeFollower(follower: FollowerDTO){
        if(_followers.contains(user) || _newFollowersAndFriends.containsKey(user.username)){
            _followers.remove(user)
            user.isFollower = false
            _newFollowersAndFriends.remove(user.username)
        }
        if(_friends.contains(user) || _newFollowersAndFriends.containsKey(user.username)){
            _friends.remove(user)
            user.isFriend = false
            _newFollowersAndFriends.remove(user.username)
        }
    }

    fun toggleFriendStatus(user: User){
        user.isFriend = !user.isFriend;
        if(_friends.contains(user) || _newFollowersAndFriends.containsKey(user.username) && _newFollowersAndFriends.get(user.username) == true){
            _newFollowersAndFriends[user.username] = false//cambia i dati da mandare al back
            _friends.remove(user)//cambia graficamente l'utente dalla lista
            user.isFriend = false
        }else{
            _friends.add(user)
            user.isFriend = true
            _newFollowersAndFriends[user.username] = true
        }
    }

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

                    val profilePicture: Bitmap = BitmapFactory.decodeByteArray(profilePicBytes, 0, profilePicBytes.size)
                    val userSearchDTO = UserSearchDTO(
                        username,
                        profilePicture,
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

    fun loadFollowersAndFriends(){
        CoroutineScope(Dispatchers.IO).launch {
            val manageURL = URL("http://25.49.50.144:8090/user-api/user/followers")//FIXME da vedere se l'api è giusta
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


                        if(friendStatus) {
                            _friends.add(
                                User()
                                    .copy(
                                        "",
                                        "",
                                        "",
                                        username,
                                        "",
                                        "",
                                        true,
                                        true
                                    )
                            )
                        }
                        _followers.add(
                            User()
                                .copy(
                                    "",
                                    "",
                                    "",
                                    username,
                                    "",
                                    "",
                                    true
                                )
                        )
                    }
                }else{
                    println("Error: ${connection.responseMessage}")
                }

            }finally {
                connection.disconnect()
            }
        }
    }

    fun loardUsers(){
        /*
        * public class UserSearchDTO {
    private String username;
    private byte[] profilePic;
}*/
    }
    fun saveNewFollowersAndFriendsOnDB(){
        CoroutineScope(Dispatchers.IO).launch {
            val manageURL = URL("http://25.49.50.144:8090/user-api/user/newFollowers")//FIXME da vedere se l'api è giusta
            //TODO put per
        }
    }

    //TODO GET PEr ottenere la lista di username dei follower, booleana per indicare se un follower è amico o meno e immagine di profilo, sarà una loadDeiFollower
    //TODO PUT per modificare lo stato di un follower, amico o non amico, passiamo username
    //TODO POST per aggiungere un follower, passiamo username
    //TODO DELETE per rimuovere un follower, passiamo username


}
