package com.example.caesarzonapplication.model.viewmodels.userViewmodels

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.compose.rememberNavController
import com.example.caesarzonapplication.R
import com.example.caesarzonapplication.model.dto.productDTOS.BuyDTO
import com.example.caesarzonapplication.model.dto.productDTOS.ChangeCartDTO
import com.example.caesarzonapplication.model.dto.productDTOS.PayPalDTO
import com.example.caesarzonapplication.model.dto.productDTOS.ProductCartDTO
import com.example.caesarzonapplication.model.dto.productDTOS.ProductCartWithImage
import com.example.caesarzonapplication.model.dto.productDTOS.SendProductCartDTO
import com.example.caesarzonapplication.model.dto.productDTOS.UnvailableDTO
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.basicToken
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import com.example.caesarzonapplication.model.utils.BitmapConverter
import com.example.caesarzonapplication.navigation.DetailsScreen
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class ShoppingCartViewModel(): ViewModel() {


    private val client = OkHttpClient()
    val gson = Gson()
    private val bitmapConverter = BitmapConverter()

    private val _productsInShoppingCart: MutableStateFlow<List<ProductCartWithImage>> = MutableStateFlow(emptyList())
    val productsInShoppingCart: StateFlow<List<ProductCartWithImage>> = _productsInShoppingCart

    private val _buyLaterProducts: MutableStateFlow<List<ProductCartWithImage>> = MutableStateFlow(emptyList())
    val buyLaterProducts: StateFlow<List<ProductCartWithImage>> = _buyLaterProducts

    private val _errorMessages: MutableStateFlow<String> = MutableStateFlow("")
    val errorMessages: StateFlow<String> = _errorMessages

    private val _total: MutableStateFlow<Double> = MutableStateFlow(0.0)
    val total: StateFlow<Double> = _total

    private var productCartId = ArrayList<String>()

    private var _showErrorDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var showErrorDialog: StateFlow<Boolean> = _showErrorDialog


    suspend fun loadProductImage(productId: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            val uuidProduct = UUID.fromString(productId)
            val manageURL = URL("http://25.49.50.144:8090/product-api/image/$uuidProduct")
            val request = Request.Builder()
                .url(manageURL)
                .addHeader("Authorization", "Bearer ${basicToken?.accessToken}")
                .build()

            try {
                val response = client.newCall(request).execute()
                if (!response.isSuccessful || response.body == null) {
                    return@withContext null
                }
                val imageByteArray = response.body?.byteStream()?.readBytes()

                if (imageByteArray != null) {
                    val convertedImage = bitmapConverter.converterByteArray2Bitmap(imageByteArray)
                    return@withContext convertedImage
                } else {
                    println("Response body is null")
                }

            } catch (e: Exception) {
                e.printStackTrace()
                println("Errore nel caricamento dell'immagine")
            }
            return@withContext null
        }
    }

    fun setShowErrorToTrue(){
        _showErrorDialog.value = true
    }

    fun getCart(){
        viewModelScope.launch {
            try{
                doGetCart()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun doGetCart(){
        val manageUrl = URL("http://25.49.50.144:8090/product-api/cart")
        val request =  Request.Builder().url(manageUrl).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()
        _productsInShoppingCart.value = emptyList()
        _buyLaterProducts.value = emptyList()
        withContext(Dispatchers.IO){
            try{
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                }

                println("Risposta dal server: $responseBody")
                val cart = object : TypeToken<List<ProductCartDTO>>() {}.type
                val products = gson.fromJson<List<ProductCartDTO>>(responseBody, cart)
                for(product in products){
                    val image = loadProductImage(product.id)
                    println("VALORE BUYLATER:" + product.buyLater)
                    if(product.buyLater)
                        _buyLaterProducts.value += ProductCartWithImage(product, image)
                    else
                        _productsInShoppingCart.value += ProductCartWithImage(product, image)

                    println(_buyLaterProducts.value)
                    println(_productsInShoppingCart.value)
                }
            }catch (e: Exception){
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
            }
        }
    }

    fun saveForLaterOrChangeQuantityAndSize(productId: String, action: Int, size: String, quantity: Int) {
        viewModelScope.launch {
            try {
                doSaveForLaterOrChangeQuantityAndSize(productId, action, size, quantity)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun doSaveForLaterOrChangeQuantityAndSize(productId: String, num: Int, size: String, quantity: Int) {
        val manageUrl = URL("http://25.49.50.144:8090/product-api/cart/product/${productId}?action=${num}")

        val changeDTO = ChangeCartDTO(quantity, size)
        val json = gson.toJson(changeDTO)
        val JSON = "application/json; charset=utf-8".toMediaType()
        val requestBody = json.toRequestBody(JSON)
        val request = Request.Builder().url(manageUrl).put(requestBody).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

        withContext(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                }

                println("Risposta dal server: $responseBody")

                // Solo se num è 0, sposta in "salva per dopo"
                if (num == 0) {
                    val productToMove = _productsInShoppingCart.value.find { it.product.id == productId }
                        ?: _buyLaterProducts.value.find { it.product.id == productId }

                    if (productToMove != null) {
                        if (_productsInShoppingCart.value.contains(productToMove)) {
                            _productsInShoppingCart.update { it - productToMove }
                            _buyLaterProducts.update {
                                it + productToMove.copy(
                                    product = productToMove.product.copy(
                                        quantity = quantity,
                                        size = size,
                                        buyLater = true  // Imposta buyLater a true quando sposti ai prodotti salvati per dopo
                                    )
                                )
                            }
                        } else if (_buyLaterProducts.value.contains(productToMove)) {
                            _buyLaterProducts.update { it - productToMove }
                            _productsInShoppingCart.update {
                                it + productToMove.copy(
                                    product = productToMove.product.copy(
                                        quantity = quantity,
                                        size = size,
                                        buyLater = false  // Imposta buyLater a false quando sposti al carrello
                                    )
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
            }
        }
    }

    fun addProductCart(productId: String, size: String?, quantity: Int){
        viewModelScope.launch {
            try{
                doAddProductCart(productId, size, quantity)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun doAddProductCart(productId: String, size: String?, quantity: Int){
        val manageUrl = URL("http://25.49.50.144:8090/product-api/cart")

        val prodDTO: SendProductCartDTO = if(size == null){
            SendProductCartDTO(productId, quantity, null )
        }else{
            SendProductCartDTO(productId, quantity, size )
        }

        val json = gson.toJson(prodDTO)
        val JSON = "application/json; charset=utf-8".toMediaType()
        val requestBody = json.toRequestBody(JSON)
        val request = Request.Builder().url(manageUrl).post(requestBody).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

        println("json: $json")
        withContext(Dispatchers.IO){
            try{
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                }

                println("risposta dal server: $responseBody")

            }catch (e: Exception){
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
            }
        }
    }


    fun deleteProductCart(productId: String){
        viewModelScope.launch {
            try{
                doDeleteProductCart(productId)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun doDeleteProductCart(productId: String){
        val manageUrl = URL("http://25.49.50.144:8090/product-api/cart/$productId")
        val request = Request.Builder().url(manageUrl).delete().addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

        withContext(Dispatchers.IO){
            try{

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                }

                println("ripsosta dal server: $responseBody")
                _productsInShoppingCart.value = _productsInShoppingCart.value.filterNot { it.product.id == productId }
                _buyLaterProducts.value = _productsInShoppingCart.value.filterNot { it.product.id == productId }
            }catch (e: Exception){
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
            }
        }
    }

    fun clearErrorMessages() {
        _errorMessages.value = ""
        _showErrorDialog.value = false
    }


    fun deleteCart(){
        viewModelScope.launch {
            try{
                doDeleteCart()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun doDeleteCart(){
        val manageUrl = URL("http://25.49.50.144:8090/product-api/cart")
        val request = Request.Builder().url(manageUrl).delete().addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

        withContext(Dispatchers.IO){
            try{

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                }

                println("ripsosta dal server: $responseBody")

            }catch (e: Exception){
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
            }
        }
    }

    fun checkAvailability(){
        viewModelScope.launch {
            try {
                doCheckAvailability()
            } catch (e: Exception) {
                e.printStackTrace()
                println("checkAvailability: Errore durante l'esecuzione di doCheckAvailability: ${e.message}") // Debug print
            }
        }
    }

    suspend fun doCheckAvailability() {
        val manageUrl = URL("http://25.49.50.144:8090/product-api/pre-order")

        for (product in _productsInShoppingCart.value) {
            productCartId.add(product.product.id)
        }

        val json = gson.toJson(productCartId)
        val JSON = "application/json; charset=utf-8".toMediaType()
        val requestBody = json.toRequestBody(JSON)
        val request = Request.Builder()
            .url(manageUrl)
            .post(requestBody)
            .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
            .build()

        withContext(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (response.isSuccessful && responseBody != null) {
                    val availability = object : TypeToken<List<UnvailableDTO>>() {}.type
                    val gsonAvailability: List<UnvailableDTO>? = gson.fromJson(responseBody, availability)

                    if (gsonAvailability != null) {
                        var errorMessage = ""
                        for (av in gsonAvailability) {
                            errorMessage += av.name + " "
                            for (a in av.availabilities) {
                                if (a.size != null) {
                                    errorMessage += a.size + " "
                                }
                                errorMessage += a.amount.toString() + "\n"
                            }
                        }
                        _errorMessages.value = errorMessage
                        setShowErrorToTrue()
                    } else {
                        println("doCheckAvailability: Tutti i prodotti sono disponibili") // Debug print
                    }
                } else {
                    println("doCheckAvailability: Chiamata fallita o disponibilità non disponibile. Codice di stato: ${response.code}") // Debug print
                }

                println("doCheckAvailability: Risposta dal server: $responseBody") // Debug print
                for (product in _productsInShoppingCart.value) {
                    _total.value += product.product.total-product.product.discountTotal
                    println("doCheckAvailability: Aggiungo il totale scontato del prodotto: ${product.product.discountTotal}") // Debug print
                }
            } catch (e: Exception) {
                e.printStackTrace()
                println("doCheckAvailability: Errore durante la chiamata: ${e.message}") // Debug print
            }
        }
    }

    fun purchase(
        addressID: String,
        cardID: String,
        payPal: Boolean,
        context: Context,
        navigateToSuccess: () -> Unit
    ){
        viewModelScope.launch {
            try {
                doPurchase(addressID, cardID, payPal, context, navigateToSuccess)
            }catch (e: Exception){
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
            }
        }
    }

    suspend fun doPurchase(addressID: String, cardID: String, paypal: Boolean, context: Context) {


        clearBuyDTO(context)
        val manageUrl = URL("http://25.49.50.144:8090/product-api/purchase?pay-method=$paypal&platform=false")
        val currentBuyDTO = BuyDTO(addressID, cardID, _total.value, productCartId)

        saveBuyDTO(context, currentBuyDTO)

        val json = gson.toJson(currentBuyDTO)
        val JSON = "application/json; charset=utf-8".toMediaType()
        val requestBody = json.toRequestBody(JSON)

        val request = Request.Builder().url(manageUrl).post(requestBody).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

        withContext(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()


                if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
                    println("Errore nella chiamata PayPal: ${response.code}")
                    return@withContext
                }

                if (paypal) {
                    withContext(Dispatchers.Main) {
                        openLinkInCustomTab(context, responseBody)
                    }
                }else{
                    if (responseBody == "Errore...") {
                        withContext(Dispatchers.Main) {
                            AlertDialog.Builder(context)
                                .setTitle("Errore")
                                .setMessage("Si è verificato un errore durante il pagamento. Riprova più tardi.")
                                .setPositiveButton("OK") { dialog, _ ->
                                    dialog.dismiss()
                                }
                                .show()
                        }
                }else {
                        navigateToSuccess()
                    }
                }

                println("Risposta dal server: $responseBody")

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun openLinkInCustomTab(context: Context, url: String) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(context, Uri.parse(url))
    }

    fun success(
        queryParams: String,
        context: Context
    ){
        viewModelScope.launch {
            try {
                doSuccess(queryParams, context)
            }catch (e: Exception){
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
            }
        }
    }

    suspend fun doSuccess(queryParams: String, context: Context) {

        val params = queryParams.split("&").associate {
            val (key, value) = it.split("=")
            key to value
        }

        val paymentId = params["paymentId"] ?: ""
        val token = params["token"] ?: ""
        val payerId = params["PayerID"] ?: ""

        val savedBuyDTO = getBuyDTO(context) ?: throw IllegalStateException("BuyDTO non può essere null")

        val payPalDTO = PayPalDTO(paymentId, token, payerId, savedBuyDTO)

        val manageUrl = URL("http://25.49.50.144:8090/product-api/success")

        val json = gson.toJson(payPalDTO)
        val JSON = "application/json; charset=utf-8".toMediaType()
        val requestBody = json.toRequestBody(JSON)

        val request = Request.Builder().url(manageUrl).post(requestBody).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

        withContext(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()


                if (!response.isSuccessful || responseBody == null || responseBody.isEmpty()) {
                    println("Errore nella chiamata PayPal: ${response.code}")
                    return@withContext
                }

                println("Risposta dal server: $responseBody")

            } catch (e: Exception) {
                e.printStackTrace()
                println("Errore nell'esecuzione della chiamata: ${e.message}")
            }
        }
    }

    fun saveBuyDTO(context: Context, buyDTO: BuyDTO) {
        val sharedPreferences = context.getSharedPreferences("buy_data", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(buyDTO)
        editor.putString("buy_dto", json)
        editor.apply()
    }

    fun getBuyDTO(context: Context): BuyDTO? {
        val sharedPreferences = context.getSharedPreferences("buy_data", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("buy_dto", null)
        return if (json != null) {
            gson.fromJson(json, BuyDTO::class.java)
        } else {
            null
        }
    }

    fun clearBuyDTO(context: Context) {
        val sharedPreferences = context.getSharedPreferences("buy_data", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("buy_dto")
        editor.apply()
    }

    fun resetAvailability(){
        viewModelScope.launch {
            try {
                doResetAvailability()
            } catch (e: Exception) {
                e.printStackTrace()
                println("checkAvailability: Errore durante l'esecuzione di doCheckAvailability: ${e.message}") // Debug print
            }
        }
    }

    suspend fun doResetAvailability() {
        val manageUrl = URL("http://25.49.50.144:8090/product-api/rollback/pre-order")

        for (product in _productsInShoppingCart.value) {
            productCartId.add(product.product.id)
        }

        val json = gson.toJson(productCartId)
        val JSON = "application/json; charset=utf-8".toMediaType()
        val requestBody = json.toRequestBody(JSON)
        val request = Request.Builder()
            .url(manageUrl)
            .post(requestBody)
            .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
            .build()

        withContext(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (response.isSuccessful && responseBody != null) {
                    val availability = object : TypeToken<List<UnvailableDTO>>() {}.type
                    val gsonAvailability: List<UnvailableDTO>? = gson.fromJson(responseBody, availability)

                    if (gsonAvailability != null) {
                        println("doCheckAvailability: Tutti i prodotti sono disponibili") // Debug print

                    } else {
                        println("doCheckAvailability: Tutti i prodotti sono disponibili") // Debug print
                    }
                } else {
                    println("doCheckAvailability: Chiamata fallita o disponibilità non disponibile. Codice di stato: ${response.code}") // Debug print
                }

                println("doCheckAvailability: Risposta dal server: $responseBody") // Debug print
                for (product in _productsInShoppingCart.value) {
                    _total.value += product.product.total-product.product.discountTotal
                    println("doCheckAvailability: Aggiungo il totale scontato del prodotto: ${product.product.discountTotal}") // Debug print
                }
            } catch (e: Exception) {
                e.printStackTrace()
                println("doCheckAvailability: Errore durante la chiamata: ${e.message}") // Debug print
            }
        }
    }

}