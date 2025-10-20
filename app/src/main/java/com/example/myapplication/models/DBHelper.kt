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


}