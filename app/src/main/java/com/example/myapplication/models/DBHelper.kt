package com.example.myapplication.models

import Ejercicio
import Nivel
import Pelicula
import Serie
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "DATABASE_NAME", null, DATABASE_VERSION) {

    companion object  {
        // --- TABLA USUARIOS ---
        private const val DATABASE_NAME = "DBFetch.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "Usuarios"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NOMBRE = "nombre"
        private const val COLUMN_APELLIDO = "apellido"
        private const val COLUMN_CORREO = "correo"
        private const val COLUMN_PAIS = "pais"
        private const val COLUMN_CELULAR = "celular"
        private const val COLUMN_FECNAC = "fecNac"
        private const val COLUMN_NOMUSUARIO = "nomUsuario"
        private const val COLUMN_CONTRASENA = "contrasena"
        // --- TABLA EJERCICIOS ---
        private const val TABLE_EJERCICIOS = "Ejercicios"
        private const val COLUMN_EJ_ID = "id"
        private const val COLUMN_EJ_PROBLEMA = "problema"
        private const val COLUMN_EJ_SOLUCION = "solucion"
        private const val COLUMN_EJ_ALTERNATIVAS = "alternativas" //  (JSON o separado por delimitador)

        // --- TABLA CANCIONES ---
        private const val TABLE_CANCIONES = "Canciones"
        private const val COLUMN_CA_TITULO = "titulo"
        private const val COLUMN_CA_ARTISTA = "artista"
        private const val COLUMN_CA_ALBUM = "album"
        private const val COLUMN_CA_GENERO = "genero"

        // --- TABLA NIVELES ---
        private const val TABLE_NIVELES = "Niveles"
        private const val COLUMN_NI_NOMBRE = "nombre"
        private const val COLUMN_NI_DIFICULTAD = "dificultad"
        private const val COLUMN_NI_DESCRIPCION = "descripcion"

        // --- TABLA PELICULAS ---
        private const val TABLE_PELICULAS = "Peliculas"
        private const val COLUMN_PE_TITULO = "titulo"
        private const val COLUMN_PE_DIRECTOR = "director"
        private const val COLUMN_PE_ANIO = "anio"
        private const val COLUMN_PE_DURACION = "duracionMinutos"

        // --- TABLA SERIES ---
        private const val TABLE_SERIES = "Series"
        private const val COLUMN_SE_TITULO = "titulo"
        private const val COLUMN_SE_TEMPORADAS = "temporadas"
        private const val COLUMN_SE_EPISODIOS_X_TEMP = "episodiosPorTemporada"
        private const val COLUMN_SE_PLATAFORMA = "plataforma"
    }
    override fun onCreate(db: SQLiteDatabase) {

        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NOMBRE TEXT,
                $COLUMN_APELLIDO TEXT,
                $COLUMN_CORREO TEXT,
                $COLUMN_PAIS TEXT,
                $COLUMN_CELULAR TEXT,
                $COLUMN_FECNAC TEXT,
                $COLUMN_NOMUSUARIO TEXT,
                $COLUMN_CONTRASENA TEXT
                )
        """.trimIndent()
        // Crear Tablas de SQL
        db.execSQL(
            createTable
        )

        val createTableEjercicios = """
            CREATE TABLE $TABLE_EJERCICIOS (
                $COLUMN_EJ_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_EJ_PROBLEMA TEXT,
                $COLUMN_EJ_SOLUCION TEXT,
                $COLUMN_EJ_ALTERNATIVAS TEXT -- (ej: "alt1|alt2|alt3")
            )
    """.trimIndent()
        db.execSQL(createTableEjercicios)

        // --- 2. Crear Tabla CANCIONES ---
        val createTableCanciones = """
            CREATE TABLE $TABLE_CANCIONES (
                $COLUMN_CA_TITULO TEXT,
                $COLUMN_CA_ARTISTA TEXT,
                $COLUMN_CA_ALBUM TEXT,
                $COLUMN_CA_GENERO TEXT
            )
    """.trimIndent()
        db.execSQL(createTableCanciones)

        // --- 3. Crear Tabla NIVELES ---
        val createTableNiveles = """
            CREATE TABLE $TABLE_NIVELES (
                $COLUMN_NI_NOMBRE TEXT,
                $COLUMN_NI_DIFICULTAD INTEGER,
                $COLUMN_NI_DESCRIPCION TEXT
            )
    """.trimIndent()
        db.execSQL(createTableNiveles)

        // --- 4. Crear Tabla PELICULAS ---
        val createTablePeliculas = """
            CREATE TABLE $TABLE_PELICULAS (
                $COLUMN_PE_TITULO TEXT,
                $COLUMN_PE_DIRECTOR TEXT,
                $COLUMN_PE_ANIO INTEGER,
                $COLUMN_PE_DURACION INTEGER
            )
    """.trimIndent()
        db.execSQL(createTablePeliculas)

        // --- 5. Crear Tabla SERIES ---
        val createTableSeries = """
            CREATE TABLE $TABLE_SERIES (
                $COLUMN_SE_TITULO TEXT,
                $COLUMN_SE_TEMPORADAS INTEGER,
                $COLUMN_SE_EPISODIOS_X_TEMP INTEGER,
                $COLUMN_SE_PLATAFORMA TEXT
            )
    """.trimIndent()
        db.execSQL(createTableSeries)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        db?.execSQL("DROP TABLE IF EXISTS Usuarios")
        onCreate(db)
    }


    fun insertarEjercicio(ejercicio: Ejercicio): Long {
        val db = this.writableDatabase

        // Convertir la lista de alternativas a un String para guardarla
        // Usamos '|' como delimitador. Necesitarás dividirla al leerla.
        val alternativasString = ejercicio.alternativas.joinToString("|")

        val valores = ContentValues().apply {
            put(COLUMN_EJ_PROBLEMA, ejercicio.problema)
            put(COLUMN_EJ_SOLUCION, ejercicio.solucion)
            put(COLUMN_EJ_ALTERNATIVAS, alternativasString)
        }

        val resultado = db.insert(TABLE_EJERCICIOS, null, valores)
        db.close()
        return resultado
    }


    fun insertarCancion(cancion: Cancion): Long {
        val db = this.writableDatabase
        val valores = ContentValues().apply {
            put(COLUMN_CA_TITULO, cancion.titulo)
            put(COLUMN_CA_ARTISTA, cancion.artista)
            put(COLUMN_CA_ALBUM, cancion.album)
            put(COLUMN_CA_GENERO, cancion.genero)
        }
        val resultado = db.insert(TABLE_CANCIONES, null, valores)
        db.close()
        return resultado
    }


    fun insertarNivel(nivel: Nivel): Long {
        val db = this.writableDatabase
        val valores = ContentValues().apply {
            put(COLUMN_NI_NOMBRE, nivel.nombre)
            put(COLUMN_NI_DIFICULTAD, nivel.dificultad)
            put(COLUMN_NI_DESCRIPCION, nivel.descripcion)
        }
        val resultado = db.insert(TABLE_NIVELES, null, valores)
        db.close()
        return resultado
    }


    fun insertarPelicula(pelicula: Pelicula): Long {
        val db = this.writableDatabase
        val valores = ContentValues().apply {
            put(COLUMN_PE_TITULO, pelicula.titulo)
            put(COLUMN_PE_DIRECTOR, pelicula.director)
            put(COLUMN_PE_ANIO, pelicula.anio)
            put(COLUMN_PE_DURACION, pelicula.duracionMinutos)
        }
        val resultado = db.insert(TABLE_PELICULAS, null, valores)
        db.close()
        return resultado
    }


    fun insertarSerie(serie: Serie): Long {
        val db = this.writableDatabase
        val valores = ContentValues().apply {
            put(COLUMN_SE_TITULO, serie.titulo)
            put(COLUMN_SE_TEMPORADAS, serie.temporadas)
            put(COLUMN_SE_EPISODIOS_X_TEMP, serie.episodiosPorTemporada)
            put(COLUMN_SE_PLATAFORMA, serie.plataforma)
        }
        val resultado = db.insert(TABLE_SERIES, null, valores)
        db.close()
        return resultado
    }

    fun obtenerTodosLosUsuarios(): List<Usuario> {
        val listaUsuarios = mutableListOf<Usuario>()

        // 1. Obtener una referencia a la DB para lectura
        val db = readableDatabase

        // 2. Definir la consulta (query)
        val query = "SELECT * FROM $TABLE_NAME"

        // 3. Ejecutar la consulta y obtener el Cursor
        val cursor = db.rawQuery(query, null)

        // 4. Iterar sobre los resultados
        if (cursor.moveToFirst()) {
            do {
                // Leer cada columna del registro actual
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE))
                val apellido = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_APELLIDO))
                val correo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CORREO))
                val pais = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAIS))
                val celular = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CELULAR))
                val fecNac = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECNAC))
                val nomUsuario = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMUSUARIO))
                val contrasena = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTRASENA))

                // 5. Crear el objeto y añadirlo a la lista
                val usuario = Usuario(
                    id, nombre, apellido, correo, pais, celular, fecNac, nomUsuario, contrasena
                )
                listaUsuarios.add(usuario)

            } while (cursor.moveToNext())
        }

        // 6. Cerrar el Cursor y la DB
        cursor.close()
        db.close()

        return listaUsuarios
    }

    fun eliminarUsuario(idUsuario: Int): Int {
        val db = writableDatabase

        val resultado = db.delete(
            TABLE_NAME,
            "$COLUMN_ID = ?",
            arrayOf(idUsuario.toString())
        )

        db.close()
        return resultado
    }

    fun buscarUsuario(user: String): Usuario? {
        val db = readableDatabase
        var usuario: Usuario? = null

        val selection = "$COLUMN_NOMUSUARIO = ?"
        val selectionArgs = arrayOf(user)

        val cursor = db.query(
            TABLE_NAME,
            null,
            selection,
            selectionArgs,
            null, // groupBy
            null, // having
            null  // orderBy
        )

        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE))
            val apellido = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_APELLIDO))
            val correo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CORREO))
            val pais = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAIS))
            val celular = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CELULAR))
            val fecNac = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECNAC))
            val nomUsuario = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMUSUARIO))
            val contrasena = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTRASENA))


            usuario = Usuario(
                id, nombre, apellido, correo, pais, celular, fecNac, nomUsuario, contrasena
            )
        }

        cursor.close()
        db.close()

        return usuario

    }

    fun verificarCorreoExiste(correoBuscado: String): Boolean {
        val db = readableDatabase

        val selection = "$COLUMN_CORREO = ?"
        val selectionArgs = arrayOf(correoBuscado)

        val cursor = db.query(
            TABLE_NAME,
            arrayOf("COUNT($COLUMN_ID)"), // Solo necesitamos saber cuántos hay
            selection,
            selectionArgs,
            null, null, null
        )

        var existe = false
        if (cursor.moveToFirst()) {
            if (cursor.getInt(0) > 0) {
                existe = true
            }
        }

        cursor.close()
        db.close()
        return existe
    }

    fun verificarNomUsuarioExiste(nomUsuarioBuscado: String): Boolean {
        val db = readableDatabase

        val selection = "$COLUMN_NOMUSUARIO = ?"
        val selectionArgs = arrayOf(nomUsuarioBuscado)

        val cursor = db.query(
            TABLE_NAME,
            arrayOf("COUNT($COLUMN_ID)"),
            selection,
            selectionArgs,
            null, null, null
        )

        var existe = false
        if (cursor.moveToFirst()) {
            if (cursor.getInt(0) > 0) {
                existe = true
            }
        }

        cursor.close()
        db.close()
        return existe
    }

}