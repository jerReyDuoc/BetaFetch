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
        val btn3 = findViewById<Button>(R.id.button3)
        val btn4 = findViewById<Button>(R.id.button4)
        val btn1 = findViewById<Button>(R.id.button)  // Pronunciation Exercises
        val btn2 = findViewById<Button>(R.id.button2) // Conversation Practice

        if (nombreUsuario != null && nombreUsuario.isNotEmpty()) {
            bienvenidaTextView.text = "Welcome $nombreUsuario!"
        } else {
            bienvenidaTextView.text = "Welcome!"
        }

        btn1.setOnClickListener {
            val intent = Intent(this@Home_act, Pronunciation_act::class.java)
            startActivity(intent)
        }

        btn2.setOnClickListener {
            val intent = Intent(this@Home_act, Conversation_act::class.java)
            startActivity(intent)
        }

        btn3.setOnClickListener {
            val intent = Intent(this@Home_act, Spinner_act::class.java)
            startActivity(intent)
        }

        btn4.setOnClickListener {
            val intent = Intent(this@Home_act, Listar_act::class.java)
            startActivity(intent)
        }


    }
}