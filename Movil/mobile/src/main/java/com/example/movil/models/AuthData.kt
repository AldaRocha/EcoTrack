package com.example.movil.models

class AuthData(
    val UsuarioId: Int,
    val Usuario: String,
    val PerfilId: Int,
    val TiempoSesion: Int,
    val TiempoInactividad: Int,
    val sesion: Sesion,
    val Bearer: String
) {
    constructor(data: Map<String, Any?>) : this(
        UsuarioId = (data["usuarioId"] as? Double)?.toInt() ?: 0,
        Usuario = data["usuario"] as? String ?: "",
        PerfilId = (data["perfilId"] as? Double)?.toInt() ?: 0,
        TiempoSesion = (data["tiempoSesion"] as? Double)?.toInt() ?: 0,
        TiempoInactividad = (data["tiempoInactividad"] as? Double)?.toInt() ?: 0,
        sesion = Sesion(data["sesion"]),
        Bearer = data["bearer"] as? String ?: ""
    )
}
