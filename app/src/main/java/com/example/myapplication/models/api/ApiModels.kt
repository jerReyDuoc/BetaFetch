package com.example.myapplication.models.api

import com.google.gson.annotations.SerializedName

// ==================== TMDB (Películas y Series) ====================

data class TMDBMovieResponse(
    val results: List<Movie>
)

data class Movie(
    val id: Int,
    val title: String,
    @SerializedName("overview")
    val description: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("vote_average")
    val rating: Double,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("original_language")
    val language: String
) {
    fun getPosterUrl(): String {
        return "https://image.tmdb.org/t/p/w500${posterPath ?: ""}"
    }

    fun isEnglish(): Boolean = language == "en"
}

data class TMDBTVResponse(
    val results: List<TVShow>
)

data class TVShow(
    val id: Int,
    val name: String,
    @SerializedName("overview")
    val description: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("vote_average")
    val rating: Double,
    @SerializedName("first_air_date")
    val firstAirDate: String,
    @SerializedName("original_language")
    val language: String
) {
    fun getPosterUrl(): String {
        return "https://image.tmdb.org/t/p/w500${posterPath ?: ""}"
    }

    fun isEnglish(): Boolean = language == "en"
}

// ==================== iTunes (Música) ====================

data class iTunesResponse(
    val results: List<Song>
)

data class Song(
    @SerializedName("trackId")
    val id: Int,
    @SerializedName("trackName")
    val title: String,
    @SerializedName("artistName")
    val artist: String,
    @SerializedName("collectionName")
    val album: String,
    @SerializedName("artworkUrl100")
    val artworkUrl: String?,
    @SerializedName("previewUrl")
    val previewUrl: String?,
    @SerializedName("primaryGenreName")
    val genre: String,
    @SerializedName("releaseDate")
    val releaseDate: String
) {
    fun getArtworkUrlLarge(): String {
        return artworkUrl?.replace("100x100", "600x600") ?: ""
    }
}

// ==================== Modelos de Preferencias del Usuario ====================

data class UserPreference(
    val id: Int = 0,
    val usuarioId: Int,
    val contentType: String, // "movie", "tv", "song"
    val contentId: Int,
    val contentTitle: String,
    val contentImage: String,
    val contentDescription: String,
    val dateAdded: String
)