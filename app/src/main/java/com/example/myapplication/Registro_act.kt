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
            val nom = nombre.text.toString().trim()
            val ape = apellido.text.toString().trim()
            val email = correo.text.toString().trim()
            val cel = celular.text.toString().trim()
            val fecha = fecnac.text.toString().trim()
            val user = nomuser.text.toString().trim()
            val pass = contrasena.text.toString().trim()
            val pais = spinner_pais.selectedItem.toString() // se obtiene la opcion seleccionada en el spinner :)


            //1. Validacion de que todos los campos esten llenos
            if (!validarCampo(nombre) || !validarCampo(apellido) || !validarCampo(correo) ||
                !validarCampo(celular) || !validarCampo(fecnac) || !validarCampo(nomuser) ||
                !validarCampo(contrasena)) {

                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // Detiene la ejecución si hay campos vacíos
            }

            //2. Verifica que el correo no exista
            if (dbHelper.verificarCorreoExiste(email)) {
                Toast.makeText(this, "Error: El correo electrónico ya está registrado.", Toast.LENGTH_LONG).show()
                correo.error = "Correo ya en uso"
                return@setOnClickListener
            }

            //3. Verifica que el usuario no exista
            if (dbHelper.verificarNomUsuarioExiste(user)) {
                Toast.makeText(this, "Error: El nombre de usuario ya está en uso.", Toast.LENGTH_LONG).show()
                nomuser.error = "Nombre de usuario no disponible"
                return@setOnClickListener
            }

            val db = dbHelper.writableDatabase  // permisos de escritura a la database

            val valores = ContentValues().apply{
                put("nombre", nom)
                put("apellido", ape)
                put("correo", email)
                put("pais", pais)
                put("celular", cel)
                put("fecNac", fecha)
                put("nomUsuario", user)
                put("contrasena", pass)
            }

            val resultado = db.insert("Usuarios", null, valores)
            db.close()

            if(resultado != -1L)  // validamos la insercion
            {
                Toast.makeText(this, "Se ha guardado !!!!", Toast.LENGTH_LONG).show()
                val intent = Intent(this@Registro_act, MainActivity::class.java)
                startActivity(intent)
            }

        }

    }

    fun validarCampo(editText: EditText): Boolean {
        if (editText.text.toString().trim().isEmpty()) {
            editText.error = "Este campo es requerido"
            return false
        }
        return true
    }
}