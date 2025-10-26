package com.example.myapplication.models

class Usuario {
    // 1. Declaración de propiedades (sin inicialización)
    var nombre: String
    var apellido: String
    var correo: String
    var celular: String
    var fecNac: String
    var nomUsuario: String
    var contrasena: String
    var pais: String

    constructor(nombre: String, apellido: String, correo: String, celular: String, fecNac: String, nomUsuario: String, contrasena: String, pais: String) {
        this.nombre = nombre
        this.apellido = apellido
        this.correo = correo
        this.celular = celular
        this.fecNac = fecNac
        this.nomUsuario = nomUsuario
        this.contrasena = contrasena
        this.pais = pais
    }

    fun imprimirDatos() {
        println("Usuario: $nomUsuario, Nombre: $nombre $apellido, Correo: $correo")
    }
}