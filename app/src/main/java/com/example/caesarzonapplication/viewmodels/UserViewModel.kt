package com.example.caesarzonapplication.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.caesarzonapplication.model.MiniUser
import com.example.caesarzonapplication.model.User
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import com.google.gson.*

class UserViewModel: ViewModel(){

    private val gson = Gson()

    private var name = ""

    private val _users = mutableStateListOf<User>()
    val users: List<User> get() = _users

    private val _followers = mutableStateListOf<User>()
    val followers: List<User> get() = _followers

    private val _friends = mutableStateListOf<User>()
    val friends: List<User> get() = _friends

    init {
        loadUsers()
    }

    private fun loadUsers() {

    }

    /*fun addFollower(user: User){
        if(!_followers.contains(user)){
            _followers.add(user)
        }
        user.isFollower = true
    }

    fun removeFollower(user: User){
        if(_followers.contains(user)){
            _followers.remove(user)
            user.isFollower = false
        }
        if(_friends.contains(user)){
            _friends.remove(user)
            user.isFriend = false
        }
    }

    fun toggleFriendStatus(user: User){
        CoroutineScope(Dispatchers.IO).launch {
            user.isFriend = !user.isFriend;
            if(_friends.contains(user)){
                _friends.remove(user)
                user.isFriend = false
            }else{
                _friends.add(user)
                user.isFriend = true
            }
        }
    }*/

    fun authenticateUser(username: String, password: String, accessToken: String){
        val manageURL = URL("http://160.97.90.201:8090/user-api/user")

        val connection = manageURL.openConnection() as HttpURLConnection

        try{
            connection.requestMethod = "GET"

            connection.setRequestProperty("Authorization", "Bearer "+accessToken)
            val responseCode = connection.responseCode
            val body = connection.responseMessage
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
                val user = gson.fromJson(response.toString(), User::class.java)
                name = user.username.toString()
                println(name)
            //_currentUser = User(user.id, user.firstName, user.lastName, user.username, user.phoneNumber,user.email)
            } else {
                println("Error: ${connection.responseMessage}")
            }
        }finally {
            connection.disconnect()
        }

    }
    fun getUsername(): String {
        return name
    }


}