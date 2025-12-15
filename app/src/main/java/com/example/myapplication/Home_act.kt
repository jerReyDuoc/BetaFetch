package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Home_act : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
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
        val tvDailyTip = findViewById<TextView>(R.id.tv_daily_tip)

        // Cards de módulos de práctica (ahora son CardViews clickeables)
        val cardPronunciation = findViewById<androidx.cardview.widget.CardView>(R.id.card_pronunciation)
        val cardConversation = findViewById<androidx.cardview.widget.CardView>(R.id.card_conversation)
        val cardGrammar = findViewById<androidx.cardview.widget.CardView>(R.id.card_grammar)
        val cardContent = findViewById<androidx.cardview.widget.CardView>(R.id.card_content)

        // Botones de header
        val btn5 = findViewById<Button>(R.id.button5)  // Progress/Dashboard
        val btn6 = findViewById<Button>(R.id.button6)  // Logout
        val btn7 = findViewById<Button>(R.id.button7)  // Edit Profile
        val btn8 = findViewById<Button>(R.id.button8)  // My List

        // Configurar mensaje de bienvenida
        if (nombreUsuario != null && nombreUsuario.isNotEmpty()) {
            bienvenidaTextView.text = "Welcome back, $nombreUsuario!"
        } else {
            bienvenidaTextView.text = "Welcome back!"
        }

        // Configurar tip del día aleatorio
        configurarTipDelDia(tvDailyTip)

        // ==================== MÓDULOS DE PRÁCTICA ====================

        // Pronunciation Exercises
        cardPronunciation.setOnClickListener {
            val intent = Intent(this@Home_act, Pronunciation_act::class.java)
            startActivity(intent)
        }

        // Conversation Practice
        cardConversation.setOnClickListener {
            val intent = Intent(this@Home_act, Conversation_act::class.java)
            startActivity(intent)
        }

        // Grammar Correction
        cardGrammar.setOnClickListener {
            val intent = Intent(this@Home_act, Spinner_act::class.java)
            startActivity(intent)
        }

        // Content Preferences (Movies, Series, Music)
        cardContent.setOnClickListener {
            val intent = Intent(this@Home_act, Preferences_act::class.java)
            startActivity(intent)
        }

        // ==================== BOTONES DE HEADER ====================

        // Ver Dashboard/Progress
        btn5.setOnClickListener {
            val intent = Intent(this@Home_act, Dashboard_act::class.java)
            startActivity(intent)
        }

        // Cerrar Sesión
        btn6.setOnClickListener {
            cerrarSesion()
        }

        // Editar Perfil
        btn7.setOnClickListener {
            val intent = Intent(this@Home_act, EditProfile_act::class.java)
            startActivity(intent)
        }

        // Mi Lista
        btn8.setOnClickListener {
            val intent = Intent(this@Home_act, MyList_act::class.java)
            startActivity(intent)
        }
    }

    private fun configurarTipDelDia(tvTip: TextView) {
        val tips = listOf(
            "Practice at least 10 minutes daily to maintain your streak! 🔥",
            "Listening to music in English helps improve your pronunciation! 🎵",
            "Try watching a series episode without subtitles today! 📺",
            "Repeat new words out loud to remember them better! 🗣️",
            "Reading before bed improves vocabulary retention! 📚",
            "Set small daily goals to stay motivated! 🎯",
            "Don't be afraid to make mistakes - they help you learn! 💪",
            "Practice speaking with yourself in the mirror! 🪞",
            "Write a short diary entry in English every day! ✍️",
            "Challenge yourself with harder content each week! 🚀"
        )

        // Seleccionar tip basado en el día actual para que sea "consistente" durante el día
        val dayOfYear = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_YEAR)
        val tipIndex = dayOfYear % tips.size
        tvTip.text = tips[tipIndex]
    }

    private fun cerrarSesion() {
        // Mostrar confirmación
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to logout?")

        builder.setPositiveButton("Yes, Logout") { dialog, _ ->
            // Limpiar SharedPreferences
            val sharedPref = getSharedPreferences("FetchPrefs", MODE_PRIVATE)
            sharedPref.edit().clear().apply()

            // Volver a MainActivity
            val intent = Intent(this@Home_act, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()

            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }

    override fun onResume() {
        super.onResume()
        // Verificar sesión cada vez que vuelve a la pantalla
        verificarSesion()
    }

    private fun verificarSesion() {
        val sharedPref = getSharedPreferences("FetchPrefs", MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)

        if (!isLoggedIn) {
            // Si no está logueado, redirigir a MainActivity
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}