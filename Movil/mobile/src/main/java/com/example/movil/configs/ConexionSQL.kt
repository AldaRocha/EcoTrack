package com.example.movil.configs

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class ConexionSQL(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "EcoTrack.db"
    }

    override fun onCreate(db: SQLiteDatabase?){
        db?.execSQL("CREATE TABLE AuthData(UsuarioId INTEGER PRIMARY KEY, Usuario TEXT, Contrasena TEXT, Token TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int){
        if (newVersion == 1){
            Log.i("Alda", "Ejecutando version 1 de BD")
        }
    }
}