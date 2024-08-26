package com.example.caesarzonapplication.model.dao.userDao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.caesarzonapplication.model.entities.userEntity.Address

@Dao
interface AddressDao {

    //Prendi gli indirizzi
    @Query("SELECT * FROM indirizzo")
    fun getAllAddreses(): LiveData<List<Address>>

    //Aggiungi l'indirizzo
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAddress(address: Address)

    //Elimina un indirizzo
    @Query("DELETE FROM indirizzo WHERE id_indirizzo = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM indirizzo WHERE id_dati_comune = :id")
    suspend fun deleteAddressByCityId(id: String)

    //Elimina tutti gli indirizzi
    @Query("DELETE FROM indirizzo")
    fun deleteAll()

}