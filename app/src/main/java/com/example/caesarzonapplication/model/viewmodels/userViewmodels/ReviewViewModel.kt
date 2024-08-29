package com.example.caesarzonapplication.model.viewmodels.userViewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caesarzonapplication.model.dto.AddressDTO
import com.example.caesarzonapplication.model.dto.AverageDTO
import com.example.caesarzonapplication.model.dto.ReviewDTO
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.URL

class ReviewViewModel: ViewModel() {
    private val client = OkHttpClient()
    val gson = Gson()

    private val _reviews: MutableStateFlow<List<ReviewDTO>> = MutableStateFlow(emptyList())
    val reviews: StateFlow<List<ReviewDTO>> = _reviews

    private val _averageReview: MutableStateFlow<Double> = MutableStateFlow(0.0)
    val averageReview: StateFlow<Double> = _averageReview

    private val _reviewsScore: MutableStateFlow<List<Int>> = MutableStateFlow(emptyList())
    val reviewsScore: StateFlow<List<Int>> = _reviewsScore

    //Manca la media delle recensioni e lo score
    fun getAllProductReviews(productId: String){
        viewModelScope.launch {
            try{
                doGetAllProductReviews(productId)
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun doGetAllProductReviews(productId: String){
        val manageUrl = URL("http://25.49.50.144:8090/user-api/reviews?prod-id=$productId&str=${0}")
        val request = Request.Builder().url(manageUrl).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

        withContext(Dispatchers.IO){
            try{
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                    return@withContext
                }

                val reviewsList = object : TypeToken<List<ReviewDTO>>() {}.type
                _reviews.value = gson.fromJson(responseBody, reviewsList)

                println("Reviews recuperate con successo: ${_reviews.value.size}")
            }catch (e: Exception){
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
            }
        }
    }

    //Aggiunta delle recensioni
    fun addReview(review: ReviewDTO){
        viewModelScope.launch {
            try{
                doAddReview(review)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun doAddReview(review: ReviewDTO){
        val manageUrl = URL("http://25.49.50.144:8090/user-api/review")

        val JSON = "application/json; charset=utf-8".toMediaType()
        val json = gson.toJson(review)
        val requestBody = json.toRequestBody(JSON)

        val request = Request.Builder().url(manageUrl).post(requestBody).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

        withContext(Dispatchers.IO){
           try{
               val response = client.newCall(request).execute()
               val responseBody = response.body?.string()

               if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                   println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
               }

               println("Risposta dal server: $responseBody")
               _reviews.value.toMutableList().add(review)
               println("Recensione aggiunta con successo")
           }catch (e: Exception){
               e.printStackTrace()
               println("Errore durante la chiamata: ${e.message}")
           }
        }
    }

    //Eliminazione della recenzione
    fun deleteReview(review: ReviewDTO){
        viewModelScope.launch {
            try{
                doDeleteReview(review)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun doDeleteReview(review: ReviewDTO){
        val manageUrl = URL("http://25.49.50.144:8090/user-api/review?product-id=${review.id}")
        val request = Request.Builder().url(manageUrl).delete().addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

        withContext(Dispatchers.IO){
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                }

                println("Risposta dal server: $responseBody")
                _reviews.value.toMutableList().remove(review)
                println("Recensione aggiunta con successo")
            }catch (e: Exception){
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
            }
        }
    }

    //Media delle recensioni
    fun getAverageReview(productId: String){
        viewModelScope.launch {
            try{
                doGetAverageReview(productId)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun doGetAverageReview(productId: String){
        val manageUrl = URL("http://25.49.50.144:8090/user-api/reviews/average?prod-id=$productId")
        val request = Request.Builder().url(manageUrl).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

        withContext(Dispatchers.IO){
            try{
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                }

                val averageProd = object : TypeToken<AverageDTO>() {}.type
                _averageReview.value = gson.fromJson(responseBody, averageProd)
                println("Recensione aggiunta con successo")
            }catch (e: Exception){
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
            }
        }

    }

    fun getReviewsScore(productId: String){
        viewModelScope.launch {
            try{
                doGetReviwsScore(productId)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun doGetReviwsScore(productId: String){
        val manageUrl = URL("http://25.49.50.144:8090/user-api/reviews/score?prod-id=$productId")
        val request = Request.Builder().url(manageUrl).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

        withContext(Dispatchers.IO){
            try{
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                }

                val scoreReviewProd = object : TypeToken<List<Int>>() {}.type
                _reviewsScore.value = gson.fromJson(responseBody, scoreReviewProd)
                println("Recensione aggiunta con successo")
            }catch (e: Exception){
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
            }
        }
    }
}