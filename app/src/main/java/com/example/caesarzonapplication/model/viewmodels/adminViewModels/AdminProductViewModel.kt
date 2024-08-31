package com.example.caesarzonapplication.model.viewmodels.adminViewModels

import com.example.caesarzonapplication.model.dto.productDTOS.SendProductDTO
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.URL
import android.content.Context
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.InputStream

class AdminProductViewModel {

    val client = OkHttpClient()
    val gson = Gson()

    private val _productIds: MutableStateFlow<String> = MutableStateFlow("")
    val productIds: StateFlow<String> = _productIds

    fun addProduct(productDTO: SendProductDTO){
        val manageURL = URL("http://25.49.50.144:8090/product-api/product")
        val JSON = "application/json; charset=utf-8".toMediaType()
        val json = gson.toJson(productDTO)
        val requestBody = json.toRequestBody(JSON)

        val request = Request.Builder().url(manageURL).post(requestBody).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

        CoroutineScope(Dispatchers.IO).launch {
            try {

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                }

                println("Risposta dal server: ${responseBody.toString()}")
                println("Prodotto aggiunto con successo")
                _productIds.value += productDTO.id
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun getImage(){
        /*TODO*/
    }


    fun deleteProduct(productID: String){
        println("id del prodtto: "+productID)
        val manageURL = URL("http://25.49.50.144:8090/product-api/product?productID=$productID")

        val request = Request
            .Builder()
            .url(manageURL)
            .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
            .delete()
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = client.newCall(request).execute()

                println("response: ${response.message} ${response.code}")
                if (!response.isSuccessful) {
                    println("Errore nella cancellazione del prodotto: ${response.message}")
                    println("Body: ${response.body?.string()}") // Stampa il body della risposta per maggiori dettagli
                } else {
                    println("Prodotto eliminato con successo")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}