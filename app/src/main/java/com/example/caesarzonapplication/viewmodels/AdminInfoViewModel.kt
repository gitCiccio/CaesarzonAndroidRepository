package com.example.caesarzonapplication.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.caesarzonapplication.model.Ban
import com.example.caesarzonapplication.model.dto.ReportDTO
import com.example.caesarzonapplication.model.dto.SupportDTO
import com.example.caesarzonapplication.model.dto.UserFindDTO
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.io.IOException
import java.net.URL
import java.util.UUID

class AdminInfoViewModel : ViewModel() {

    val client = OkHttpClient()

    private val _searchResults = mutableStateListOf<UserFindDTO>()
    val searchResults: List<UserFindDTO> get() = _searchResults


    private val _reports = mutableStateListOf<ReportDTO>()
    val reports: List<ReportDTO> get() = _reports


    private val _supportRequests = mutableStateListOf<SupportDTO>()
    val supportRequests: List<SupportDTO> get() = _supportRequests

    private val _bans = mutableStateListOf<Ban>()
    val bans: List<Ban> get() = _bans
    //Rendere i numeri per le chiamate dinamici

    //da capire come fare l'init
    init {
        //searchUsers()
        //searchReports()
        //searchSupportRequests()
        //loadSupport()
        generateFakeReports()
    }

