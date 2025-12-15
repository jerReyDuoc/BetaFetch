package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.models.DBHelper
import com.example.myapplication.models.api.Movie
import com.example.myapplication.models.api.Song
import com.example.myapplication.models.api.TVShow
import com.example.myapplication.models.api.TMDBClient
import com.example.myapplication.models.api.iTunesClient
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch

class Preferences_act : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var dbHelper: DBHelper

    private var currentTab = 0 // 0: Movies, 1: TV Shows, 2: Music
    private var usuarioId: Int = -1

    private var moviesList = listOf<Movie>()
    private var tvShowsList = listOf<TVShow>()
    private var songsList = listOf<Song>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_preferences)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = DBHelper(this)
        obtenerUsuarioId()
        initializeViews()
        setupTabs()
        loadInitialContent()
    }

    private fun obtenerUsuarioId() {
        val sharedPref = getSharedPreferences("FetchPrefs", MODE_PRIVATE)
        usuarioId = sharedPref.getInt("userId", -1)
    }

    private fun initializeViews() {
        tabLayout = findViewById(R.id.tab_layout)
        recyclerView = findViewById(R.id.recycler_view_content)
        progressBar = findViewById(R.id.progress_bar)

        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("🎬 Movies"))
        tabLayout.addTab(tabLayout.newTab().setText("📺 TV Shows"))
        tabLayout.addTab(tabLayout.newTab().setText("🎵 Music"))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                currentTab = tab?.position ?: 0
                loadContentForTab()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun loadInitialContent() {
        loadContentForTab()
    }

    private fun loadContentForTab() {
        when (currentTab) {
            0 -> loadMovies()
            1 -> loadTVShows()
            2 -> loadMusic()
        }
    }

    private fun loadMovies() {
        showLoading(true)

        lifecycleScope.launch {
            try {
                val response = TMDBClient.service.getPopularMovies(TMDBClient.API_KEY)

                if (response.isSuccessful) {
                    moviesList = response.body()?.results?.filter { it.isEnglish() } ?: emptyList()

                    if (moviesList.isEmpty()) {
                        Toast.makeText(this@Preferences_act, "No English movies found", Toast.LENGTH_SHORT).show()
                    } else {
                        recyclerView.adapter = MoviesAdapter(moviesList, usuarioId, dbHelper)
                    }
                } else {
                    Toast.makeText(this@Preferences_act, "Error loading movies: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@Preferences_act, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            } finally {
                showLoading(false)
            }
        }
    }

    private fun loadTVShows() {
        showLoading(true)

        lifecycleScope.launch {
            try {
                val response = TMDBClient.service.getPopularTVShows(TMDBClient.API_KEY)

                if (response.isSuccessful) {
                    tvShowsList = response.body()?.results?.filter { it.isEnglish() } ?: emptyList()

                    if (tvShowsList.isEmpty()) {
                        Toast.makeText(this@Preferences_act, "No English TV shows found", Toast.LENGTH_SHORT).show()
                    } else {
                        recyclerView.adapter = TVShowsAdapter(tvShowsList, usuarioId, dbHelper)
                    }
                } else {
                    Toast.makeText(this@Preferences_act, "Error loading TV shows: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@Preferences_act, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            } finally {
                showLoading(false)
            }
        }
    }

    private fun loadMusic() {
        showLoading(true)

        lifecycleScope.launch {
            try {
                // Buscar canciones populares en inglés
                val response = iTunesClient.service.searchSongs("pop english songs")

                if (response.isSuccessful) {
                    songsList = response.body()?.results ?: emptyList()

                    if (songsList.isEmpty()) {
                        Toast.makeText(this@Preferences_act, "No songs found", Toast.LENGTH_SHORT).show()
                    } else {
                        recyclerView.adapter = SongsAdapter(songsList, usuarioId, dbHelper)
                    }
                } else {
                    Toast.makeText(this@Preferences_act, "Error loading music: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@Preferences_act, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            } finally {
                showLoading(false)
            }
        }
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        recyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }
}