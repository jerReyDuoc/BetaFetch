package com.example.myapplication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale

class Conversation_act : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tvScenario: TextView
    private lateinit var tvContext: TextView
    private lateinit var scrollView: ScrollView
    private lateinit var chatContainer: LinearLayout
    private lateinit var etUserMessage: EditText
    private lateinit var btnSend: Button
    private lateinit var btnHint: Button
    private lateinit var tvHint: TextView
    private lateinit var cardHint: CardView

    private var tts: TextToSpeech? = null
    private var currentScenarioIndex = 0
    private var messageCount = 0
    private val MAX_MESSAGES_PER_SCENARIO = 6

    // Escenarios de conversación
    private val scenarios = listOf(
        ConversationScenario(
            title = "At a Restaurant",
            context = "You're ordering food at a restaurant. The waiter will help you.",
            initialMessage = "Good evening! Welcome to our restaurant. Would you like to see the menu?",
            hints = listOf(
                "Try: 'Yes, please. What do you recommend?'",
                "Say: 'I'd like to order the pasta, please'",
                "Ask: 'Could I have some water?'",
                "Say: 'Could I have the check, please?'"
            ),
            suggestions = listOf(
                "Yes, please",
                "What do you recommend?",
                "I'd like to order...",
                "Could I have...?"
            )
        ),
        ConversationScenario(
            title = "At the Airport",
            context = "You're checking in for your flight. Talk to the airport staff.",
            initialMessage = "Hello! Welcome to the check-in counter. May I see your passport and ticket?",
            hints = listOf(
                "Say: 'Here you go'",
                "Ask: 'Which gate is my flight?'",
                "Say: 'I have one suitcase to check in'",
                "Ask: 'What time is boarding?'"
            ),
            suggestions = listOf(
                "Here you go",
                "Which gate?",
                "I need to check a bag",
                "What time is boarding?"
            )
        ),
        ConversationScenario(
            title = "Job Interview",
            context = "You're in a job interview. Answer the interviewer's questions professionally.",
            initialMessage = "Good morning! Thank you for coming. Please, tell me about yourself.",
            hints = listOf(
                "Start: 'I have experience in...'",
                "Mention: 'My strengths are...'",
                "Ask: 'What are the main responsibilities?'",
                "Say: 'I'm very interested in this position'"
            ),
            suggestions = listOf(
                "I have experience...",
                "My strengths are...",
                "What responsibilities?",
                "I'm interested in..."
            )
        ),
        ConversationScenario(
            title = "Making New Friends",
            context = "You're at a social event meeting new people.",
            initialMessage = "Hi there! I haven't seen you here before. Are you new to the area?",
            hints = listOf(
                "Introduce yourself: 'Hi! I'm...'",
                "Say: 'I just moved here last month'",
                "Ask: 'What do you do for fun around here?'",
                "Suggest: 'We should hang out sometime!'"
            ),
            suggestions = listOf(
                "Hi! I'm...",
                "I just moved here",
                "What do you recommend?",
                "Let's hang out!"
            )
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_conversation)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeViews()
        setupTextToSpeech()
        loadScenario()
        setupClickListeners()
    }

    private fun initializeViews() {
        tvScenario = findViewById(R.id.tv_scenario)
        tvContext = findViewById(R.id.tv_context)
        scrollView = findViewById(R.id.scroll_view)
        chatContainer = findViewById(R.id.chat_container)
        etUserMessage = findViewById(R.id.et_user_message)
        btnSend = findViewById(R.id.btn_send)
        btnHint = findViewById(R.id.btn_hint)
        tvHint = findViewById(R.id.tv_hint)
        cardHint = findViewById(R.id.card_hint)

        cardHint.visibility = View.GONE
    }

    private fun setupTextToSpeech() {
        tts = TextToSpeech(this, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.setLanguage(Locale.US)
        }
    }

    private fun loadScenario() {
        val scenario = scenarios[currentScenarioIndex]
        tvScenario.text = scenario.title
        tvContext.text = scenario.context

        chatContainer.removeAllViews()
        messageCount = 0

        // Mensaje inicial del sistema
        addMessage(scenario.initialMessage, isUser = false, speak = true)

        cardHint.visibility = View.GONE
    }

    private fun setupClickListeners() {
        btnSend.setOnClickListener {
            sendMessage()
        }

        btnHint.setOnClickListener {
            showHint()
        }

        etUserMessage.setOnEditorActionListener { _, _, _ ->
            sendMessage()
            true
        }
    }

    private fun sendMessage() {
        val message = etUserMessage.text.toString().trim()

        if (message.isEmpty()) {
            Toast.makeText(this, "Please write a message", Toast.LENGTH_SHORT).show()
            return
        }

        // Agregar mensaje del usuario
        addMessage(message, isUser = true, speak = false)
        etUserMessage.text.clear()
        messageCount++

        // Respuesta automática después de un delay
        Handler(Looper.getMainLooper()).postDelayed({
            generateResponse(message)
        }, 1500)
    }

    private fun generateResponse(userMessage: String) {
        val scenario = scenarios[currentScenarioIndex]
        val response = when {
            messageCount >= MAX_MESSAGES_PER_SCENARIO -> {
                "Thank you for practicing! Let's try a different scenario."
            }
            currentScenarioIndex == 0 -> generateRestaurantResponse(userMessage)
            currentScenarioIndex == 1 -> generateAirportResponse(userMessage)
            currentScenarioIndex == 2 -> generateInterviewResponse(userMessage)
            else -> generateSocialResponse(userMessage)
        }

        addMessage(response, isUser = false, speak = true)

        // Si completó el escenario
        if (messageCount >= MAX_MESSAGES_PER_SCENARIO) {
            Handler(Looper.getMainLooper()).postDelayed({
                showCompletionDialog()
            }, 2000)
        }
    }

    private fun generateRestaurantResponse(message: String): String {
        val lowerMessage = message.lowercase()
        return when {
            lowerMessage.contains("yes") || lowerMessage.contains("menu") ->
                "Great! I recommend our special pasta and the grilled salmon. What would you like to order?"
            lowerMessage.contains("pasta") || lowerMessage.contains("salmon") || lowerMessage.contains("order") ->
                "Excellent choice! Would you like something to drink with that?"
            lowerMessage.contains("water") || lowerMessage.contains("drink") ->
                "Of course! I'll bring that right away. Anything else?"
            lowerMessage.contains("check") || lowerMessage.contains("bill") || lowerMessage.contains("pay") ->
                "Sure! I'll bring the check right away. Did you enjoy your meal?"
            else -> "Good! What else can I help you with today?"
        }
    }

    private fun generateAirportResponse(message: String): String {
        val lowerMessage = message.lowercase()
        return when {
            lowerMessage.contains("passport") || lowerMessage.contains("here") ->
                "Thank you! Everything looks good. Do you have any bags to check?"
            lowerMessage.contains("bag") || lowerMessage.contains("suitcase") || lowerMessage.contains("luggage") ->
                "Perfect! Please place your bag on the scale. Your flight is at gate B7."
            lowerMessage.contains("gate") ->
                "Gate B7. Boarding starts at 3:30 PM. Have a great flight!"
            lowerMessage.contains("time") || lowerMessage.contains("boarding") ->
                "Boarding begins at 3:30 PM, about 30 minutes before departure."
            else -> "Is there anything else I can help you with?"
        }
    }

    private fun generateInterviewResponse(message: String): String {
        val lowerMessage = message.lowercase()
        return when {
            lowerMessage.contains("experience") || lowerMessage.contains("worked") ->
                "That's impressive! What would you say are your greatest strengths?"
            lowerMessage.contains("strength") || lowerMessage.contains("good at") ->
                "Excellent! Now, why are you interested in working with our company?"
            lowerMessage.contains("interested") || lowerMessage.contains("company") || lowerMessage.contains("position") ->
                "Great to hear! Do you have any questions about the role or our company?"
            lowerMessage.contains("question") || lowerMessage.contains("responsibilities") ->
                "The main responsibilities include team collaboration, project management, and innovation. Does this align with your goals?"
            else -> "Could you elaborate on that?"
        }
    }

    private fun generateSocialResponse(message: String): String {
        val lowerMessage = message.lowercase()
        return when {
            lowerMessage.contains("hi") || lowerMessage.contains("hello") || lowerMessage.contains("i'm") ->
                "Nice to meet you! So, what brings you to this event?"
            lowerMessage.contains("moved") || lowerMessage.contains("new") ->
                "Welcome! How are you liking it so far?"
            lowerMessage.contains("fun") || lowerMessage.contains("do") || lowerMessage.contains("recommend") ->
                "There are great cafes downtown and a nice park nearby. Do you like outdoor activities?"
            lowerMessage.contains("hang out") || lowerMessage.contains("meet") || lowerMessage.contains("friend") ->
                "That sounds great! I'd love to. Let me give you my contact info!"
            else -> "That sounds interesting! Tell me more about it."
        }
    }

    private fun addMessage(text: String, isUser: Boolean, speak: Boolean) {
        val messageView = layoutInflater.inflate(
            if (isUser) R.layout.item_message_user else R.layout.item_message_assistant,
            chatContainer,
            false
        )

        val tvMessage = messageView.findViewById<TextView>(R.id.tv_message)
        val imgIcon = messageView.findViewById<ImageView>(R.id.img_icon)

        tvMessage.text = text

        if (!isUser && speak) {
            imgIcon.setOnClickListener {
                tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }

        chatContainer.addView(messageView)

        // Scroll automático al final
        scrollView.post {
            scrollView.fullScroll(View.FOCUS_DOWN)
        }

        // Hablar automáticamente si es mensaje del asistente
        if (!isUser && speak) {
            Handler(Looper.getMainLooper()).postDelayed({
                tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            }, 500)
        }
    }

    private fun showHint() {
        val scenario = scenarios[currentScenarioIndex]
        val hintIndex = minOf(messageCount / 2, scenario.hints.size - 1)

        tvHint.text = scenario.hints[hintIndex]
        cardHint.visibility = View.VISIBLE

        // Ocultar hint después de 5 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            cardHint.visibility = View.GONE
        }, 5000)
    }

    private fun showCompletionDialog() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("🎉 Scenario Complete!")
        builder.setMessage("Great job! You've completed this conversation scenario.\n\nWould you like to try another scenario?")

        builder.setPositiveButton("Next Scenario") { dialog, _ ->
            // GUARDAR PROGRESO
            val sharedPref = getSharedPreferences("FetchPrefs", android.content.Context.MODE_PRIVATE)
            val usuarioId = sharedPref.getInt("userId", -1)

            if (usuarioId != -1) {
                val dbHelper = com.example.myapplication.models.DBHelper(this)
                dbHelper.actualizarProgresoConversacion(usuarioId)
            }

            currentScenarioIndex++
            if (currentScenarioIndex >= scenarios.size) {
                Toast.makeText(this, "🎊 Congratulations! You've completed all scenarios!", Toast.LENGTH_LONG).show()
                finish()
            } else {
                loadScenario()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Exit") { dialog, _ ->
            dialog.dismiss()
            finish()
        }

        builder.setCancelable(false)
        builder.show()
    }

    override fun onDestroy() {
        tts?.shutdown()
        super.onDestroy()
    }

    data class ConversationScenario(
        val title: String,
        val context: String,
        val initialMessage: String,
        val hints: List<String>,
        val suggestions: List<String>
    )
}