package com.example.caesarzonapplication.model.repository.userRepository

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

    suspend fun getAllCityData(): List<CityData> {
        return try {
            val cityData = cityDataDao.getAllCityData()
            cityData
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getCityDataById(id: Long): CityData? {
        return try {
            val cityData = cityDataDao.getCityDataById(id)
            cityData
        } catch (e: Exception) {
            e.printStackTrace()
            null
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

