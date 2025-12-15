package com.example.myapplication

import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.models.DBHelper
import com.example.myapplication.models.Usuario

class EditProfile_act : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private var usuarioId: Int = -1
    private var usuarioActual: Usuario? = null

    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var etCorreo: EditText
    private lateinit var spinnerPais: Spinner
    private lateinit var etCelular: EditText
    private lateinit var etFechaNac: EditText
    private lateinit var tvNombreUsuario: TextView
    private lateinit var tvFechaRegistro: TextView
    private lateinit var btnGuardar: Button
    private lateinit var btnCambiarPass: Button
    private lateinit var btnEliminarCuenta: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = DBHelper(this)
        obtenerUsuarioId()
        initializeViews()
        configurarSpinner()
        cargarDatosUsuario()
        setupClickListeners()
    }

    private fun obtenerUsuarioId() {
        val sharedPref = getSharedPreferences("FetchPrefs", MODE_PRIVATE)
        usuarioId = sharedPref.getInt("userId", -1)

        if (usuarioId == -1) {
            Toast.makeText(this, "Error: Usuario no identificado", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun initializeViews() {
        etNombre = findViewById(R.id.et_nombre)
        etApellido = findViewById(R.id.et_apellido)
        etCorreo = findViewById(R.id.et_correo)
        spinnerPais = findViewById(R.id.spinner_pais)
        etCelular = findViewById(R.id.et_celular)
        etFechaNac = findViewById(R.id.et_fecha_nac)
        tvNombreUsuario = findViewById(R.id.tv_nombre_usuario)
        tvFechaRegistro = findViewById(R.id.tv_fecha_registro)
        btnGuardar = findViewById(R.id.btn_guardar)
        btnCambiarPass = findViewById(R.id.btn_cambiar_password)
        btnEliminarCuenta = findViewById(R.id.btn_eliminar_cuenta)
    }

    private fun configurarSpinner() {
        val paises = listOf("Chile", "Argentina", "Peru", "Colombia", "Mexico", "España", "Estados Unidos")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, paises)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPais.adapter = adapter
    }

    private fun cargarDatosUsuario() {
        usuarioActual = dbHelper.buscarUsuarioPorId(usuarioId)

        usuarioActual?.let { usuario ->
            etNombre.setText(usuario.nombre)
            etApellido.setText(usuario.apellido)
            etCorreo.setText(usuario.correo)
            etCelular.setText(usuario.celular)
            etFechaNac.setText(usuario.fecNac)
            tvNombreUsuario.text = "@${usuario.nomUsuario}"

            // Seleccionar país en spinner
            val adapter = spinnerPais.adapter as ArrayAdapter<String>
            val position = adapter.getPosition(usuario.pais)
            if (position >= 0) {
                spinnerPais.setSelection(position)
            }

            // Mostrar fecha de creación del progreso como "fecha de registro"
            val progreso = dbHelper.obtenerProgreso(usuarioId)
            tvFechaRegistro.text = "Member since: ${progreso?.fechaCreacion ?: "Unknown"}"
        }
    }

    private fun setupClickListeners() {
        btnGuardar.setOnClickListener {
            guardarCambios()
        }

        btnCambiarPass.setOnClickListener {
            mostrarDialogoCambiarPassword()
        }

        btnEliminarCuenta.setOnClickListener {
            mostrarDialogoEliminarCuenta()
        }
    }

    private fun guardarCambios() {
        val nombre = etNombre.text.toString().trim()
        val apellido = etApellido.text.toString().trim()
        val correo = etCorreo.text.toString().trim()
        val pais = spinnerPais.selectedItem.toString()
        val celular = etCelular.text.toString().trim()
        val fechaNac = etFechaNac.text.toString().trim()

        // Validaciones
        if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (!esCorreoValido(correo)) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
            return
        }

        // Verificar si el correo ya existe (pero no es del usuario actual)
        if (correo != usuarioActual?.correo && dbHelper.verificarCorreoExiste(correo)) {
            Toast.makeText(this, "Email already in use", Toast.LENGTH_SHORT).show()
            return
        }

        // Actualizar en base de datos
        val db = dbHelper.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", nombre)
            put("apellido", apellido)
            put("correo", correo)
            put("pais", pais)
            put("celular", celular)
            put("fecNac", fechaNac)
        }

        val filasActualizadas = db.update(
            "Usuarios",
            valores,
            "id = ?",
            arrayOf(usuarioId.toString())
        )
        db.close()

        if (filasActualizadas > 0) {
            Toast.makeText(this, "Profile updated successfully! ✅", Toast.LENGTH_SHORT).show()

            // Actualizar SharedPreferences si cambió el nombre
            val sharedPref = getSharedPreferences("FetchPrefs", MODE_PRIVATE)
            sharedPref.edit().putString("username", usuarioActual?.nomUsuario).apply()

            finish()
        } else {
            Toast.makeText(this, "Error updating profile", Toast.LENGTH_SHORT).show()
        }
    }

    private fun mostrarDialogoCambiarPassword() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_change_password, null)
        val etCurrentPass = dialogView.findViewById<EditText>(R.id.et_current_password)
        val etNewPass = dialogView.findViewById<EditText>(R.id.et_new_password)
        val etConfirmPass = dialogView.findViewById<EditText>(R.id.et_confirm_password)

        AlertDialog.Builder(this)
            .setTitle("Change Password")
            .setView(dialogView)
            .setPositiveButton("Change") { dialog, _ ->
                val currentPass = etCurrentPass.text.toString()
                val newPass = etNewPass.text.toString()
                val confirmPass = etConfirmPass.text.toString()

                // Validaciones
                if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                    Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                if (currentPass != usuarioActual?.contrasena) {
                    Toast.makeText(this, "Current password is incorrect", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                if (newPass != confirmPass) {
                    Toast.makeText(this, "New passwords don't match", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                if (newPass.length < 4) {
                    Toast.makeText(this, "Password must be at least 4 characters", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                // Actualizar contraseña
                val db = dbHelper.writableDatabase
                val valores = ContentValues().apply {
                    put("contrasena", newPass)
                }

                val filasActualizadas = db.update(
                    "Usuarios",
                    valores,
                    "id = ?",
                    arrayOf(usuarioId.toString())
                )
                db.close()

                if (filasActualizadas > 0) {
                    Toast.makeText(this, "Password changed successfully! 🔒", Toast.LENGTH_SHORT).show()
                    usuarioActual?.contrasena = newPass
                }

                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun mostrarDialogoEliminarCuenta() {
        AlertDialog.Builder(this)
            .setTitle("⚠️ Delete Account")
            .setMessage("Are you sure you want to delete your account?\n\nThis action CANNOT be undone and you will lose:\n• All your progress\n• Your achievements\n• Your saved content\n• Your statistics")
            .setPositiveButton("Yes, Delete Forever") { dialog, _ ->
                confirmarEliminarCuenta()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun confirmarEliminarCuenta() {
        // Segundo diálogo de confirmación
        val dialogView = layoutInflater.inflate(R.layout.dialog_confirm_delete, null)
        val etPassword = dialogView.findViewById<EditText>(R.id.et_password_confirm)

        AlertDialog.Builder(this)
            .setTitle("Final Confirmation")
            .setMessage("Enter your password to confirm deletion:")
            .setView(dialogView)
            .setPositiveButton("Delete Account") { dialog, _ ->
                val password = etPassword.text.toString()

                if (password != usuarioActual?.contrasena) {
                    Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                // Eliminar todos los datos del usuario
                eliminarCuentaCompleta()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun eliminarCuentaCompleta() {
        val db = dbHelper.writableDatabase

        try {
            // Eliminar en orden (respetando foreign keys)
            db.delete("LogrosUsuario", "usuarioId = ?", arrayOf(usuarioId.toString()))
            db.delete("ActividadDiaria", "usuarioId = ?", arrayOf(usuarioId.toString()))
            db.delete("Progreso", "usuarioId = ?", arrayOf(usuarioId.toString()))
            db.delete("Usuarios", "id = ?", arrayOf(usuarioId.toString()))

            db.close()

            Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_LONG).show()

            // Cerrar sesión y volver al login
            val sharedPref = getSharedPreferences("FetchPrefs", MODE_PRIVATE)
            sharedPref.edit().clear().apply()

            val intent = android.content.Intent(this, MainActivity::class.java)
            intent.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()

        } catch (e: Exception) {
            Toast.makeText(this, "Error deleting account: ${e.message}", Toast.LENGTH_LONG).show()
            db.close()
        }
    }

    private fun esCorreoValido(correo: String): Boolean {
        val patronCorreo = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")
        return correo.matches(patronCorreo)
    }
}