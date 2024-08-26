package com.example.caesarzonapplication.model.repository.userRepository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.caesarzonapplication.model.dao.userDao.AddressDao
import com.example.caesarzonapplication.model.dto.AddressDTO
import com.example.caesarzonapplication.model.dto.CityDataDTO
import com.example.caesarzonapplication.model.entities.userEntity.Address
import com.example.caesarzonapplication.model.entities.userEntity.CityData

class AddressRepository(private val addressDao: AddressDao) {

    // Recupera tutti gli indirizzi
    fun getAllAddresses(): LiveData<List<AddressDTO>> {
        return try{
           val addresses = addressDao.getAllAddreses()

            addresses.map { addressList ->
                addressList.map {
                    AddressDTO(
                        id = it.address_id,
                        roadName = it.streetName,
                        houseNumber = it.streetNumber,
                        roadType = it.roadType,
                        city = CityDataDTO(
                            it.city.id_city_data.toLong(),
                            it.city.city,
                            it.city.cap,
                            it.city.region,
                            it.city.province
                        )
                    )
                }
            }

        }catch (e: Exception){
            e.printStackTrace()
            MutableLiveData(emptyList())
        }
    }


    // Inserisci un nuovo indirizzo
    suspend fun addAddress(address: AddressDTO): Boolean {
        println("sono in addAddress")
        return try{
            val newAddress =
                Address(
                    address_id = address.id,
                    streetName = address.roadName,
                    streetNumber = address.houseNumber,
                    roadType = address.roadType,
                    city = CityData(address.city.id.toString(),
                        address.city.city,
                        address.city.cap,
                        address.city.region,
                        address.city.province)
                )
            addressDao.addAddress(newAddress)
            println("indirizzo aggiunto nel db con successo")
            true
        }catch (e: Exception){
            e.printStackTrace()
            println("Impossibile aggiungere l'indirizzo")
            false
        }
    }

    // Elimina un indirizzo per ID
    suspend fun deleteAddressByCityId(address: AddressDTO): Boolean{
        return try{//da testare
            addressDao.deleteAddressByCityId(address.city.id.toString())
            true
        }catch (e: Exception){
            e.printStackTrace()
            false
        }
    }

    suspend fun deleteById(address: AddressDTO): Boolean{
        return try{
            //addressDao.deleteById(address.id.toLong()) da capire come gestire l'eliminazione
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
