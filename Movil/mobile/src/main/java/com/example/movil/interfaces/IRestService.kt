package com.example.movil.interfaces

import com.example.movil.models.Response
import com.example.movil.models.Request
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Url
import retrofit2.Response as RetrofitResponse

interface IRestService {
    @GET
    suspend fun Get(
        @Url url: String,
        @HeaderMap headers: Map<String, String>
    ): RetrofitResponse<Response>

    @POST
    suspend fun Post(
        @Url url: String,
        @Body body: Request,
        @HeaderMap headers: Map<String, String>
    ): RetrofitResponse<Response>

    @PUT
    suspend fun Put(
        @Url url: String,
        @Body body: Request,
        @HeaderMap headers: Map<String, String>
    ): RetrofitResponse<Response>

    @DELETE
    suspend fun Delete(
        @Url url: String,
        @HeaderMap headers: Map<String, String>
    ): RetrofitResponse<Response>
}