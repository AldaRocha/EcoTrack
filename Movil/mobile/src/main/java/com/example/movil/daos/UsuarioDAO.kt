package com.example.movil.daos

import android.content.Context
import android.database.Cursor
import com.example.movil.configs.ConexionSQL
import com.example.movil.models.Usuario

class UsuarioDAO(context: Context) {
    private val DBConn = ConexionSQL(context)
    private val TableName = "AuthData"

    fun GetAll(): List<Usuario>{
        val db = DBConn.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TableName", null)
        val data = mutableListOf<Usuario>()
        with(cursor){
            while(moveToNext()){
                val item = Usuario(
                    getInt(getColumnIndexOrThrow("UsuarioId")),
                    getString(getColumnIndexOrThrow("Usuario")),
                    getString(getColumnIndexOrThrow("Contrasena")),
                    getString(getColumnIndexOrThrow("Token"))
                )
                data.add(item)
            }
            close()
        }
        db.close()
        return data
    }

    fun GetById(usuarioId: Int): Usuario?{
        val db = DBConn.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TableName WHERE UsuarioId = ?", arrayOf(usuarioId.toString()))
        val data = mutableListOf<Usuario>()
        with(cursor){
            while(moveToNext()){
                val item = Usuario(
                    getInt(getColumnIndexOrThrow("UsuarioId")),
                    getString(getColumnIndexOrThrow("Usuario")),
                    getString(getColumnIndexOrThrow("Contrasena")),
                    getString(getColumnIndexOrThrow("Token"))
                )
                data.add(item)
            }
            close()
        }
        db.close()
        if(data.isNotEmpty()){
            return data.first()
        }
        return null
    }

    fun GetByUser(usuario: String): Usuario?{
        val db = DBConn.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TableName WHERE Usuario = ?", arrayOf(usuario))
        val data = mutableListOf<Usuario>()
        with(cursor){
            while(moveToNext()){
                val item = Usuario(
                    getInt(getColumnIndexOrThrow("UsuarioId")),
                    getString(getColumnIndexOrThrow("Usuario")),
                    getString(getColumnIndexOrThrow("Contrasena")),
                    getString(getColumnIndexOrThrow("Token"))
                )
                data.add(item)
            }
            close()
        }
        db.close()
        if(data.isNotEmpty()){
            return data.first()
        }
        return null
    }

    fun Insert(data: Usuario){
        val db = DBConn.writableDatabase
        db.insert(TableName, null, data.MapForInsert())
        db.close()
    }

    fun Update(data: Usuario){
        val db = DBConn.writableDatabase
        db.update(TableName, data.MapForUpdate(), "UsuarioId = ?", arrayOf(data.UsuarioId.toString()))
        db.close()
    }

    fun Delete(data: Usuario){
        val db = DBConn.writableDatabase
        db.delete(TableName, "UsuarioId = ?", arrayOf(data.UsuarioId.toString()))
        db.close()
    }
}