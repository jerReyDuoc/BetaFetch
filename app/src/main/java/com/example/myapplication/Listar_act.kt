package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.models.DBHelper
import com.example.myapplication.models.Usuario
import com.example.myapplication.models.UsuarioAdapter

private lateinit var listaDeUsuarios: MutableList<Usuario>
private lateinit var adaptador: UsuarioAdapter
private lateinit var dbHelper: DBHelper
private lateinit var listViewUsuarios: ListView
class Listar_act : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_listar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = DBHelper(this)
        listViewUsuarios = findViewById<ListView>(R.id.lista_usuarios)

        cargarDatos()

        listViewUsuarios.setOnItemLongClickListener { _, _, position, _ ->

            val usuarioAEliminar = listaDeUsuarios[position]

            mostrarDialogoConfirmacion(usuarioAEliminar, position)
            true
        }
    }

    private fun cargarDatos() {
        listaDeUsuarios = dbHelper.obtenerTodosLosUsuarios().toMutableList()
        adaptador = UsuarioAdapter(this, listaDeUsuarios)
        listViewUsuarios.adapter = adaptador
    }

    private fun mostrarDialogoConfirmacion(usuario: Usuario, position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Confirmar Eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar a ${usuario.nombre} ${usuario.apellido}?")
            .setPositiveButton("Sí, Eliminar") { dialog, which ->

                val filasEliminadas = dbHelper.eliminarUsuario(usuario.id)

                if (filasEliminadas > 0) {
                    listaDeUsuarios.removeAt(position)
                    adaptador.notifyDataSetChanged()
                    Toast.makeText(this, "Usuario eliminado correctamente.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al eliminar el usuario.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}