    fun searchUsers() {
        CoroutineScope(Dispatchers.IO).launch {
            val manageURL = URL("http://25.49.50.144:8090/user-api/users?str=0");
            val request = Request.Builder().url(manageURL)
                .addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()
            try {
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    return@launch
                }
                val responseBody = response.body?.string()
                val jsonResponse = JSONArray(responseBody)
                _searchResults.clear()
                for (i in 0 until jsonResponse.length()) {
                    val username = jsonResponse.getJSONObject(i).getString("username")
                    val profilePictureBase64 =
                        jsonResponse.getJSONObject(i).optString("profilePicture", "")
                    _searchResults.add(UserFindDTO(username, profilePictureBase64))
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun searchSpecifcUsers(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            var username = ""
            val manageURLUsername = URL("http://25.49.50.144:8090/user-api/users/$query")
            val request = Request.Builder().url(manageURLUsername)
                .addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()
            try {
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    return@launch
                }

                val responseBody = response.body?.string()

                try {
                    val jsonResponse = JSONArray(responseBody)
                    _searchResults.clear()
                    for (i in 0 until jsonResponse.length()) {
                        username = jsonResponse.getString(i)
                        val manageURLProfilePic =
                            URL("http://25.49.50.144:8090/user-api/image/$username")
                        val responseImage = client.newCall(
                            Request.Builder().url(manageURLProfilePic)
                                .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
                                .build()
                        ).execute()
                        if (!responseImage.isSuccessful)
                            return@launch
                        val responseBodyImage = responseImage.body?.string()
                        val profilePictureBase64 = responseBodyImage
                        profilePictureBase64?.let { UserFindDTO(username, it) }
                            ?.let { _searchResults.add(it) }
                    }
                } catch (e: org.json.JSONException) {
                    println("Errore nel parsing del JSON: ${e.message}")
                }


            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /*
    //dopo che prendi le richieste di supporto, quelle che vengono gestite devono essere eliminate chiamando la delete
    fun searchReports(){
        CoroutineScope(Dispatchers.IO).launch {
            val manageURL = URL("http://25.49.50.144:8090/notify-api/report?num=0");
            val request = Request.Builder().url(manageURL).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()
            try{
                val response = client.newCall(request).execute()
                println("valore della risposta: "+response.message)
                if(!response.isSuccessful)
                    return@launch
                val responseBody = response.body?.string()
                val jsonResponse = JSONArray(responseBody)
                _reports.clear()
                for(i in 0 until jsonResponse.length()){
                    val reportDate = jsonResponse.getJSONObject(i).getString("reportDate")
                    val reason = jsonResponse.getJSONObject(i).getString("reason")
                    val description = jsonResponse.getJSONObject(i).getString("description")
                    val usernameUser1 = jsonResponse.getJSONObject(i).getString("usernameUser1")
                    val usernameUser2 = jsonResponse.getJSONObject(i).getString("usernameUser2")
                    val reviewId = jsonResponse.getJSONObject(i).getString("reviewId")
                    _reports.add(ReportDTO(reportDate, reason, description, usernameUser1, usernameUser2, UUID.fromString(reviewId)))
                }


            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

 */

    fun searchSupportRequests() {
        CoroutineScope(Dispatchers.IO).launch {
            val manageURL = URL("http://25.49.50.144:8090/notify-api/support?num=0");
            val request = Request.Builder().url(manageURL)
                .addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

            try {
                val response = client.newCall(request).execute()
                if (!response.isSuccessful)
                    return@launch
                val responseBody = response.body?.string()
                val jsonResponse = JSONArray(responseBody)
                _supportRequests.clear()
                for (i in 0 until jsonResponse.length()) {
                    val username = jsonResponse.getJSONObject(i).getString("username")
                    val type = jsonResponse.getJSONObject(i).getString("type")
                    val subject = jsonResponse.getJSONObject(i).getString("subject")
                    val text = jsonResponse.getJSONObject(i).getString("text")
                    val localDate = jsonResponse.getJSONObject(i).optString("localDate", "")
                    _supportRequests.add(SupportDTO(username, type, subject, text, localDate))
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun deleteSupport(supportDTO: SupportDTO){
        _supportRequests.remove(supportDTO)
        println("Eliminato: "+supportDTO.username)
    }

    fun loadSupport() {
        _supportRequests.addAll(listOf(
            SupportDTO(
                username = "Bug_Man1",
                type = "Technical",
                subject = "Errore nel caricamento",
                text = "Errore nel caricamento dei dati",
                localDate = "2024-06-01"
            ),
            SupportDTO(
                username = "Bug_Man2",
                type = "Technical",
                subject = "Errore nel caricamento",
                text = "Errore nel caricamento dei dati",
                localDate = "2024-06-01"
            ),
            SupportDTO(
                username = "Bug_Man3",
                type = "Technical",
                subject = "Errore nel caricamento",
                text = "Errore nel caricamento dei dati",
                localDate = "2024-06-01"
            ),
            SupportDTO(
                username = "Bug_Man4",
                type = "Technical",
                subject = "Errore nel caricamento",
                text = "Errore nel caricamento dei dati",
                localDate = "2024-06-01"
            ),
            SupportDTO(
                username = "Bug_Man5",
                type = "Technical",
                subject = "Errore nel caricamento",
                text = "Errore nel caricamento dei dati",
                localDate = "2024-06-01"
            )
        ))
    }

    fun deleteReport(ReportDTO: ReportDTO){
        _reports.remove(ReportDTO)
    }

    fun generateFakeReports() {
        _reports.addAll(listOf(
            ReportDTO(
                reportDate = "2024-06-01",
                reason = "Inappropriate content",
                description = "The user posted inappropriate content.",
                usernameUser1 = "user1",
                usernameUser2 = "reportedUser1",
                reviewId = UUID.randomUUID()
            ),
            ReportDTO(
                reportDate = "2024-06-02",
                reason = "Harassment",
                description = "The user is harassing others.",
                usernameUser1 = "user2",
                usernameUser2 = "reportedUser2",
                reviewId = UUID.randomUUID()
            ),
            ReportDTO(
                reportDate = "2024-06-03",
                reason = "Spam",
                description = "The user is spamming the forum.",
                usernameUser1 = "user3",
                usernameUser2 = "reportedUser3",
                reviewId = UUID.randomUUID()
            ),
            ReportDTO(
                reportDate = "2024-06-03",
                reason = "Spam",
                description = "The user is spamming the forum.",
                usernameUser1 = "user3",
                usernameUser2 = "reportedUser3",
                reviewId = UUID.randomUUID()
            ),
            ReportDTO(
                reportDate = "2024-06-03",
                reason = "Spam",
                description = "The user is spamming the forum.",
                usernameUser1 = "user3",
                usernameUser2 = "reportedUser3",
                reviewId = UUID.randomUUID()
            )
        ))

    }
}