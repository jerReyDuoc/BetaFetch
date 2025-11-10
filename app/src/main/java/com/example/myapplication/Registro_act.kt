package com.example.myapplication

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.models.DBHelper

class Registro_act : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val dbHelper = DBHelper(this)
        val spinner_pais: Spinner = findViewById(R.id.spinner_pais)

        val paises = listOf("Chile","Argentina","Peru")

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            paises
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_pais.adapter = adapter

        val nombre = findViewById<EditText>(R.id.id_nom)
        val apellido = findViewById<EditText>(R.id.id_ape)
        val correo = findViewById<EditText>(R.id.id_correo)
        val celular = findViewById<EditText>(R.id.id_cel)
        val fecnac = findViewById<EditText>(R.id.id_fec)
        val nomuser = findViewById<EditText>(R.id.id_nomUser)
        val contrasena = findViewById<EditText>(R.id.id_contra)
        val boton = findViewById<Button>(R.id.btn_reg)

        boton.setOnClickListener {
            val pais = spinner_pais.selectedItem.toString() // se obtiene la opcion seleccionada en el spinner :)
            val db = dbHelper.writableDatabase  // permisos de escritura a la database

            val valores = ContentValues().apply{  // aplicamos contentValues para añadir datos al arreglo de datos

                // campo de la database // valor que quiero guardar
                put("nombre", nombre.text.toString())
                put("apellido", apellido.text.toString())
                put("correo", correo.text.toString())
                put("pais", pais)
                put("celular", celular.text.toString())
                put("fecNac", fecnac.text.toString())
                put("nomUsuario", nomuser.text.toString())
                put("contrasena", contrasena.text.toString())
            }

            val resultado = db.insert("Usuarios", null, valores)
            db.close()

            if(resultado != -1L)  // validamos la insercion
            {
                Toast.makeText(this, "Se ha guardado !!!!", Toast.LENGTH_LONG).show()
            }

        }

    }
}