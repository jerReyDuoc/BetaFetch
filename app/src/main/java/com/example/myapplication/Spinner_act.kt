package com.example.myapplication

import Ejercicio
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.models.DBHelper

class Spinner_act : AppCompatActivity() {

    private lateinit var tvQuestion: TextView
    private lateinit var tvHint: TextView
    private lateinit var spinnerOptions: Spinner
    private lateinit var btnConfirm: Button
    private lateinit var btnSkip: Button
    private lateinit var tvFeedback: TextView
    private lateinit var tvExplanation: TextView
    private lateinit var cardFeedback: CardView
    private lateinit var imgFeedback: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvProgress: TextView
    private lateinit var dbHelper: DBHelper

    private var currentExerciseIndex = 0
    private var correctAnswers = 0
    private var hasAnswered = false
    private var usuarioId: Int = -1

    private val ejercicios = listOf(
        Ejercicio(1, "I _____ with my dog every morning", "play", listOf("play", "plays", "playing", "played")),
        Ejercicio(2, "She _____ to the gym three times a week", "goes", listOf("go", "goes", "going", "gone")),
        Ejercicio(3, "They _____ studying English for two years", "have been", listOf("are", "have been", "were", "has been")),
        Ejercicio(4, "Last night, I _____ a movie with my friends", "watched", listOf("watch", "watches", "watched", "watching")),
        Ejercicio(5, "If I _____ more time, I would travel the world", "had", listOf("have", "had", "has", "having")),
        Ejercicio(6, "The book _____ by millions of people", "was read", listOf("read", "was read", "is reading", "reads")),
        Ejercicio(7, "We _____ to the beach tomorrow if it's sunny", "will go", listOf("go", "will go", "went", "going")),
        Ejercicio(8, "He _____ his homework when I called him", "was doing", listOf("does", "did", "was doing", "is doing")),
        Ejercicio(9, "I wish I _____ speak five languages", "could", listOf("can", "could", "will", "would")),
        Ejercicio(10, "The project _____ by next Friday", "must be finished", listOf("finish", "finishes", "must be finished", "finishing"))
    )

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

