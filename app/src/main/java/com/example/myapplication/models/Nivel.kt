#!/usr/bin/env kotlin

class Nivel {
    // Propiedades
    var nombre: String
    var dificultad: Int
    var descripcion: String


    constructor(nombre: String, dificultad: Int, descripcion: String) {
        this.nombre = nombre
        this.dificultad = dificultad
        this.descripcion = descripcion
    }


    fun mostrarProgreso() {
        println("Estás en el nivel $nombre. Dificultad actual: $dificultad. Descripción: $descripcion")
    }
}