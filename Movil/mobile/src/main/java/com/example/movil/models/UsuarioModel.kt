package com.example.movil.models

class UsuarioModel(
    val usuarioId: Int,
    val cuenta: String,
    val contrasena: String,
    val reiniciarContrasena: Byte,
    val terminos: Byte,
    val activo: Byte,
    val personaId: Int,
    val persona: Persona,
    val tipoUsuarioId: Int,
    val tipoUsuario: TipoUsuario?,
    val perfilId: Int
)