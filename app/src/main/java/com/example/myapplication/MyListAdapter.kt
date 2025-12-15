package com.example.myapplication

import Pelicula
import Serie
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.models.Cancion
import com.example.myapplication.models.DBHelper

// ==================== Movies Adapter ====================

class MyListMoviesAdapter(
    private var peliculas: List<Pelicula>,
    private val dbHelper: DBHelper,
    private val onDelete: () -> Unit
) : RecyclerView.Adapter<MyListMoviesAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tv_item_title)
        val tvInfo: TextView = view.findViewById(R.id.tv_item_info)
        val tvDescription: TextView = view.findViewById(R.id.tv_item_description)
        val btnDelete: Button = view.findViewById(R.id.btn_delete_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_my_list, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val pelicula = peliculas[position]

        holder.tvTitle.text = "🎬 ${pelicula.titulo}"
        holder.tvInfo.text = "${pelicula.anio} • ${pelicula.duracionMinutos} min"
        holder.tvDescription.text = "Director: ${pelicula.director}"

        holder.btnDelete.setOnClickListener {
            mostrarDialogoEliminar(holder, pelicula, position)
        }
    }

    private fun mostrarDialogoEliminar(holder: MovieViewHolder, pelicula: Pelicula, position: Int) {
        AlertDialog.Builder(holder.itemView.context)
            .setTitle("Remove Movie")
            .setMessage("Remove '${pelicula.titulo}' from your list?")
            .setPositiveButton("Remove") { dialog, _ ->
                val resultado = dbHelper.eliminarPelicula(pelicula.titulo)
                if (resultado > 0) {
                    Toast.makeText(holder.itemView.context, "Removed!", Toast.LENGTH_SHORT).show()
                    onDelete()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun getItemCount() = peliculas.size
}

// ==================== TV Shows Adapter ====================

class MyListTVAdapter(
    private var series: List<Serie>,
    private val dbHelper: DBHelper,
    private val onDelete: () -> Unit
) : RecyclerView.Adapter<MyListTVAdapter.TVViewHolder>() {

    inner class TVViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tv_item_title)
        val tvInfo: TextView = view.findViewById(R.id.tv_item_info)
        val tvDescription: TextView = view.findViewById(R.id.tv_item_description)
        val btnDelete: Button = view.findViewById(R.id.btn_delete_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TVViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_my_list, parent, false)
        return TVViewHolder(view)
    }

    override fun onBindViewHolder(holder: TVViewHolder, position: Int) {
        val serie = series[position]

        holder.tvTitle.text = "📺 ${serie.titulo}"
        holder.tvInfo.text = "${serie.temporadas} seasons • ${serie.episodiosPorTemporada} episodes/season"
        holder.tvDescription.text = "Platform: ${serie.plataforma}"

        holder.btnDelete.setOnClickListener {
            mostrarDialogoEliminar(holder, serie, position)
        }
    }

    private fun mostrarDialogoEliminar(holder: TVViewHolder, serie: Serie, position: Int) {
        AlertDialog.Builder(holder.itemView.context)
            .setTitle("Remove TV Show")
            .setMessage("Remove '${serie.titulo}' from your list?")
            .setPositiveButton("Remove") { dialog, _ ->
                val resultado = dbHelper.eliminarSerie(serie.titulo)
                if (resultado > 0) {
                    Toast.makeText(holder.itemView.context, "Removed!", Toast.LENGTH_SHORT).show()
                    onDelete()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun getItemCount() = series.size
}

// ==================== Music Adapter ====================

class MyListMusicAdapter(
    private var canciones: List<Cancion>,
    private val dbHelper: DBHelper,
    private val onDelete: () -> Unit
) : RecyclerView.Adapter<MyListMusicAdapter.MusicViewHolder>() {

    inner class MusicViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tv_item_title)
        val tvInfo: TextView = view.findViewById(R.id.tv_item_info)
        val tvDescription: TextView = view.findViewById(R.id.tv_item_description)
        val btnDelete: Button = view.findViewById(R.id.btn_delete_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_my_list, parent, false)
        return MusicViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val cancion = canciones[position]

        holder.tvTitle.text = "🎵 ${cancion.titulo}"
        holder.tvInfo.text = cancion.artista
        holder.tvDescription.text = "${cancion.album} • ${cancion.genero}"

        holder.btnDelete.setOnClickListener {
            mostrarDialogoEliminar(holder, cancion, position)
        }
    }

    private fun mostrarDialogoEliminar(holder: MusicViewHolder, cancion: Cancion, position: Int) {
        AlertDialog.Builder(holder.itemView.context)
            .setTitle("Remove Song")
            .setMessage("Remove '${cancion.titulo}' by ${cancion.artista} from your list?")
            .setPositiveButton("Remove") { dialog, _ ->
                val resultado = dbHelper.eliminarCancion(cancion.titulo, cancion.artista)
                if (resultado > 0) {
                    Toast.makeText(holder.itemView.context, "Removed!", Toast.LENGTH_SHORT).show()
                    onDelete()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun getItemCount() = canciones.size
}