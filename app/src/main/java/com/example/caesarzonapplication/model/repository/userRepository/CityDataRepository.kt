package com.example.caesarzonapplication.model.repository.userRepository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.caesarzonapplication.model.dao.userDao.CityDataDao
import com.example.caesarzonapplication.model.entities.userEntity.CityData

class CityDataRepository(private val cityDataDao: CityDataDao) {

    suspend fun addCityData(cityData: CityData): Boolean {
        return try {
            cityDataDao.addCityData(cityData)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getAllCityData(): LiveData<List<CityData>> {
        return try {
            val cityData = cityDataDao.getAllCityData()
            cityData
        } catch (e: Exception) {
            e.printStackTrace()
            MutableLiveData(emptyList())
        }
    }

    suspend fun deleteCityDataById(id: Long): Boolean {
        return try {
            cityDataDao.deleteCityDataById(id)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun deleteAllCityData(): Boolean {
        return try {
            cityDataDao.deleteAllCityData()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}

