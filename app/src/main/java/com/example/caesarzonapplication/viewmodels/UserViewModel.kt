package com.example.caesarzonapplication.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.caesarzonapplication.model.User

class UserViewModel: ViewModel(){
    private val _users = mutableStateListOf<User>()
    val users: List<User> get() = _users

    private val _friends = mutableStateListOf<User>()
    val friends: List<User> get() = _friends

    private val _favorites = mutableStateListOf<User>()
    val favorites: List<User> get() = _favorites

    init {
        loadUsers()
    }

    private fun loadUsers() {
        _users.addAll(
            listOf(
                User("Pino Cammino",true),
                User("Tina Patatina",false),
                User("Giulio Regeni",false),
                User("Thomas Turbato",false)
            )
        )
    }

    fun addFriend(user: User){
        if(!_friends.contains(user)){
            _friends.add(user)
        }
    }

    fun toggleFavorite(user: User){
        user.copy(user.username, !user.isFavorite)
        println("Username: "+user.username+" status: "+user.isFavorite.toString())
    }
}