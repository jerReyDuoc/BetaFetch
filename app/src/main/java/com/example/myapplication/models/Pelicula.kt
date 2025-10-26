#!/usr/bin/env kotlin
class Pelicula {
    // Propiedades
    var titulo: String
    var director: String
    var anio: Int
    var duracionMinutos: Int

    constructor(titulo: String, director: String, anio: Int, duracionMinutos: Int) {
        this.titulo = titulo
        this.director = director
        this.anio = anio
        this.duracionMinutos = duracionMinutos
    }


    fun obtenerDuracionHoras(): String {
        val horas = duracionMinutos / 60
        val minutos = duracionMinutos % 60
        return "La película '$titulo' dura $horas horas y $minutos minutos."
    }
}
