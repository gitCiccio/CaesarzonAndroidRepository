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
            userDao.addUser(User(user.username, user.firstName, user.lastName, "", user.email, user.credentialValue, ""))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    // Recupera i dati dell'utente
    fun getUserData(): LiveData<UserDTO> {
        return userDao.getUserData().map { user ->
            user.let {
                UserDTO(it.username, it.firstName, it.lastName, it.phoneNumber, it.email)
            }
        }
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

