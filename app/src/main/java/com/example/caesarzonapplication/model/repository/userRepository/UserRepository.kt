package com.example.caesarzonapplication.model.repository.userRepository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.caesarzonapplication.model.dao.userDao.UserDao
import com.example.caesarzonapplication.model.dto.UserDTO
import com.example.caesarzonapplication.model.dto.UserRegistrationDTO
import com.example.caesarzonapplication.model.entities.userEntity.User

class UserRepository(private val userDao: UserDao) {

    // Inserisci un nuovo utente
    suspend fun addUser(user: UserRegistrationDTO): Boolean {
        return try {
            userDao.addUser(User(user.lastName, user.username, user.firstName,  "", user.email, user.credentialValue, ""))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    // Recupera i dati dell'utente
    suspend fun getUserData(username: String): UserDTO {
        val user = userDao.getUserData(username)
        println("utente: "+user.username
            +" "+user.firstName+" "+user.lastName+" "+user.phoneNumber+" "+user.email)
        return UserDTO(user.username, user.firstName, user.lastName, user.phoneNumber, user.email)
    }


    // Elimina i dati dell'utente
    suspend fun deleteUserData(): Boolean {
        return try {
            userDao.deleteUserData()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}

