package com.example.movil.services

import android.content.Context
import android.util.Base64
import com.example.movil.interfaces.IRestService
import com.example.movil.models.AuthData
import com.example.movil.models.Request
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class RestService(
    private val context: Context
) {
    private val Url = "http://10.0.2.2:5063/api/"
    private val Version = "v1"
    private val Secret = "EcoTrack2025"
    private val gson = Gson()

    suspend fun Get(
        route: String,
        version: Boolean = true
    ): Map<String, Any?> = withContext(Dispatchers.IO){
        try{
            val headers = SetHeaders()
            val fullUrl = if (version) "$Version$route" else route

            val retrofit = Retrofit.Builder()
                .baseUrl(Url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val api = retrofit.create(IRestService::class.java)
            val response = api.Get(fullUrl, headers)

            if (response.isSuccessful){
                val res = response.body()
                return@withContext mapOf("message" to res?.message, "data" to Decode(res?.token))
            } else{
                return@withContext mapOf("error" to true, "message" to response.message())
            }
        } catch (ex: Exception){
            return@withContext mapOf("error" to true, "message" to ex.message)
        }
    }

    suspend fun Post(
        route: String,
        data: Any,
        version: Boolean = true,
        decode: Boolean = true
    ): Map<String, Any?> = withContext(Dispatchers.IO){
        try{
            val headers = SetHeaders()
            val token = Sign(data, Secret)
            val fullUrl = if (version) "$Url$Version$route" else "$Url$route"
            val request = Request(token)

            val retrofit = Retrofit.Builder()
                .baseUrl(Url)
                .client(client) // Eliminar
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val api = retrofit.create(IRestService::class.java)
            val response = api.Post(fullUrl, request, headers)

            if (response.isSuccessful){
                val res = response.body()
                if (decode && res?.token != null){
                    return@withContext mapOf("message" to res.message, "data" to Decode(res.token))
                } else{
                    return@withContext mapOf("message" to res?.message, "token" to res?.token)
                }
            } else{
                return@withContext mapOf("error" to true, "message" to response.message())
            }
        } catch (ex: Exception){
            return@withContext mapOf("error" to true, "message" to ex.message)
        }
    }

    suspend fun Put(
        route: String,
        data: Any,
        version: Boolean = true,
        decode: Boolean = true
    ): Map<String, Any?> = withContext(Dispatchers.IO){
        try{
            val headers = SetHeaders()
            val token = Sign(data, Secret)
            val fullUrl = if (version) "$Url$Version$route" else "$Url$route"
            val request = Request(token)

            val retrofit = Retrofit.Builder()
                .baseUrl(Url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val api = retrofit.create(IRestService::class.java)
            val response = api.Put(fullUrl, request, headers)

            if (response.isSuccessful){
                val res = response.body()
                if (decode && res?.token != null){
                    return@withContext mapOf("message" to res.message, "data" to Decode(res.token))
                } else{
                    return@withContext mapOf("message" to res?.message, "token" to res?.token)
                }
            } else{
                return@withContext mapOf("error" to true, "message" to response.message())
            }
        } catch (ex: Exception){
            return@withContext mapOf("error" to true, "message" to ex.message)
        }
    }

    suspend fun Delete(
        route: String,
        version: Boolean = true,
        decode: Boolean = true
    ): Map<String, Any?> = withContext(Dispatchers.IO){
        try{
            val headers = SetHeaders()
            val fullUrl = if (version) "$Url$Version$route" else "$Url$route"

            val retrofit = Retrofit.Builder()
                .baseUrl(Url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val api = retrofit.create(IRestService::class.java)
            val response = api.Delete(fullUrl, headers)

            if (response.isSuccessful){
                val res = response.body()
                if (decode && res?.token != null){
                    return@withContext mapOf("message" to res.message, "data" to Decode(res.token))
                } else{
                    return@withContext mapOf("message" to res?.message, "token" to res?.token)
                }
            } else{
                return@withContext mapOf("error" to true, "message" to response.message())
            }
        } catch (ex: Exception){
            return@withContext mapOf("error" to true, "message" to ex.message)
        }
    }

    private fun SetHeaders(): Map<String, String>{
        val shared = context.getSharedPreferences("AuthData", Context.MODE_PRIVATE)
        val token = shared.getString("ecotrack-oauth", null)

        val headers = mutableMapOf<String, String>()

        if (!token.isNullOrEmpty()){
            val authData = AuthData(Decode(token))
            headers["Authorization"] = "Bearer ${authData.Bearer}"
        }
        return headers
    }

    private fun Sign(data: Any, secret: String): String{
        val header = mapOf("alg" to "HS256", "typ" to "JWT")
        val stringifiedHeader = gson.toJson(header).toByteArray(Charsets.UTF_8)
        val stringifiedData = gson.toJson(data).toByteArray(Charsets.UTF_8)

        val encodedHeader = Base64Url(stringifiedHeader)
        val encodedData = Base64Url(stringifiedData)
        val token = "$encodedHeader.$encodedData"

        val hmacSha256 = Mac.getInstance("HmacSHA256")
        val secretKeySpec = SecretKeySpec(secret.toByteArray(Charsets.UTF_8), "HmacSHA256")
        hmacSha256.init(secretKeySpec)
        val signature = hmacSha256.doFinal(token.toByteArray(Charsets.UTF_8))
        val encodedSignature = Base64Url(signature)

        return "$token.$encodedSignature"
    }

    private fun Base64Url(source: ByteArray): String{
        return Base64.encodeToString(source, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
    }

    fun Decode(token: String?): Map<String, Any?>{
        if (token == null){
            return emptyMap()
        }
        val parts = token.split(".")
        if (parts.size < 2){
            return emptyMap()
        }
        val payload = Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
        val json = String(payload, Charsets.UTF_8)
        return gson.fromJson(json, Map::class.java) as Map<String, Any?>
    }

    // Eliminar
    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()
}