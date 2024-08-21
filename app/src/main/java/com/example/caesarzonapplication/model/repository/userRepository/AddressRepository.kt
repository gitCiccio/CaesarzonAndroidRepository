package com.example.caesarzonapplication.model.repository.userRepository

import com.example.caesarzonapplication.model.dao.userDao.AddressDao
import com.example.caesarzonapplication.model.entities.userEntity.Address

class AddressRepository(private val addressDao: AddressDao) {

    // Recupera tutti gli indirizzi
    suspend fun getAllAddress(): List<Address> {
        return addressDao.getAllAddress()
    }

    // Recupera un indirizzo per ID
    suspend fun getAddressById(id: Long): Address? {
        return addressDao.getAddressById(id)
    }

    // Inserisci un nuovo indirizzo
    suspend fun addAddress(address: Address) {
        addressDao.addAddress(address)
    }

    // Elimina un indirizzo per ID
    suspend fun deleteById(id: Long) {
        addressDao.deleteById(id)
    }

    // Elimina tutti gli indirizzi
    suspend fun deleteAll() {
        addressDao.deleteAll()
    }
}
