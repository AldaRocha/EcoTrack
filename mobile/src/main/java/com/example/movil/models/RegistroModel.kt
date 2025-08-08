package com.example.movil.models

import com.google.gson.annotations.SerializedName

data class RegistroModel(
    @SerializedName("IdRegistro")
    val idRegistro: Int = 0,

    @SerializedName("Nombre")
    val nombre: String,

    @SerializedName("ApellidoPaterno")
    val apellido_paterno: String,

    @SerializedName("ApellidoMaterno")
    val apellido_materno: String,

    @SerializedName("Telefono")
    val telefono: String,

    @SerializedName("FechaNacimiento")
    val fecha_nacimiento: String,

    @SerializedName("Pais")
    val pais: String,

    @SerializedName("Correo")
    val correo: String,

    @SerializedName("Contrasenia")
    val contrasenia: String,

    @SerializedName("ConfirmarContrasenia")
    val confirmar_contrasenia: String,

    @SerializedName("AceptarTerminos")
    val aceptar_terminos: Boolean
)

