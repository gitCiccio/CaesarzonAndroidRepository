package com.example.caesarzonapplication.model.dao.userDao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.caesarzonapplication.model.entities.userEntity.CityData

@Dao
interface CityDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCityData(cityData: CityData)

    @Query("SELECT * FROM dati_comune")
    suspend fun getAllCityData(): List<CityData>

    @Query("SELECT * FROM dati_comune WHERE id = :id")
    suspend fun getCityDataById(id: Long): CityData?

    @Query("DELETE FROM dati_comune WHERE id = :id")
    suspend fun deleteCityDataById(id: Long)

    @Query("DELETE FROM dati_comune")
    fun deleteAllCityData()

}