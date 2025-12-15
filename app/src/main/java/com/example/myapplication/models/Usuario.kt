package com.example.myapplication.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.myapplication.R

class Usuario {
    // 1. Declaración de propiedades (sin inicialización)
    var id: Int
    var nombre: String
    var apellido: String
    var correo: String
    var pais: String
    var celular: String
    var fecNac: String
    var nomUsuario: String
    var contrasena: String


    constructor(id: Int, nombre: String, apellido: String, correo: String, pais: String, celular: String, fecNac: String, nomUsuario: String, contrasena: String) {
        this.id = id
        this.nombre = nombre
        this.apellido = apellido
        this.correo = correo
        this.pais = pais
        this.celular = celular
        this.fecNac = fecNac
        this.nomUsuario = nomUsuario
        this.contrasena = contrasena
    }

    fun imprimirDatos() {
        println("Usuario: $nomUsuario, Nombre: $nombre $apellido, Correo: $correo")
    }

    fun validarUsuario(nomUser: String, pass: String): Boolean {
        if (nomUser.equals(nomUsuario) && pass.equals(contrasena)) {
            return true
            }
        else { return false }
    }
}

