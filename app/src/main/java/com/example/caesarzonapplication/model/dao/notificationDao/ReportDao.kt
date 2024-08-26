package com.example.caesarzonapplication.model.dao.notificationDao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.caesarzonapplication.model.entities.notificationEntity.Report

@Dao
interface ReportDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addReport(report: Report)

    @Query("SELECT COUNT(*) FROM segnala WHERE username_utente2 = :username AND id_recensione != :reviewId")
    suspend fun countByUsernameUser2AndReviewId(username: String, reviewId: String): Int

    @Query("DELETE FROM segnala WHERE id_recensione = :reviewId")
    suspend fun deleteByReviewId(reviewId: String)

    @Query("SELECT * FROM segnala WHERE id_recensione = :reviewId LIMIT 1")
    suspend fun findByReviewId(reviewId: String): Report?

    //da capire come gestirla
    //@Query("SELECT * FROM segnala WHERE username_utente1 = :usernameUser1 AND id_recensione = :reviewId LIMIT 1")
    //suspend fun findByUsernameUser1AndReviewId(usernameUser1: String, reviewId: UUID): Report?

    @Query("DELETE FROM segnala")
    fun deleteAllReports()

}