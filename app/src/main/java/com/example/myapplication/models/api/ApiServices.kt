package com.example.myapplication.models.api

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// ==================== TMDB Service (Películas y Series) ====================

interface TMDBService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<TMDBMovieResponse>

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("language") language: String = "en-US"
    ): Response<TMDBMovieResponse>

    @GET("tv/popular")
    suspend fun getPopularTVShows(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<TMDBTVResponse>

    @GET("search/tv")
    suspend fun searchTVShows(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("language") language: String = "en-US"
    ): Response<TMDBTVResponse>
}

object TMDBClient {
    private const val BASE_URL = "https://api.themoviedb.org/3/"
    // API Key gratuita de TMDB - Reemplaza con tu propia key de https://www.themoviedb.org/settings/api
    const val API_KEY = "52e90d22fcbe503b6e9eddfb7c286488" // Instrucciones abajo para obtenerla

    val service: TMDBService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TMDBService::class.java)
    }
}

// ==================== iTunes Service (Música) ====================

interface iTunesService {
    @GET("search")
    suspend fun searchSongs(
        @Query("term") term: String,
        @Query("media") media: String = "music",
        @Query("entity") entity: String = "song",
        @Query("limit") limit: Int = 50
    ): Response<iTunesResponse>

    @GET("search")
    suspend fun getTopSongs(
        @Query("term") term: String = "top songs",
        @Query("media") media: String = "music",
        @Query("entity") entity: String = "song",
        @Query("limit") limit: Int = 50
    ): Response<iTunesResponse>
}

object iTunesClient {
    private const val BASE_URL = "https://itunes.apple.com/"

    val service: iTunesService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(iTunesService::class.java)
    }
}

/*
INSTRUCCIONES PARA OBTENER TMDB API KEY (GRATIS):

1. Ve a https://www.themoviedb.org/
2. Crea una cuenta gratuita
3. Ve a Settings → API
4. Solicita una API Key (selecciona "Developer")
5. Copia la "API Key (v3 auth)"
6. Reemplaza "TU_API_KEY_AQUI" con tu key

Nota: iTunes API no requiere API key
*/