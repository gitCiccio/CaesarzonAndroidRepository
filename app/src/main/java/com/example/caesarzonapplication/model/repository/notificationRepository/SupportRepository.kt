package com.example.caesarzonapplication.model.repository.notificationRepository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.caesarzonapplication.model.dao.notificationDao.SupportDao
import com.example.caesarzonapplication.model.dto.SupportDTO
import com.example.caesarzonapplication.model.entities.notificationEntity.Support
import com.example.caesarzonapplication.model.utils.Converters
import java.time.LocalDate

class SupportRepository(private val supportDao: SupportDao) {

    suspend fun addSupport(support: SupportDTO): Boolean{
        return try {
            val supportEntity = Support(support.id, support.username,support.type, support.subject, support.text, Converters().toLocalDate(support.dateRequest))
            supportDao.insert(supportEntity)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getAllSupport(): List<SupportDTO> {
        return try{
            val supports = supportDao.getAllSupport()
            supports.map { support ->
                SupportDTO(support.support_id, support.username,support.type, support.subject, support.text, support.dateRequest.toString())
            }
        }catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }
    }


    fun getSupport(id: String): LiveData<SupportDTO> {
        return try {
            val support = supportDao.getById(id)
            val supportDTO = SupportDTO(support.support_id, support.username,support.type, support.subject, support.text, support.dateRequest.toString())
            MutableLiveData(supportDTO)
        } catch (e: Exception) {
            e.printStackTrace()
            MutableLiveData(null)
        }
    }


    suspend fun deleteSupportById(id: String): Boolean {
        return try {
            supportDao.deleteSupportById(id)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun deleteAllSupport(): Boolean {
        return try {
            supportDao.deleteAllSupport()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
