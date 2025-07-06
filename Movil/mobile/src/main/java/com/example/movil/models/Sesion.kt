package com.example.movil.models

class Sesion(
    val sesionId: Int,
    val token: String?,
    val inicia: String,
    val finaliza: String?,
    val usuarioId: Int,
) {
    constructor(data: Any?) : this(
        sesionId = ((data as? Map<*, *>)?.get("sesionId") as? Double)?.toInt() ?: 0,
        token = (data as? Map<*, *>)?.get("token") as? String,
        inicia = (data as? Map<*, *>)?.get("inicia") as? String ?: "",
        finaliza = (data as? Map<*, *>)?.get("finaliza") as? String,
        usuarioId = ((data as? Map<*, *>)?.get("usuarioId") as? Double)?.toInt() ?: 0
    )

}
