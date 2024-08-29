package com.example.caesarzonapplication.model.service

import androidx.compose.runtime.mutableStateOf
import com.example.caesarzonapplication.model.TokenResponse
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.PublicKey
import java.security.Signature
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

class KeycloakService {

    companion object {
        var myToken: TokenResponse? = null
        var basicToken: TokenResponse? = null
        var isAdmin = mutableStateOf(false)
        var logged = mutableStateOf(false)

    }

    val client = OkHttpClient()
    private val keycloakRealm = "CaesarRealm"
    private val keycloakUrl = "http://25.24.244.170:8080"

    suspend fun getAccessToken(username: String, password: String) {
        withContext(Dispatchers.IO) {
            try {
                val url = URL("http://25.24.244.170:8080/realms/CaesarRealm/protocol/openid-connect/token")
                val connection = url.openConnection() as HttpURLConnection

                connection.requestMethod = "POST"
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

                val postData = "username=${URLEncoder.encode(username, "UTF-8")}" +
                        "&password=${URLEncoder.encode(password, "UTF-8")}" +
                        "&grant_type=password" +
                        "&client_id=caesar-app"
                val outputStream = OutputStreamWriter(connection.outputStream)
                outputStream.write(postData)
                outputStream.flush()

                val responseCode = connection.responseCode
                val inputStream: InputStream = if (responseCode == HttpURLConnection.HTTP_OK) {
                    connection.inputStream
                } else {
                    connection.errorStream
                }

                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                reader.forEachLine { line -> response.append(line) }
                reader.close()
                connection.disconnect()

                val gson = Gson()
                myToken = gson.fromJson(response.toString(), TokenResponse::class.java)
                decodeToken(myToken!!.accessToken)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getBasicToken() {
        println("Sono nella funzione per prendere il basic token")
        CoroutineScope(Dispatchers.IO).launch {
            val manageUrl =
                "http://25.24.244.170:8080/realms/CaesarRealm/protocol/openid-connect/token"
            val passwordGuest = "Mascalzone1"

            // Encode the parameters
            val params = listOf(
                "client_id" to "caesar-app",
                "grant_type" to "password",
                "username" to "Guest",
                "password" to passwordGuest
            ).joinToString("&") { (key, value) ->
                "${URLEncoder.encode(key, StandardCharsets.UTF_8.name())}=${
                    URLEncoder.encode(
                        value,
                        StandardCharsets.UTF_8.name()
                    )
                }"
            }
            val requestBody =
                params.toRequestBody("application/x-www-form-urlencoded".toMediaType())
            val request = Request.Builder()
                .url(manageUrl)
                .post(requestBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build()
            try {
                val response = client.newCall(request).execute()
                println("Response code: ${response.code}")
                println("Response message: ${response.message}")

                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    println("Response body: $responseBody")

                    if (responseBody != null) {
                        val json = JSONObject(responseBody)
                        val accessToken = json.getString("access_token")
                        val refreshToken = json.getString("refresh_token")
                        basicToken = TokenResponse(accessToken, refreshToken)
                    } else {
                        println("Null response body")
                    }
                } else {
                    println("Error in the request: ${response.body?.string()}")
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun decodeToken(accessToken: String){

        try {
            // Ottieni la chiave pubblica da Keycloak
            val publicKey = fetchPublicKey()

            // Decodifica il token JWT
            val parts = accessToken.split(".")
            if (parts.size != 3) {
                throw IllegalArgumentException("Token JWT non valido")
            }

            val header = String(Base64.getUrlDecoder().decode(parts[0]))
            val payload = String(Base64.getUrlDecoder().decode(parts[1]))
            val signature = Base64.getUrlDecoder().decode(parts[2])

            println("Header: $header")
            println("Payload: $payload")

            // Verifica la firma del token JWT
            val data = "${parts[0]}.${parts[1]}".toByteArray()
            val signatureInstance = Signature.getInstance("SHA256withRSA")
            signatureInstance.initVerify(publicKey)
            signatureInstance.update(data)

            val isValid = signatureInstance.verify(signature)
            if (isValid) {
                println("Il token JWT è valido.")
                // Controlla se l'utente ha il ruolo di admin dal payload
                val mapper = ObjectMapper()
                val jsonPayload: JsonNode = mapper.readTree(payload)
                val roles = jsonPayload.get("realm_access").get("roles")
                if (roles.isArray && roles.any { it.asText() == "admin" }) {
                    println("L'utente è un admin.")
                    isAdmin.value = true
                } else {
                    println("L'utente non è un admin.")
                }
            } else {
                println("Il token JWT non è valido.")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            println("Errore durante la decodifica o verifica del token: ${e.message}")
        }

    }

    private fun fetchPublicKey(): PublicKey {

        val jwksUrl = "$keycloakUrl/realms/$keycloakRealm/protocol/openid-connect/certs"

        val request = Request.Builder().url(jwksUrl).build()
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) throw RuntimeException("Failed to fetch JWK set: ${response.message}")

        val responseBody = response.body?.string() ?: throw RuntimeException("Empty response")
        val mapper = ObjectMapper()
        val jwkSet: JsonNode = mapper.readTree(responseBody)
        val keys = jwkSet.get("keys")
        if (keys.isArray) {
            for (key in keys) {
                if (key.get("alg").asText() == "RS256") {
                    val x5c = key.get("x5c").get(0).asText()
                    val decodedKey = Base64.getDecoder().decode(x5c)
                    val spec = X509EncodedKeySpec(decodedKey)
                    val keyFactory = KeyFactory.getInstance("RSA")
                    return keyFactory.generatePublic(spec)
                }
            }
        }
        throw RuntimeException("No suitable RSA key found in JWKset")
    }
}
