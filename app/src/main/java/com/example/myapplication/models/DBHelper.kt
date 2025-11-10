package com.example.myapplication.models

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "DATABASE_NAME", null, DATABASE_VERSION) {

    companion object  {
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
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        db?.execSQL("DROP TABLE IF EXISTS Usuarios")
        onCreate(db)
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

        val selection = "$COLUMN_NOMBRE = ?"
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