package com.example.movil.models

import android.content.ContentValues
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Usuario(
    val UsuarioId: Int,
    val Usuario: String,
    val Contrasena: String,
    val Token: String?
):Parcelable {
    fun MapForInsert(): ContentValues{
        return ContentValues().apply{
            put("Usuario", Usuario)
            put("Contrasena", Contrasena)
            put("Token", Token)
        }
    }

    fun MapForUpdate(): ContentValues{
        return ContentValues().apply{
            put("Usuario", Usuario)
            put("Contrasena", Contrasena)
            put("Token", Token)
        }
    }
}