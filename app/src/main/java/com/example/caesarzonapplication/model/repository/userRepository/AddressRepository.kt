package com.example.caesarzonapplication.model.repository.userRepository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.caesarzonapplication.model.dao.userDao.AddressDao
import com.example.caesarzonapplication.model.dto.AddressDTO
import com.example.caesarzonapplication.model.entities.userEntity.Address

class AddressRepository(private val addressDao: AddressDao) {

    // Recupera tutti gli indirizzi
    fun getAllAddresses(): LiveData<List<Address>> {
        return try{
            val addresses = addressDao.getAllAddreses()
            addresses
        }catch (e: Exception){
            e.printStackTrace()
            MutableLiveData(emptyList())
        }
    }


    // Inserisci un nuovo indirizzo
    suspend fun addAddress(address: AddressDTO): Boolean {
        return try{
            val address = Address(id = 0, address_id = address.id, streetName = address.roadName, streetNumber = address.houseNumber, roadType = address.roadType, city = address.city.id.toLong())
            addressDao.addAddress(address)
            true
        }catch (e: Exception){
            false
        }
    }

    // Elimina un indirizzo per ID
    suspend fun deleteById(id: Long): Boolean{
        return try{
            addressDao.deleteById(id)
            true
        }catch (e: Exception){
            e.printStackTrace()
            false
        }
    }

    // Elimina tutti gli indirizzi
    suspend fun deleteAll(): Boolean {
        return try{
            addressDao.deleteAll()
            true
        }catch (e: Exception){
            e.printStackTrace()
            false
        }
    }
}
