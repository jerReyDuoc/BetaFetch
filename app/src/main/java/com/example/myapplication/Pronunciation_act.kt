package com.example.myapplication

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import java.util.Locale

class Pronunciation_act : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tvWord: TextView
    private lateinit var tvPhonetic: TextView
    private lateinit var tvDefinition: TextView
    private lateinit var btnListen: ImageButton
    private lateinit var btnRecord: ImageButton
    private lateinit var btnPlayRecording: ImageButton
    private lateinit var btnNext: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tvProgress: TextView
    private lateinit var tvFeedback: TextView

    private var tts: TextToSpeech? = null
    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private var audioFilePath: String? = null
    private var isRecording = false
    private var currentWordIndex = 0

    private val RECORD_AUDIO_PERMISSION_CODE = 100

    // Lista de palabras para practicar
    private val palabras = listOf(
        PronunciationWord("Restaurant", "/ˈrestərɑːnt/", "A place where people pay to sit and eat meals"),
        PronunciationWord("Beautiful", "/ˈbjuːtɪfl/", "Pleasing to the senses or mind"),
        PronunciationWord("Technology", "/tekˈnɒlədʒi/", "The application of scientific knowledge"),
        PronunciationWord("Comfortable", "/ˈkʌmftəbl/", "Providing physical ease and relaxation"),
        PronunciationWord("Environment", "/ɪnˈvaɪrənmənt/", "The surroundings or conditions in which someone lives"),
        PronunciationWord("Wednesday", "/ˈwenzdeɪ/", "The day of the week before Thursday"),
        PronunciationWord("February", "/ˈfebruəri/", "The second month of the year"),
        PronunciationWord("Pronunciation", "/prəˌnʌnsiˈeɪʃn/", "The way in which a word is pronounced")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pronunciation)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeViews()
        setupTextToSpeech()
        checkAudioPermission()
        loadWord()
        setupClickListeners()
    }

    private fun initializeViews() {
        tvWord = findViewById(R.id.tv_word)
        tvPhonetic = findViewById(R.id.tv_phonetic)
        tvDefinition = findViewById(R.id.tv_definition)
        btnListen = findViewById(R.id.btn_listen)
        btnRecord = findViewById(R.id.btn_record)
        btnPlayRecording = findViewById(R.id.btn_play_recording)
        btnNext = findViewById(R.id.btn_next)
        progressBar = findViewById(R.id.progress_bar)
        tvProgress = findViewById(R.id.tv_progress)
        tvFeedback = findViewById(R.id.tv_feedback)

        btnPlayRecording.isEnabled = false
    }

    private fun setupTextToSpeech() {
        tts = TextToSpeech(this, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Language not supported", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkAudioPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                RECORD_AUDIO_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RECORD_AUDIO_PERMISSION_CODE) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission required to record audio", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loadWord() {
        val word = palabras[currentWordIndex]
        tvWord.text = word.word
        tvPhonetic.text = word.phonetic
        tvDefinition.text = word.definition
        tvProgress.text = "${currentWordIndex + 1} / ${palabras.size}"
        progressBar.max = palabras.size
        progressBar.progress = currentWordIndex + 1
        tvFeedback.text = ""

        // Resetear estado de botones
        btnPlayRecording.isEnabled = false
        audioFilePath = null
    }

    private fun setupClickListeners() {
        btnListen.setOnClickListener {
            speakWord()
        }

        btnRecord.setOnClickListener {
            if (isRecording) {
                stopRecording()
            } else {
                startRecording()
            }
        }

        btnPlayRecording.setOnClickListener {
            playRecording()
        }

        btnNext.setOnClickListener {
            nextWord()
        }
    }

    private fun speakWord() {
        val word = palabras[currentWordIndex].word
        tts?.speak(word, TextToSpeech.QUEUE_FLUSH, null, null)

        // Animación visual
        btnListen.animate()
            .scaleX(0.9f)
            .scaleY(0.9f)
            .setDuration(100)
            .withEndAction {
                btnListen.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .start()
            }
            .start()
    }

    private fun startRecording() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            checkAudioPermission()
            return
        }

        try {
            audioFilePath = "${externalCacheDir?.absolutePath}/pronunciation_${System.currentTimeMillis()}.3gp"

            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                setOutputFile(audioFilePath)
                prepare()
                start()
            }

            isRecording = true
            btnRecord.setImageResource(android.R.drawable.ic_media_pause)
            tvFeedback.text = "🎤 Recording..."
            tvFeedback.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_light))

            // Auto-detener después de 3 segundos
            Handler(Looper.getMainLooper()).postDelayed({
                if (isRecording) {
                    stopRecording()
                }
            }, 3000)

        } catch (e: Exception) {
            Toast.makeText(this, "Error starting recording: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopRecording() {
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null
            isRecording = false
            btnRecord.setImageResource(android.R.drawable.ic_btn_speak_now)
            btnPlayRecording.isEnabled = true

            tvFeedback.text = "✅ Recording saved! Listen to your pronunciation"
            tvFeedback.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_light))

        } catch (e: Exception) {
            Toast.makeText(this, "Error stopping recording: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun playRecording() {
        if (audioFilePath == null) {
            Toast.makeText(this, "No recording available", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(audioFilePath)
                prepare()
                start()
                setOnCompletionListener {
                    tvFeedback.text = "Compare with the original pronunciation"
                }
            }

            tvFeedback.text = "🔊 Playing your recording..."
            tvFeedback.setTextColor(ContextCompat.getColor(this, android.R.color.holo_blue_light))

        } catch (e: Exception) {
            Toast.makeText(this, "Error playing recording: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun nextWord() {
        // GUARDAR PROGRESO
        val sharedPref = getSharedPreferences("FetchPrefs", android.content.Context.MODE_PRIVATE)
        val usuarioId = sharedPref.getInt("userId", -1)

        if (usuarioId != -1) {
            val dbHelper = com.example.myapplication.models.DBHelper(this)
            dbHelper.actualizarProgresoPronunciacion(usuarioId)
        }

        currentWordIndex++
        if (currentWordIndex >= palabras.size) {
            Toast.makeText(this, "🎉 Excellent work! You've completed all pronunciation exercises!", Toast.LENGTH_LONG).show()
            finish()
        } else {
            loadWord()
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    override fun onDestroy() {
        tts?.shutdown()
        mediaRecorder?.release()
        mediaPlayer?.release()

        // Limpiar archivos temporales
        audioFilePath?.let { path ->
            File(path).delete()
        }

        super.onDestroy()
    }

    data class PronunciationWord(
        val word: String,
        val phonetic: String,
        val definition: String
    )
}