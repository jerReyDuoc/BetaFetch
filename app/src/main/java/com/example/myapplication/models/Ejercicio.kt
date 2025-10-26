#!/usr/bin/env kotlin

class Ejercicio {
    // Propiedades
    var id: Int
    var problema: String // El texto de la pregunta (ej: "Translate 'casa'")
    var alternativas: List<String>
    var solucion: String // La respuesta correcta (ej: "house")

    constructor(id: Int, problema: String, solucion: String, alternativas: List<String>) {
        this.id = id
        this.problema = problema
        this.solucion = solucion
        this.alternativas = alternativas
    }

    fun validarSolucion(respuestaUsuario: String): Boolean {
        // Usamos trim() para quitar espacios en blanco al inicio y al final
        // Usamos equals() con ignoreCase = true para que no importe si es mayúscula o minúscula
        return respuestaUsuario.trim().equals(solucion, ignoreCase = true)
    }
}