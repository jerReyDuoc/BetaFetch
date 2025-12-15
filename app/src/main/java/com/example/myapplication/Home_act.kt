package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Home_act : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bienvenidaTextView = findViewById<TextView>(R.id.text_bienvenida)
        val nombreUsuario = intent.getStringExtra("EXTRA_USERNAME")

        val btn1 = findViewById<Button>(R.id.button)   // Pronunciation
        val btn2 = findViewById<Button>(R.id.button2)  // Conversation
        val btn3 = findViewById<Button>(R.id.button3)  // Grammar
        val btn4 = findViewById<Button>(R.id.button4)  // Preferences
        val btn5 = findViewById<Button>(R.id.button5)  // Editar Perfil
        val btn6 = findViewById<Button>(R.id.button6)  // Cerrar Sesión

        if (nombreUsuario != null && nombreUsuario.isNotEmpty()) {
            bienvenidaTextView.text = "Welcome $nombreUsuario!"
        } else {
            bienvenidaTextView.text = "Welcome!"
        }

        // Pronunciation Exercises
        btn1.setOnClickListener {
            val intent = Intent(this@Home_act, Pronunciation_act::class.java)
            startActivity(intent)
        }

        // Conversation Practice
        btn2.setOnClickListener {
            val intent = Intent(this@Home_act, Conversation_act::class.java)
            startActivity(intent)
        }

        // Grammar Correction
        btn3.setOnClickListener {
            val intent = Intent(this@Home_act, Spinner_act::class.java)
            startActivity(intent)
        }

        // Preferences (Movies, Series, Music)
        btn4.setOnClickListener {
            val intent = Intent(this@Home_act, Preferences_act::class.java)
            startActivity(intent)
        }

        // Editar Perfil (TODO: implementar)
        btn5.setOnClickListener {
            // Por ahora solo un mensaje
            android.widget.Toast.makeText(this, "Coming soon!", android.widget.Toast.LENGTH_SHORT).show()
        }

        // Cerrar Sesión
        btn6.setOnClickListener {
            // Limpiar SharedPreferences
            val sharedPref = getSharedPreferences("FetchPrefs", MODE_PRIVATE)
            sharedPref.edit().clear().apply()

            // Volver a MainActivity
            val intent = Intent(this@Home_act, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}