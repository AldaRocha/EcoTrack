package com.example.movil.services

import android.content.Context
import com.example.movil.models.UsuarioLogin
import com.google.gson.Gson
import androidx.core.content.edit

class StorageService(
    private val context: Context
){
    private val shared = context.getSharedPreferences("EcoTrack", Context.MODE_PRIVATE)

    fun SaveItem(
        name: String,
        obj: String
    ){
        shared.edit { putString(name, obj) }
    }

    fun GetItem(
        name: String
    ): String?{
        return shared.getString(name, null)
    }

    fun ClearSession(){
        shared.edit { clear() }
    }
}