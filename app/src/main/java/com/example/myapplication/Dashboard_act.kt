package com.example.myapplication

import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.models.DBHelper
import com.example.myapplication.models.Logro

class Dashboard_act : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private var usuarioId: Int = -1

    // Views para estadísticas
    private lateinit var tvLevel: TextView
    private lateinit var tvPoints: TextView
    private lateinit var tvStreak: TextView
    private lateinit var tvMaxStreak: TextView
    private lateinit var progressBarLevel: ProgressBar
    private lateinit var tvProgressXP: TextView

    // Views para contadores de actividades
    private lateinit var tvGrammarCount: TextView
    private lateinit var tvGrammarCorrect: TextView
    private lateinit var tvPronunciationCount: TextView
    private lateinit var tvConversationCount: TextView

    // RecyclerView para logros
    private lateinit var recyclerViewLogros: RecyclerView
    private lateinit var tvNoLogros: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = DBHelper(this)
        obtenerUsuarioId()
        initializeViews()
        cargarDatos()
    }

    private fun obtenerUsuarioId() {
        val sharedPref = getSharedPreferences("FetchPrefs", MODE_PRIVATE)
        usuarioId = sharedPref.getInt("userId", -1)

        if (usuarioId == -1) {
            finish()
        }
    }

    private fun initializeViews() {
        // Estadísticas principales
        tvLevel = findViewById(R.id.tv_level)
        tvPoints = findViewById(R.id.tv_points)
        tvStreak = findViewById(R.id.tv_streak)
        tvMaxStreak = findViewById(R.id.tv_max_streak)
        progressBarLevel = findViewById(R.id.progress_bar_level)
        tvProgressXP = findViewById(R.id.tv_progress_xp)

        // Contadores de actividades
        tvGrammarCount = findViewById(R.id.tv_grammar_count)
        tvGrammarCorrect = findViewById(R.id.tv_grammar_correct)
        tvPronunciationCount = findViewById(R.id.tv_pronunciation_count)
        tvConversationCount = findViewById(R.id.tv_conversation_count)

        // Logros
        recyclerViewLogros = findViewById(R.id.recycler_view_logros)
        tvNoLogros = findViewById(R.id.tv_no_logros)

        recyclerViewLogros.layoutManager = GridLayoutManager(this, 2)
    }

    private fun cargarDatos() {
        val progreso = dbHelper.obtenerProgreso(usuarioId)

        if (progreso != null) {
            // Nivel y experiencia
            tvLevel.text = "Level ${progreso.nivelActual}"
            tvPoints.text = "${progreso.puntuacionTotal} pts"

            // Calcular XP para el siguiente nivel
            val xpActual = progreso.experiencia % 100
            val xpNecesario = 100
            progressBarLevel.max = xpNecesario
            progressBarLevel.progress = xpActual
            tvProgressXP.text = "$xpActual / $xpNecesario XP"

            // Rachas
            tvStreak.text = "${progreso.rachaActual} days"
            tvMaxStreak.text = "Best: ${progreso.rachaMaxima} days"

            // Actividades completadas
            tvGrammarCount.text = "${progreso.ejerciciosGramaticaCompletados}"

            // Calcular porcentaje de aciertos
            val porcentaje = if (progreso.ejerciciosGramaticaCompletados > 0) {
                (progreso.ejerciciosGramaticaCorrectos * 100.0 / progreso.ejerciciosGramaticaCompletados).toInt()
            } else {
                0
            }
            tvGrammarCorrect.text = "$porcentaje% correct"

            tvPronunciationCount.text = "${progreso.pronunciacionCompletada}"
            tvConversationCount.text = "${progreso.conversacionesCompletadas}"

            // Cargar logros
            cargarLogros()
        }
    }

    private fun cargarLogros() {
        val logrosDesbloqueados = dbHelper.obtenerLogrosDesbloqueados(usuarioId)

        if (logrosDesbloqueados.isEmpty()) {
            tvNoLogros.visibility = android.view.View.VISIBLE
            recyclerViewLogros.visibility = android.view.View.GONE
        } else {
            tvNoLogros.visibility = android.view.View.GONE
            recyclerViewLogros.visibility = android.view.View.VISIBLE
            recyclerViewLogros.adapter = LogrosAdapter(logrosDesbloqueados)
        }
    }
}

// Adapter para mostrar logros
class LogrosAdapter(private val logros: List<Logro>) :
    RecyclerView.Adapter<LogrosAdapter.LogroViewHolder>() {

    inner class LogroViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val imgIcon: TextView = view.findViewById(R.id.tv_logro_icon)
        val tvNombre: TextView = view.findViewById(R.id.tv_logro_nombre)
        val tvDescripcion: TextView = view.findViewById(R.id.tv_logro_descripcion)
        val card: CardView = view.findViewById(R.id.card_logro)
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): LogroViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_logro, parent, false)
        return LogroViewHolder(view)
    }

    override fun onBindViewHolder(holder: LogroViewHolder, position: Int) {
        val logro = logros[position]

        holder.imgIcon.text = logro.icono
        holder.tvNombre.text = logro.nombre
        holder.tvDescripcion.text = logro.descripcion

        // Animación de aparición
        holder.card.alpha = 0f
        holder.card.animate()
            .alpha(1f)
            .setDuration(300)
            .setStartDelay((position * 50).toLong())
            .start()
    }

    override fun getItemCount() = logros.size
}