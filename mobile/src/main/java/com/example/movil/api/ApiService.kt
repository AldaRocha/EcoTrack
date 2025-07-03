package com.example.movil.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import com.example.movil.models.RegistroModel

interface ApiService {
    @GET("Registros")
    fun obtenerUsuarios(): Call<List<RegistroModel>>

    @POST("Registros")
    fun registrarUsuario(@Body usuario: RegistroModel): Call<RegistroModel>
}
