package com.example.caesarzonapplication.model.repository

import androidx.lifecycle.LiveData
import com.example.caesarzonapplication.model.dao.UserDao
import com.example.caesarzonapplication.model.entities.User

class UserRepository (private val userDao: UserDao){

    val readAllData: LiveData<List<User>> = userDao.getAllUsers()

    suspend fun addUser(user: User){
        userDao.addUser(user)
    }
}