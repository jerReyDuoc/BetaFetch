package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


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

        btn.setOnClickListener {
            val user = etUser.text.toString()
            val pass = etPass.text.toString()

            if (user == "admin" && pass == "pass") {
                val intent = Intent(this@MainActivity, Home_act::class.java)
                startActivity(intent)

            } else {
                //Credenciales incorrectas
            }
        }

        btn2.setOnClickListener {
            val intent = Intent(this@MainActivity, Registro_act::class.java)
            startActivity(intent)
        }
    }
}