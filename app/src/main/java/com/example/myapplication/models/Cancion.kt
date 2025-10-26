package com.example.myapplication.models

class Cancion {
     // Propiedades
    var titulo: String
    var artista: String
    var album: String
    var genero: String


    constructor(titulo: String, artista: String, album: String, genero: String) {
        this.titulo = titulo
        this.artista = artista
        this.album = album
        this.genero = genero
    }


    fun reproducir() {
        println("Reproduciendo '$titulo' de $artista, del álbum '$album'.")
    }
}