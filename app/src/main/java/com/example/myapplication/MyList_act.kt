package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.models.DBHelper
import com.google.android.material.tabs.TabLayout

class MyList_act : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var tabLayout: TabLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvEmpty: TextView

    private var currentTab = 0 // 0: Movies, 1: TV Shows, 2: Music

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = DBHelper(this)
        initializeViews()
        setupTabs()
        loadContent()
    }

    private fun initializeViews() {
        tabLayout = findViewById(R.id.tab_layout)
        recyclerView = findViewById(R.id.recycler_view_my_list)
        tvEmpty = findViewById(R.id.tv_empty)

        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("🎬 Movies"))
        tabLayout.addTab(tabLayout.newTab().setText("📺 TV Shows"))
        tabLayout.addTab(tabLayout.newTab().setText("🎵 Music"))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                currentTab = tab?.position ?: 0
                loadContent()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun loadContent() {
        when (currentTab) {
            0 -> loadMovies()
            1 -> loadTVShows()
            2 -> loadMusic()
        }
    }

    private fun loadMovies() {
        val peliculas = dbHelper.obtenerTodasLasPeliculas()

        if (peliculas.isEmpty()) {
            showEmpty("No movies in your list yet.\nAdd some from Content!")
        } else {
            hideEmpty()
            recyclerView.adapter = MyListMoviesAdapter(peliculas, dbHelper) {
                // Callback cuando se elimina una película
                loadMovies()
            }
        }
    }

    private fun loadTVShows() {
        val series = dbHelper.obtenerTodasLasSeries()

        if (series.isEmpty()) {
            showEmpty("No TV shows in your list yet.\nAdd some from Content!")
        } else {
            hideEmpty()
            recyclerView.adapter = MyListTVAdapter(series, dbHelper) {
                // Callback cuando se elimina una serie
                loadTVShows()
            }
        }
    }

    private fun loadMusic() {
        val canciones = dbHelper.obtenerTodasLasCanciones()

        if (canciones.isEmpty()) {
            showEmpty("No music in your list yet.\nAdd some from Content!")
        } else {
            hideEmpty()
            recyclerView.adapter = MyListMusicAdapter(canciones, dbHelper) {
                // Callback cuando se elimina una canción
                loadMusic()
            }
        }
    }

    private fun showEmpty(message: String) {
        tvEmpty.text = message
        tvEmpty.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun hideEmpty() {
        tvEmpty.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }
}