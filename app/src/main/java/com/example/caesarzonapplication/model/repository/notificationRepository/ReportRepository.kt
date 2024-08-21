package com.example.caesarzonapplication.model.repository.notificationRepository

import com.example.caesarzonapplication.model.dao.notificationDao.ReportDao
import com.example.caesarzonapplication.model.entities.notificationEntity.Report

class ReportRepository(private val reportDao: ReportDao) {

    suspend fun countByUsernameUser2AndReviewId(username: String, reviewId: Long): Int {
        return try {
            val count = reportDao.countByUsernameUser2AndReviewId(username, reviewId)
            count
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }

    suspend fun deleteByReviewId(reviewId: Long): Boolean {
        return try {
            reportDao.deleteByReviewId(reviewId)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun findByReviewId(reviewId: Long): Report? {
        return try {
            val report = reportDao.findByReviewId(reviewId)
            report
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun addReport(report: Report): Boolean {
        return try {
            reportDao.insert(report)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}

