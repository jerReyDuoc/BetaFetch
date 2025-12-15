package com.example.myapplication

import Pelicula
import Serie
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.models.DBHelper
import com.example.myapplication.models.api.Movie
import com.example.myapplication.models.api.Song
import com.example.myapplication.models.api.TVShow

// ==================== Movies Adapter ====================

class MoviesAdapter(
    private val movies: List<Movie>,
    private val usuarioId: Int,
    private val dbHelper: DBHelper
) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgPoster: ImageView = view.findViewById(R.id.img_poster)
        val tvTitle: TextView = view.findViewById(R.id.tv_title)
        val tvRating: TextView = view.findViewById(R.id.tv_rating)
        val tvDate: TextView = view.findViewById(R.id.tv_date)
        val tvDescription: TextView = view.findViewById(R.id.tv_description)
        val btnAdd: Button = view.findViewById(R.id.btn_add)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]

        holder.tvTitle.text = movie.title
        holder.tvRating.text = String.format("%.1f", movie.rating)
        holder.tvDate.text = movie.releaseDate.take(4)
        holder.tvDescription.text = movie.description

        // Cargar imagen con Glide
        Glide.with(holder.itemView.context)
            .load(movie.getPosterUrl())
            .placeholder(android.R.color.darker_gray)
            .into(holder.imgPoster)

        holder.btnAdd.setOnClickListener {
            agregarPelicula(holder, movie)
        }
    }

    private fun agregarPelicula(holder: MovieViewHolder, movie: Movie) {
        val pelicula = Pelicula(
            titulo = movie.title,
            director = "Unknown", // TMDB API gratuita no incluye director
            anio = movie.releaseDate.take(4).toIntOrNull() ?: 0,
            duracionMinutos = 120 // Valor por defecto
        )

        val resultado = dbHelper.insertarPelicula(pelicula)

        if (resultado != -1L) {
            holder.btnAdd.text = "✓ Added"
            holder.btnAdd.isEnabled = false
            Toast.makeText(holder.itemView.context, "${movie.title} added!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = movies.size
}

// ==================== TV Shows Adapter ====================

class TVShowsAdapter(
    private val tvShows: List<TVShow>,
    private val usuarioId: Int,
    private val dbHelper: DBHelper
) : RecyclerView.Adapter<TVShowsAdapter.TVShowViewHolder>() {

    inner class TVShowViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgPoster: ImageView = view.findViewById(R.id.img_poster)
        val tvTitle: TextView = view.findViewById(R.id.tv_title)
        val tvRating: TextView = view.findViewById(R.id.tv_rating)
        val tvDate: TextView = view.findViewById(R.id.tv_date)
        val tvDescription: TextView = view.findViewById(R.id.tv_description)
        val btnAdd: Button = view.findViewById(R.id.btn_add)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TVShowViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return TVShowViewHolder(view)
    }

    override fun onBindViewHolder(holder: TVShowViewHolder, position: Int) {
        val show = tvShows[position]

        holder.tvTitle.text = show.name
        holder.tvRating.text = String.format("%.1f", show.rating)
        holder.tvDate.text = show.firstAirDate.take(4)
        holder.tvDescription.text = show.description

        Glide.with(holder.itemView.context)
            .load(show.getPosterUrl())
            .placeholder(android.R.color.darker_gray)
            .into(holder.imgPoster)

        holder.btnAdd.setOnClickListener {
            agregarSerie(holder, show)
        }
    }

    private fun agregarSerie(holder: TVShowViewHolder, show: TVShow) {
        val serie = Serie(
            titulo = show.name,
            temporadas = 1, // Valor por defecto
            episodiosPorTemporada = 10, // Valor por defecto
            plataforma = "Streaming"
        )

        val resultado = dbHelper.insertarSerie(serie)

        if (resultado != -1L) {
            holder.btnAdd.text = "✓ Added"
            holder.btnAdd.isEnabled = false
            Toast.makeText(holder.itemView.context, "${show.name} added!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = tvShows.size
}

// ==================== Songs Adapter ====================

class SongsAdapter(
    private val songs: List<Song>,
    private val usuarioId: Int,
    private val dbHelper: DBHelper
) : RecyclerView.Adapter<SongsAdapter.SongViewHolder>() {

    inner class SongViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgPoster: ImageView = view.findViewById(R.id.img_poster)
        val tvTitle: TextView = view.findViewById(R.id.tv_title)
        val tvRating: TextView = view.findViewById(R.id.tv_rating)
        val tvDate: TextView = view.findViewById(R.id.tv_date)
        val tvDescription: TextView = view.findViewById(R.id.tv_description)
        val btnAdd: Button = view.findViewById(R.id.btn_add)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]

        holder.tvTitle.text = song.title
        holder.tvRating.text = song.artist
        holder.tvDate.text = song.releaseDate.take(4)
        holder.tvDescription.text = "${song.album} • ${song.genre}"

        Glide.with(holder.itemView.context)
            .load(song.getArtworkUrlLarge())
            .placeholder(android.R.color.darker_gray)
            .into(holder.imgPoster)

        holder.btnAdd.setOnClickListener {
            agregarCancion(holder, song)
        }
    }

    private fun agregarCancion(holder: SongViewHolder, song: Song) {
        val cancion = com.example.myapplication.models.Cancion(
            titulo = song.title,
            artista = song.artist,
            album = song.album,
            genero = song.genre
        )

        val resultado = dbHelper.insertarCancion(cancion)

        if (resultado != -1L) {
            holder.btnAdd.text = "✓ Added"
            holder.btnAdd.isEnabled = false
            Toast.makeText(holder.itemView.context, "${song.title} added!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = songs.size
}