        dbHelper = DBHelper(this)
        obtenerUsuarioId()
        initializeViews()
        loadExercise()
        setupClickListeners()
    }

    private fun obtenerUsuarioId() {
        val sharedPref = getSharedPreferences("FetchPrefs", Context.MODE_PRIVATE)
        usuarioId = sharedPref.getInt("userId", -1)

        if (usuarioId == -1) {
            Toast.makeText(this, "Error: Usuario no identificado", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun initializeViews() {
        tvQuestion = findViewById(R.id.tvQuestion)
        tvHint = findViewById(R.id.tvHint)
        spinnerOptions = findViewById(R.id.spinnerOptions)
        btnConfirm = findViewById(R.id.btnConfirm)
        btnSkip = findViewById(R.id.btnSkip)
        tvFeedback = findViewById(R.id.tvFeedback)
        tvExplanation = findViewById(R.id.tvExplanation)
        cardFeedback = findViewById(R.id.card_feedback)
        imgFeedback = findViewById(R.id.img_feedback)
        progressBar = findViewById(R.id.progress_bar_grammar)
        tvProgress = findViewById(R.id.tv_progress_grammar)

        progressBar.max = ejercicios.size
        cardFeedback.visibility = View.GONE
    }

    private fun loadExercise() {
        if (currentExerciseIndex >= ejercicios.size) {
            showFinalResults()
            return
        }

        val ejercicioActual = ejercicios[currentExerciseIndex]
        hasAnswered = false

        tvQuestion.text = ejercicioActual.problema
        progressBar.progress = currentExerciseIndex + 1
        tvProgress.text = "${currentExerciseIndex + 1} / ${ejercicios.size}"

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            ejercicioActual.alternativas
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerOptions.adapter = adapter

        cardFeedback.visibility = View.GONE
        btnConfirm.text = "Check Answer"
        btnConfirm.isEnabled = true
        cardFeedback.alpha = 0f
    }

    private fun setupClickListeners() {
        btnConfirm.setOnClickListener {
            if (!hasAnswered) {
                checkAnswer()
            } else {
                nextExercise()
            }
        }

        btnSkip.setOnClickListener {
            skipExercise()
        }
    }

    private fun checkAnswer() {
        val ejercicioActual = ejercicios[currentExerciseIndex]
        val selectedAnswer = spinnerOptions.selectedItem.toString()
        val isCorrect = ejercicioActual.validarSolucion(selectedAnswer)

        hasAnswered = true

        // GUARDAR PROGRESO EN BD
        if (usuarioId != -1) {
            dbHelper.actualizarProgresoGramatica(usuarioId, isCorrect)
        }

        cardFeedback.visibility = View.VISIBLE
        cardFeedback.animate()
            .alpha(1f)
            .setDuration(300)
            .start()

        if (isCorrect) {
            correctAnswers++
            showCorrectFeedback(ejercicioActual)
        } else {
            showIncorrectFeedback(ejercicioActual, selectedAnswer)
        }

        btnConfirm.text = "Next →"
        btnConfirm.animate()
            .scaleX(0.95f)
            .scaleY(0.95f)
            .setDuration(100)
            .withEndAction {
                btnConfirm.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .start()
            }
            .start()
    }

    private fun showCorrectFeedback(ejercicio: Ejercicio) {
        tvFeedback.text = "✅ Correct!"
        tvFeedback.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
        imgFeedback.setImageResource(android.R.drawable.checkbox_on_background)
        imgFeedback.setColorFilter(ContextCompat.getColor(this, android.R.color.holo_green_dark))
        tvExplanation.text = getExplanation(ejercicio, true)
        tvExplanation.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))
        cardFeedback.setCardBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_green_light))
    }

    private fun showIncorrectFeedback(ejercicio: Ejercicio, selectedAnswer: String) {
        tvFeedback.text = "❌ Incorrect"
        tvFeedback.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
        imgFeedback.setImageResource(android.R.drawable.ic_delete)
        imgFeedback.setColorFilter(ContextCompat.getColor(this, android.R.color.holo_red_dark))
        tvExplanation.text = "The correct answer is '${ejercicio.solucion}'. ${getExplanation(ejercicio, false)}"
        tvExplanation.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))
        cardFeedback.setCardBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_light))
    }

    private fun getExplanation(ejercicio: Ejercicio, isCorrect: Boolean): String {
        return when (ejercicio.id) {
            1 -> "Present simple uses the base form with 'I/you/we/they'."
            2 -> "Third person singular (she/he/it) adds 's' or 'es' in present simple."
            3 -> "'Have been' is used for present perfect continuous with plural subjects."
            4 -> "Past simple tense is used for completed actions in the past."
            5 -> "Second conditional uses 'had' in the if-clause for hypothetical situations."
            6 -> "Passive voice in past tense: 'was/were + past participle'."
            7 -> "Future simple uses 'will' for predictions and decisions made at the moment."
            8 -> "Past continuous shows an action in progress when another action interrupted it."
            9 -> "'Could' expresses ability in unreal or hypothetical situations."
            10 -> "Modal 'must' + 'be' + past participle for passive voice obligation."
            else -> "Great effort! Keep practicing to improve your grammar skills."
        }
    }

    private fun skipExercise() {
        Toast.makeText(this, "Skipped. Try to answer next time!", Toast.LENGTH_SHORT).show()
        nextExercise()
    }

    private fun nextExercise() {
        currentExerciseIndex++
        loadExercise()
    }

    private fun showFinalResults() {
        val percentage = (correctAnswers.toFloat() / ejercicios.size * 100).toInt()
        val message = when {
            percentage >= 90 -> "🎉 Excellent! You're a grammar expert!"
            percentage >= 70 -> "👏 Great job! Keep up the good work!"
            percentage >= 50 -> "👍 Good effort! Practice makes perfect!"
            else -> "💪 Keep practicing! You'll get better!"
        }

        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Exercise Complete!")
        builder.setMessage("$message\n\nYou got $correctAnswers out of ${ejercicios.size} correct.\nScore: $percentage%")

        builder.setPositiveButton("Try Again") { dialog, _ ->
            currentExerciseIndex = 0
            correctAnswers = 0
            loadExercise()
            dialog.dismiss()
        }

        builder.setNegativeButton("Exit") { dialog, _ ->
            dialog.dismiss()
            finish()
        }

        builder.setCancelable(false)
        builder.show()
    }
}