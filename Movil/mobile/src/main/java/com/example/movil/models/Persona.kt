package com.example.movil.models

class Persona(
    val personaId: Int,
    val nombre: String,
    val apellidoPaterno: String,
    val apellidoMaterno: String,
    val celular: String,
    val fechaNacimiento: String,
    val email: String,
    val fechaRegistro: String,
    val paisId: Int,
    val pais: Pais
)