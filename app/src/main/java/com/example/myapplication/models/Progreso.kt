package com.example.myapplication.models

data class Progreso(
    val id: Int = 0,
    val usuarioId: Int,
    val ejerciciosGramaticaCompletados: Int = 0,
    val ejerciciosGramaticaCorrectos: Int = 0,
    val pronunciacionCompletada: Int = 0,
    val conversacionesCompletadas: Int = 0,
    val puntuacionTotal: Int = 0,
    val nivelActual: Int = 1,
    val experiencia: Int = 0,
    val rachaActual: Int = 0,
    val rachaMaxima: Int = 0,
    val fechaUltimaActividad: String = "",
    val fechaCreacion: String = ""
)

data class ActividadDiaria(
    val id: Int = 0,
    val usuarioId: Int,
    val fecha: String,
    val ejerciciosCompletados: Int = 0,
    val tiempoEstudio: Int = 0, // minutos
    val puntuacionGanada: Int = 0
)

data class LogroUsuario(
    val id: Int = 0,
    val usuarioId: Int,
    val logroId: Int,
    val fechaDesbloqueo: String,
    val visto: Boolean = false
)

data class Logro(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val icono: String,
    val requisitoTipo: String, // "ejercicios", "racha", "puntos", "nivel"
    val requisitoValor: Int
)
