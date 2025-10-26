package com.example.myapplication

import Ejercicio
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

private lateinit var tvQuestion: TextView
private lateinit var tvFeedback: TextView
private lateinit var spinnerOptions: Spinner

class Spinner_act : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_spinner)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        val btn = findViewById<Button>(R.id.btnConfirm)


        val alternativas = listOf("Play", "Playes", "Plays")
        val ejercicioActual =
            Ejercicio(id = 1, problema = "I _____ with my dog", solucion = "Play", alternativas = alternativas)

        mostrarEjercicio(ejercicioActual)

        btn.setOnClickListener {
            verificarRespuesta(ejercicioActual)
        }

    }

    fun mostrarEjercicio(ejercicio: Ejercicio) {
        tvQuestion.text = ejercicio.problema

        // Adaptador para el Spinner
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            ejercicio.alternativas
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerOptions.adapter = adapter

        tvFeedback.visibility = View.GONE
    }

    private fun verificarRespuesta(ejercicio: Ejercicio) {
        val seleccion = spinnerOptions.selectedItem.toString()

        if (ejercicio.validarSolucion(seleccion)) {
            tvFeedback.setText("✅ ¡Correcto!")
            tvFeedback.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_light))
        } else {
            tvFeedback.setText("❌ Incorrecto. La respuesta correcta es: ${ejercicio.solucion}")
            tvFeedback.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_light))
        }

        tvFeedback.visibility = View.VISIBLE
    }
}