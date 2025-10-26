#!/usr/bin/env kotlin

class Serie {
    // Propiedades
    var titulo: String
    var temporadas: Int
    var episodiosPorTemporada: Int
    var plataforma: String


    constructor(titulo: String, temporadas: Int, episodiosPorTemporada: Int, plataforma: String) {
        this.titulo = titulo
        this.temporadas = temporadas
        this.episodiosPorTemporada = episodiosPorTemporada
        this.plataforma = plataforma
    }


    fun calcularTotalEpisodios(): Int {
        val total = temporadas * episodiosPorTemporada
        println("La serie '$titulo' tiene un total estimado de $total episodios.")
        return total
    }
}