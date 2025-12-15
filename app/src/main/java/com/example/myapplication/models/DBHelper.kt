package com.example.myapplication.models

import Ejercicio
import Nivel
import Pelicula
import Serie
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "DBFetch.db"
        private const val DATABASE_VERSION = 3 // Incrementado para nuevas tablas

        // TABLA USUARIOS
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

        // TABLA PROGRESO
        private const val TABLE_PROGRESO = "Progreso"
        private const val COLUMN_PR_ID = "id"
        private const val COLUMN_PR_USUARIO_ID = "usuarioId"
        private const val COLUMN_PR_GRAMATICA_COMPLETADOS = "ejerciciosGramaticaCompletados"
        private const val COLUMN_PR_GRAMATICA_CORRECTOS = "ejerciciosGramaticaCorrectos"
        private const val COLUMN_PR_PRONUNCIACION = "pronunciacionCompletada"
        private const val COLUMN_PR_CONVERSACIONES = "conversacionesCompletadas"
        private const val COLUMN_PR_PUNTUACION = "puntuacionTotal"
        private const val COLUMN_PR_NIVEL = "nivelActual"
        private const val COLUMN_PR_EXPERIENCIA = "experiencia"
        private const val COLUMN_PR_RACHA_ACTUAL = "rachaActual"
        private const val COLUMN_PR_RACHA_MAXIMA = "rachaMaxima"
        private const val COLUMN_PR_ULTIMA_ACTIVIDAD = "fechaUltimaActividad"
        private const val COLUMN_PR_FECHA_CREACION = "fechaCreacion"

        // TABLA ACTIVIDAD DIARIA
        private const val TABLE_ACTIVIDAD = "ActividadDiaria"
        private const val COLUMN_AC_ID = "id"
        private const val COLUMN_AC_USUARIO_ID = "usuarioId"
        private const val COLUMN_AC_FECHA = "fecha"
        private const val COLUMN_AC_EJERCICIOS = "ejerciciosCompletados"
        private const val COLUMN_AC_TIEMPO = "tiempoEstudio"
        private const val COLUMN_AC_PUNTUACION = "puntuacionGanada"

        // TABLA LOGROS
        private const val TABLE_LOGROS = "Logros"
        private const val COLUMN_LO_ID = "id"
        private const val COLUMN_LO_NOMBRE = "nombre"
        private const val COLUMN_LO_DESCRIPCION = "descripcion"
        private const val COLUMN_LO_ICONO = "icono"
        private const val COLUMN_LO_TIPO = "requisitoTipo"
        private const val COLUMN_LO_VALOR = "requisitoValor"

        // TABLA LOGROS USUARIO
        private const val TABLE_LOGROS_USUARIO = "LogrosUsuario"
        private const val COLUMN_LU_ID = "id"
        private const val COLUMN_LU_USUARIO_ID = "usuarioId"
        private const val COLUMN_LU_LOGRO_ID = "logroId"
        private const val COLUMN_LU_FECHA = "fechaDesbloqueo"
        private const val COLUMN_LU_VISTO = "visto"

        // Tablas existentes
        private const val TABLE_EJERCICIOS = "Ejercicios"
        private const val TABLE_CANCIONES = "Canciones"
        private const val TABLE_NIVELES = "Niveles"
        private const val TABLE_PELICULAS = "Peliculas"
        private const val TABLE_SERIES = "Series"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Tabla Usuarios
        val createTableUsuarios = """
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
        db.execSQL(createTableUsuarios)

        // Tabla Progreso
        val createTableProgreso = """
            CREATE TABLE $TABLE_PROGRESO (
                $COLUMN_PR_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_PR_USUARIO_ID INTEGER UNIQUE,
                $COLUMN_PR_GRAMATICA_COMPLETADOS INTEGER DEFAULT 0,
                $COLUMN_PR_GRAMATICA_CORRECTOS INTEGER DEFAULT 0,
                $COLUMN_PR_PRONUNCIACION INTEGER DEFAULT 0,
                $COLUMN_PR_CONVERSACIONES INTEGER DEFAULT 0,
                $COLUMN_PR_PUNTUACION INTEGER DEFAULT 0,
                $COLUMN_PR_NIVEL INTEGER DEFAULT 1,
                $COLUMN_PR_EXPERIENCIA INTEGER DEFAULT 0,
                $COLUMN_PR_RACHA_ACTUAL INTEGER DEFAULT 0,
                $COLUMN_PR_RACHA_MAXIMA INTEGER DEFAULT 0,
                $COLUMN_PR_ULTIMA_ACTIVIDAD TEXT,
                $COLUMN_PR_FECHA_CREACION TEXT,
                FOREIGN KEY($COLUMN_PR_USUARIO_ID) REFERENCES $TABLE_NAME($COLUMN_ID)
            )
        """.trimIndent()
        db.execSQL(createTableProgreso)

        // Tabla Actividad Diaria
        val createTableActividad = """
            CREATE TABLE $TABLE_ACTIVIDAD (
                $COLUMN_AC_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_AC_USUARIO_ID INTEGER,
                $COLUMN_AC_FECHA TEXT,
                $COLUMN_AC_EJERCICIOS INTEGER DEFAULT 0,
                $COLUMN_AC_TIEMPO INTEGER DEFAULT 0,
                $COLUMN_AC_PUNTUACION INTEGER DEFAULT 0,
                FOREIGN KEY($COLUMN_AC_USUARIO_ID) REFERENCES $TABLE_NAME($COLUMN_ID),
                UNIQUE($COLUMN_AC_USUARIO_ID, $COLUMN_AC_FECHA)
            )
        """.trimIndent()
        db.execSQL(createTableActividad)

        // Tabla Logros
        val createTableLogros = """
            CREATE TABLE $TABLE_LOGROS (
                $COLUMN_LO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_LO_NOMBRE TEXT,
                $COLUMN_LO_DESCRIPCION TEXT,
                $COLUMN_LO_ICONO TEXT,
                $COLUMN_LO_TIPO TEXT,
                $COLUMN_LO_VALOR INTEGER
            )
        """.trimIndent()
        db.execSQL(createTableLogros)

        // Tabla Logros Usuario
        val createTableLogrosUsuario = """
            CREATE TABLE $TABLE_LOGROS_USUARIO (
                $COLUMN_LU_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_LU_USUARIO_ID INTEGER,
                $COLUMN_LU_LOGRO_ID INTEGER,
                $COLUMN_LU_FECHA TEXT,
                $COLUMN_LU_VISTO INTEGER DEFAULT 0,
                FOREIGN KEY($COLUMN_LU_USUARIO_ID) REFERENCES $TABLE_NAME($COLUMN_ID),
                FOREIGN KEY($COLUMN_LU_LOGRO_ID) REFERENCES $TABLE_LOGROS($COLUMN_LO_ID),
                UNIQUE($COLUMN_LU_USUARIO_ID, $COLUMN_LU_LOGRO_ID)
            )
        """.trimIndent()
        db.execSQL(createTableLogrosUsuario)

        // Crear tablas existentes (Ejercicios, Canciones, etc.)
        crearTablasExistentes(db)

        // Insertar logros predefinidos
        insertarLogrosPredefinidos(db)
    }

    private fun crearTablasExistentes(db: SQLiteDatabase) {
        // Las tablas que ya tenías
        db.execSQL("""
            CREATE TABLE $TABLE_EJERCICIOS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                problema TEXT,
                solucion TEXT,
                alternativas TEXT
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE $TABLE_CANCIONES (
                titulo TEXT,
                artista TEXT,
                album TEXT,
                genero TEXT
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE $TABLE_NIVELES (
                nombre TEXT,
                dificultad INTEGER,
                descripcion TEXT
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE $TABLE_PELICULAS (
                titulo TEXT,
                director TEXT,
                anio INTEGER,
                duracionMinutos INTEGER
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE $TABLE_SERIES (
                titulo TEXT,
                temporadas INTEGER,
                episodiosPorTemporada INTEGER,
                plataforma TEXT
            )
        """.trimIndent())
    }

    private fun insertarLogrosPredefinidos(db: SQLiteDatabase) {
        val logros = listOf(
            Logro(1, "First Steps", "Complete your first exercise", "🎯", "ejercicios", 1),
            Logro(2, "Grammar Novice", "Complete 10 grammar exercises", "📚", "ejercicios", 10),
            Logro(3, "Grammar Expert", "Complete 50 grammar exercises", "🎓", "ejercicios", 50),
            Logro(4, "Pronunciation Pro", "Complete 5 pronunciation exercises", "🗣️", "pronunciacion", 5),
            Logro(5, "Conversationalist", "Complete 3 conversation scenarios", "💬", "conversaciones", 3),
            Logro(6, "On Fire!", "Maintain a 7-day streak", "🔥", "racha", 7),
            Logro(7, "Dedicated Learner", "Maintain a 30-day streak", "⭐", "racha", 30),
            Logro(8, "Point Collector", "Earn 500 points", "💰", "puntos", 500),
            Logro(9, "Level Up!", "Reach level 5", "📈", "nivel", 5),
            Logro(10, "Master", "Reach level 10", "👑", "nivel", 10)
        )

        logros.forEach { logro ->
            val valores = ContentValues().apply {
                put(COLUMN_LO_NOMBRE, logro.nombre)
                put(COLUMN_LO_DESCRIPCION, logro.descripcion)
                put(COLUMN_LO_ICONO, logro.icono)
                put(COLUMN_LO_TIPO, logro.requisitoTipo)
                put(COLUMN_LO_VALOR, logro.requisitoValor)
            }
            db.insert(TABLE_LOGROS, null, valores)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Actualizar base de datos preservando datos
        if (oldVersion < 3) {
            db.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_PROGRESO (" +
                    "$COLUMN_PR_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_PR_USUARIO_ID INTEGER UNIQUE, " +
                    "$COLUMN_PR_GRAMATICA_COMPLETADOS INTEGER DEFAULT 0, " +
                    "$COLUMN_PR_GRAMATICA_CORRECTOS INTEGER DEFAULT 0, " +
                    "$COLUMN_PR_PRONUNCIACION INTEGER DEFAULT 0, " +
                    "$COLUMN_PR_CONVERSACIONES INTEGER DEFAULT 0, " +
                    "$COLUMN_PR_PUNTUACION INTEGER DEFAULT 0, " +
                    "$COLUMN_PR_NIVEL INTEGER DEFAULT 1, " +
                    "$COLUMN_PR_EXPERIENCIA INTEGER DEFAULT 0, " +
                    "$COLUMN_PR_RACHA_ACTUAL INTEGER DEFAULT 0, " +
                    "$COLUMN_PR_RACHA_MAXIMA INTEGER DEFAULT 0, " +
                    "$COLUMN_PR_ULTIMA_ACTIVIDAD TEXT, " +
                    "$COLUMN_PR_FECHA_CREACION TEXT)")

            db.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_ACTIVIDAD (" +
                    "$COLUMN_AC_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_AC_USUARIO_ID INTEGER, " +
                    "$COLUMN_AC_FECHA TEXT, " +
                    "$COLUMN_AC_EJERCICIOS INTEGER DEFAULT 0, " +
                    "$COLUMN_AC_TIEMPO INTEGER DEFAULT 0, " +
                    "$COLUMN_AC_PUNTUACION INTEGER DEFAULT 0)")

            db.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_LOGROS (" +
                    "$COLUMN_LO_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_LO_NOMBRE TEXT, " +
                    "$COLUMN_LO_DESCRIPCION TEXT, " +
                    "$COLUMN_LO_ICONO TEXT, " +
                    "$COLUMN_LO_TIPO TEXT, " +
                    "$COLUMN_LO_VALOR INTEGER)")

            db.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_LOGROS_USUARIO (" +
                    "$COLUMN_LU_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_LU_USUARIO_ID INTEGER, " +
                    "$COLUMN_LU_LOGRO_ID INTEGER, " +
                    "$COLUMN_LU_FECHA TEXT, " +
                    "$COLUMN_LU_VISTO INTEGER DEFAULT 0)")

            insertarLogrosPredefinidos(db)
        }
    }

    // ==================== MÉTODOS DE PROGRESO ====================

    fun crearProgresoInicial(usuarioId: Int): Long {
        val db = writableDatabase
        val fechaActual = obtenerFechaActual()

        val valores = ContentValues().apply {
            put(COLUMN_PR_USUARIO_ID, usuarioId)
            put(COLUMN_PR_FECHA_CREACION, fechaActual)
            put(COLUMN_PR_ULTIMA_ACTIVIDAD, fechaActual)
        }

        val resultado = db.insert(TABLE_PROGRESO, null, valores)
        db.close()
        return resultado
    }

    fun obtenerProgreso(usuarioId: Int): Progreso? {
        val db = readableDatabase
        var progreso: Progreso? = null

        val cursor = db.query(
            TABLE_PROGRESO,
            null,
            "$COLUMN_PR_USUARIO_ID = ?",
            arrayOf(usuarioId.toString()),
            null, null, null
        )

        if (cursor.moveToFirst()) {
            progreso = Progreso(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PR_ID)),
                usuarioId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PR_USUARIO_ID)),
                ejerciciosGramaticaCompletados = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PR_GRAMATICA_COMPLETADOS)),
                ejerciciosGramaticaCorrectos = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PR_GRAMATICA_CORRECTOS)),
                pronunciacionCompletada = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PR_PRONUNCIACION)),
                conversacionesCompletadas = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PR_CONVERSACIONES)),
                puntuacionTotal = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PR_PUNTUACION)),
                nivelActual = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PR_NIVEL)),
                experiencia = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PR_EXPERIENCIA)),
                rachaActual = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PR_RACHA_ACTUAL)),
                rachaMaxima = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PR_RACHA_MAXIMA)),
                fechaUltimaActividad = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PR_ULTIMA_ACTIVIDAD)),
                fechaCreacion = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PR_FECHA_CREACION))
            )
        }

        cursor.close()
        db.close()
        return progreso
    }

    fun actualizarProgresoGramatica(usuarioId: Int, completado: Boolean) {
        val progreso = obtenerProgreso(usuarioId) ?: return
        val db = writableDatabase

        val valores = ContentValues().apply {
            put(COLUMN_PR_GRAMATICA_COMPLETADOS, progreso.ejerciciosGramaticaCompletados + 1)
            if (completado) {
                put(COLUMN_PR_GRAMATICA_CORRECTOS, progreso.ejerciciosGramaticaCorrectos + 1)
                put(COLUMN_PR_PUNTUACION, progreso.puntuacionTotal + 10)
                put(COLUMN_PR_EXPERIENCIA, progreso.experiencia + 15)
            }
            put(COLUMN_PR_ULTIMA_ACTIVIDAD, obtenerFechaActual())
        }

        db.update(TABLE_PROGRESO, valores, "$COLUMN_PR_USUARIO_ID = ?", arrayOf(usuarioId.toString()))

        // Actualizar nivel si es necesario
        actualizarNivel(usuarioId)

        // Verificar logros
        verificarLogros(usuarioId)

        // Registrar actividad diaria
        registrarActividadDiaria(usuarioId, 1, 10)

        db.close()
    }

    fun actualizarProgresoPronunciacion(usuarioId: Int) {
        val progreso = obtenerProgreso(usuarioId) ?: return
        val db = writableDatabase

        val valores = ContentValues().apply {
            put(COLUMN_PR_PRONUNCIACION, progreso.pronunciacionCompletada + 1)
            put(COLUMN_PR_PUNTUACION, progreso.puntuacionTotal + 15)
            put(COLUMN_PR_EXPERIENCIA, progreso.experiencia + 20)
            put(COLUMN_PR_ULTIMA_ACTIVIDAD, obtenerFechaActual())
        }

        db.update(TABLE_PROGRESO, valores, "$COLUMN_PR_USUARIO_ID = ?", arrayOf(usuarioId.toString()))
        actualizarNivel(usuarioId)
        verificarLogros(usuarioId)
        registrarActividadDiaria(usuarioId, 1, 15)
        db.close()
    }

    fun actualizarProgresoConversacion(usuarioId: Int) {
        val progreso = obtenerProgreso(usuarioId) ?: return
        val db = writableDatabase

        val valores = ContentValues().apply {
            put(COLUMN_PR_CONVERSACIONES, progreso.conversacionesCompletadas + 1)
            put(COLUMN_PR_PUNTUACION, progreso.puntuacionTotal + 20)
            put(COLUMN_PR_EXPERIENCIA, progreso.experiencia + 25)
            put(COLUMN_PR_ULTIMA_ACTIVIDAD, obtenerFechaActual())
        }

        db.update(TABLE_PROGRESO, valores, "$COLUMN_PR_USUARIO_ID = ?", arrayOf(usuarioId.toString()))
        actualizarNivel(usuarioId)
        verificarLogros(usuarioId)
        registrarActividadDiaria(usuarioId, 1, 20)
        db.close()
    }

    private fun actualizarNivel(usuarioId: Int) {
        val progreso = obtenerProgreso(usuarioId) ?: return
        val nivelNuevo = calcularNivel(progreso.experiencia)

        if (nivelNuevo > progreso.nivelActual) {
            val db = writableDatabase
            val valores = ContentValues().apply {
                put(COLUMN_PR_NIVEL, nivelNuevo)
            }
            db.update(TABLE_PROGRESO, valores, "$COLUMN_PR_USUARIO_ID = ?", arrayOf(usuarioId.toString()))
            db.close()
        }
    }

    private fun calcularNivel(experiencia: Int): Int {
        // 100 XP por nivel
        return (experiencia / 100) + 1
    }

    fun actualizarRacha(usuarioId: Int) {
        val progreso = obtenerProgreso(usuarioId) ?: return
        val fechaHoy = obtenerFechaActual()
        val fechaUltima = progreso.fechaUltimaActividad

        val diasDiferencia = calcularDiasDiferencia(fechaUltima, fechaHoy)

        val db = writableDatabase
        val valores = ContentValues()

        when {
            diasDiferencia == 0 -> {
                // Mismo día, no hacer nada
                db.close()
                return
            }
            diasDiferencia == 1 -> {
                // Día consecutivo, incrementar racha
                val nuevaRacha = progreso.rachaActual + 1
                valores.put(COLUMN_PR_RACHA_ACTUAL, nuevaRacha)
                if (nuevaRacha > progreso.rachaMaxima) {
                    valores.put(COLUMN_PR_RACHA_MAXIMA, nuevaRacha)
                }
            }
            else -> {
                // Se rompió la racha
                valores.put(COLUMN_PR_RACHA_ACTUAL, 1)
            }
        }

        db.update(TABLE_PROGRESO, valores, "$COLUMN_PR_USUARIO_ID = ?", arrayOf(usuarioId.toString()))
        db.close()
    }

    private fun registrarActividadDiaria(usuarioId: Int, ejercicios: Int, puntos: Int) {
        val db = writableDatabase
        val fechaHoy = obtenerFechaActual()

        // Intentar actualizar registro existente
        val cursor = db.query(
            TABLE_ACTIVIDAD,
            null,
            "$COLUMN_AC_USUARIO_ID = ? AND $COLUMN_AC_FECHA = ?",
            arrayOf(usuarioId.toString(), fechaHoy),
            null, null, null
        )

        if (cursor.moveToFirst()) {
            // Actualizar existente
            val ejerciciosActuales = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AC_EJERCICIOS))
            val puntuacionActual = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AC_PUNTUACION))

            val valores = ContentValues().apply {
                put(COLUMN_AC_EJERCICIOS, ejerciciosActuales + ejercicios)
                put(COLUMN_AC_PUNTUACION, puntuacionActual + puntos)
            }

            db.update(
                TABLE_ACTIVIDAD,
                valores,
                "$COLUMN_AC_USUARIO_ID = ? AND $COLUMN_AC_FECHA = ?",
                arrayOf(usuarioId.toString(), fechaHoy)
            )
        } else {
            // Crear nuevo
            val valores = ContentValues().apply {
                put(COLUMN_AC_USUARIO_ID, usuarioId)
                put(COLUMN_AC_FECHA, fechaHoy)
                put(COLUMN_AC_EJERCICIOS, ejercicios)
                put(COLUMN_AC_PUNTUACION, puntos)
            }
            db.insert(TABLE_ACTIVIDAD, null, valores)
        }

        cursor.close()
        db.close()

        // Actualizar racha
        actualizarRacha(usuarioId)
    }

    fun obtenerActividadSemanal(usuarioId: Int): List<ActividadDiaria> {
        val lista = mutableListOf<ActividadDiaria>()
        val db = readableDatabase

        // Últimos 7 días
        val query = """
            SELECT * FROM $TABLE_ACTIVIDAD 
            WHERE $COLUMN_AC_USUARIO_ID = ? 
            ORDER BY $COLUMN_AC_FECHA DESC 
            LIMIT 7
        """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(usuarioId.toString()))

        if (cursor.moveToFirst()) {
            do {
                val actividad = ActividadDiaria(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AC_ID)),
                    usuarioId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AC_USUARIO_ID)),
                    fecha = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AC_FECHA)),
                    ejerciciosCompletados = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AC_EJERCICIOS)),
                    tiempoEstudio = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AC_TIEMPO)),
                    puntuacionGanada = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AC_PUNTUACION))
                )
                lista.add(actividad)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return lista
    }

    private fun verificarLogros(usuarioId: Int) {
        val progreso = obtenerProgreso(usuarioId) ?: return
        val db = readableDatabase

        // Obtener todos los logros
        val cursorLogros = db.query(TABLE_LOGROS, null, null, null, null, null, null)

        if (cursorLogros.moveToFirst()) {
            do {
                val logroId = cursorLogros.getInt(cursorLogros.getColumnIndexOrThrow(COLUMN_LO_ID))
                val tipo = cursorLogros.getString(cursorLogros.getColumnIndexOrThrow(COLUMN_LO_TIPO))
                val valor = cursorLogros.getInt(cursorLogros.getColumnIndexOrThrow(COLUMN_LO_VALOR))

                // Verificar si cumple requisito
                val cumpleRequisito = when (tipo) {
                    "ejercicios" -> progreso.ejerciciosGramaticaCompletados >= valor
                    "pronunciacion" -> progreso.pronunciacionCompletada >= valor
                    "conversaciones" -> progreso.conversacionesCompletadas >= valor
                    "racha" -> progreso.rachaActual >= valor
                    "puntos" -> progreso.puntuacionTotal >= valor
                    "nivel" -> progreso.nivelActual >= valor
                    else -> false
                }

                if (cumpleRequisito) {
                    // Verificar si ya lo tiene
                    val tieneLogro = verificarSiTieneLogro(usuarioId, logroId)
                    if (!tieneLogro) {
                        desbloquearLogro(usuarioId, logroId)
                    }
                }
            } while (cursorLogros.moveToNext())
        }

        cursorLogros.close()
        db.close()
    }

    private fun verificarSiTieneLogro(usuarioId: Int, logroId: Int): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_LOGROS_USUARIO,
            null,
            "$COLUMN_LU_USUARIO_ID = ? AND $COLUMN_LU_LOGRO_ID = ?",
            arrayOf(usuarioId.toString(), logroId.toString()),
            null, null, null
        )

        val tiene = cursor.count > 0
        cursor.close()
        db.close()
        return tiene
    }

    private fun desbloquearLogro(usuarioId: Int, logroId: Int) {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put(COLUMN_LU_USUARIO_ID, usuarioId)
            put(COLUMN_LU_LOGRO_ID, logroId)
            put(COLUMN_LU_FECHA, obtenerFechaActual())
            put(COLUMN_LU_VISTO, 0)
        }

        db.insert(TABLE_LOGROS_USUARIO, null, valores)
        db.close()
    }

    fun obtenerLogrosDesbloqueados(usuarioId: Int): List<Logro> {
        val lista = mutableListOf<Logro>()
        val db = readableDatabase

        val query = """
            SELECT l.* FROM $TABLE_LOGROS l
            INNER JOIN $TABLE_LOGROS_USUARIO lu ON l.$COLUMN_LO_ID = lu.$COLUMN_LU_LOGRO_ID
            WHERE lu.$COLUMN_LU_USUARIO_ID = ?
            ORDER BY lu.$COLUMN_LU_FECHA DESC
        """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(usuarioId.toString()))

        if (cursor.moveToFirst()) {
            do {
                val logro = Logro(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LO_ID)),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LO_NOMBRE)),
                    descripcion = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LO_DESCRIPCION)),
                    icono = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LO_ICONO)),
                    requisitoTipo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LO_TIPO)),
                    requisitoValor = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LO_VALOR))
                )
                lista.add(logro)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return lista
    }

    // Utilidades
    private fun obtenerFechaActual(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun calcularDiasDiferencia(fecha1: String, fecha2: String): Int {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date1 = sdf.parse(fecha1) ?: return 999
            val date2 = sdf.parse(fecha2) ?: return 999

            val diff = date2.time - date1.time
            (diff / (1000 * 60 * 60 * 24)).toInt()
        } catch (e: Exception) {
            999
        }
    }

    // ==================== MÉTODOS EXISTENTES (mantener) ====================

    fun insertarEjercicio(ejercicio: Ejercicio): Long {
        val db = this.writableDatabase
        val alternativasString = ejercicio.alternativas.joinToString("|")

        val valores = ContentValues().apply {
            put("problema", ejercicio.problema)
            put("solucion", ejercicio.solucion)
            put("alternativas", alternativasString)
        }

        val resultado = db.insert(TABLE_EJERCICIOS, null, valores)
        db.close()
        return resultado
    }

    fun insertarCancion(cancion: Cancion): Long {
        val db = this.writableDatabase
        val valores = ContentValues().apply {
            put("titulo", cancion.titulo)
            put("artista", cancion.artista)
            put("album", cancion.album)
            put("genero", cancion.genero)
        }
        val resultado = db.insert(TABLE_CANCIONES, null, valores)
        db.close()
        return resultado
    }

    fun insertarNivel(nivel: Nivel): Long {
        val db = this.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", nivel.nombre)
            put("dificultad", nivel.dificultad)
            put("descripcion", nivel.descripcion)
        }
        val resultado = db.insert(TABLE_NIVELES, null, valores)
        db.close()
        return resultado
    }

    fun insertarPelicula(pelicula: Pelicula): Long {
        val db = this.writableDatabase
        val valores = ContentValues().apply {
            put("titulo", pelicula.titulo)
            put("director", pelicula.director)
            put("anio", pelicula.anio)
            put("duracionMinutos", pelicula.duracionMinutos)
        }
        val resultado = db.insert(TABLE_PELICULAS, null, valores)
        db.close()
        return resultado
    }

    fun insertarSerie(serie: Serie): Long {
        val db = this.writableDatabase
        val valores = ContentValues().apply {
            put("titulo", serie.titulo)
            put("temporadas", serie.temporadas)
            put("episodiosPorTemporada", serie.episodiosPorTemporada)
            put("plataforma", serie.plataforma)
        }
        val resultado = db.insert(TABLE_SERIES, null, valores)
        db.close()
        return resultado
    }

    fun obtenerTodosLosUsuarios(): List<Usuario> {
        val listaUsuarios = mutableListOf<Usuario>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE))
                val apellido = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_APELLIDO))
                val correo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CORREO))
                val pais = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAIS))
                val celular = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CELULAR))
                val fecNac = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECNAC))
                val nomUsuario = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMUSUARIO))
                val contrasena = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTRASENA))

                val usuario = Usuario(
                    id, nombre, apellido, correo, pais, celular, fecNac, nomUsuario, contrasena
                )
                listaUsuarios.add(usuario)
            } while (cursor.moveToNext())
        }

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
            null, null, null
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