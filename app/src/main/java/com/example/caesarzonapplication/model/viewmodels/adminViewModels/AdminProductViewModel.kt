package com.example.caesarzonapplication.model.viewmodels.adminViewModels

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import com.example.caesarzonapplication.model.dto.productDTOS.SendProductDTO
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.URL
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.net.toFile
import com.example.caesarzonapplication.model.utils.BitmapConverter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream

class AdminProductViewModel {

    val client = OkHttpClient()
    val gson = Gson()

    val bitmapConverter = BitmapConverter()

    private val _productId: MutableStateFlow<String> = MutableStateFlow("")
    val productId: StateFlow<String> = _productId



    fun addProduct(productDTO: SendProductDTO, image: ImageBitmap, context: Context,
                   onContinueShopping: () -> Unit){
        val manageURL = URL("http://25.49.50.144:8090/product-api/product?new=true")
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
                println("Codice di stato: ${response.code}")
                println("Messaggio di risposta: ${response.message}")


                postProductImage(responseBody.toString(), image, context, onContinueShopping = onContinueShopping )
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    private suspend fun postProductImage(productId: String, imageBitmap: ImageBitmap, context: Context,
                                         onContinueShopping: () -> Unit): String? {

        val id = productId.trim('"')
        return withContext(Dispatchers.IO) {
            val manageURL = URL("http://25.49.50.144:8090/product-api/image/$id")
            println("URL: $manageURL")
            val bitmap = imageBitmap.asAndroidBitmap()

            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val byteArray = bitmapConverter.converterBitmap2ByteArray(bitmap)


            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "image.jpg", byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull(), 0, byteArray.size))
                .build()


            val request = Request.Builder()
                .url(manageURL)
                .put(requestBody)
                .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
                .build()

            try {
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    println("Problemi nel caricamento dell'immagine: ${response.message} code: ${response.code}")
                    return@withContext null
                }else{
                    withContext(Dispatchers.Main) {
                        AlertDialog.Builder(context)
                            .setTitle("Errore")
                            .setMessage("Prodotto aggiunto o modificato con successo!.")
                            .setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                                onContinueShopping()

                            }
                            .show()
                    }
                }

                response.body?.string()
            } catch (e: Exception) {
                e.printStackTrace()
                println("Errore nel caricamento dell'immagine")
                return@withContext null
            }
        }
    }

    fun deleteProduct(productID: String){
        println("id del prodtto: $productID")
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