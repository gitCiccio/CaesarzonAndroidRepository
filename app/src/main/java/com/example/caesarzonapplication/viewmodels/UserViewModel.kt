package com.example.caesarzonapplication.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.caesarzonapplication.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel: ViewModel(){
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

    fun addFollower(user: User){
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
    }
}