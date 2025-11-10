package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.models.DBHelper


class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etUser = findViewById<EditText>(R.id.id_user)
        val etPass = findViewById<EditText>(R.id.id_pass)
        val btn = findViewById<Button>(R.id.btn_login)
        val btn2 = findViewById<Button>(R.id.btn_res)
        val dbHelper = DBHelper(this)

        btn.setOnClickListener {
            val user = etUser.text.toString().trim()
            val pass = etPass.text.toString().trim()

            //1. Valida que los campos tengan texto
            if (validarCampo(etUser) && validarCampo(etPass)) {

                //2. Para tener acceso por ahora, despues se elimina
                if (user == "admin" && pass == "pass") {
                    val intent = Intent(this@MainActivity, Home_act::class.java)
                    startActivity(intent)

                } else {

                    //3. Validacion de existencia de datos
                    val buscarUser = dbHelper.buscarUsuario(user)

                    if (buscarUser != null) {
                        val validacion = buscarUser.contrasena
                        //4. Valida que el la contraseña del usuario sea valida
                        if (validacion == pass) {
                            val intent = Intent(this@MainActivity, Home_act::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Toast.makeText(this, "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        btn2.setOnClickListener {
            val intent = Intent(this@MainActivity, Registro_act::class.java)
            startActivity(intent)
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