package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.models.DBHelper
import com.example.myapplication.models.UsuarioAdapter

class Listar_act : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_listar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val listViewUsuarios = findViewById<ListView>(R.id.lista_usuarios)
        val dbHelper = DBHelper(this)
        val listaDeUsuarios = dbHelper.obtenerTodosLosUsuarios()
        val adaptador = UsuarioAdapter(this, listaDeUsuarios)

        listViewUsuarios.adapter = adaptador
    }
}