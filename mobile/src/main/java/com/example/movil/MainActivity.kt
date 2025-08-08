package com.example.movil

import android.os.Bundle
import android.widget.*
import retrofit2.Call
import androidx.appcompat.app.AppCompatActivity
import com.example.movil.models.RegistroModel
import com.example.movil.api.RetrofitClient
import retrofit2.Callback
import retrofit2.Response
import android.util.Log

class MainActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etApellidoPaterno: EditText
    private lateinit var etApellidoMaterno: EditText
    private lateinit var etTelefono: EditText
    private lateinit var etFechaNacimiento: EditText
    private lateinit var etPais: EditText
    private lateinit var etCorreo: EditText
    private lateinit var etContrasenia: EditText
    private lateinit var etConfirmar: EditText
    private lateinit var cbTerminos: CheckBox
    private lateinit var btnRegistrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etNombre = findViewById(R.id.etNombre)
        etApellidoPaterno = findViewById(R.id.etApellidoPaterno)
        etApellidoMaterno = findViewById(R.id.etApellidoMaterno)
        etTelefono = findViewById(R.id.etTelefono)
        etFechaNacimiento = findViewById(R.id.etFechaNacimiento)
        etPais = findViewById(R.id.etPais)
        etCorreo = findViewById(R.id.etCorreo)
        etContrasenia = findViewById(R.id.etContrasenia)
        etConfirmar = findViewById(R.id.etConfirmarContrasenia)
        cbTerminos = findViewById(R.id.cbTerminos)
        btnRegistrar = findViewById(R.id.btnRegistrar)

        btnRegistrar.setOnClickListener {
            registrarUsuarios()
        }
    }

    private fun registrarUsuarios() {
        val pass = etContrasenia.text.toString()
        val confirmar = etConfirmar.text.toString()

        if (!cbTerminos.isChecked) {
            Toast.makeText(this, "Debes aceptar los términos", Toast.LENGTH_SHORT).show()
            return
        }

        if (pass != confirmar) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        val usuario = RegistroModel(
            nombre = etNombre.text.toString(),
            apellido_paterno = etApellidoPaterno.text.toString(),
            apellido_materno = etApellidoMaterno.text.toString(),
            telefono = etTelefono.text.toString(),
            fecha_nacimiento = etFechaNacimiento.text.toString(),
            pais = etPais.text.toString(),
            correo = etCorreo.text.toString(),
            contrasenia = pass,
            confirmar_contrasenia = pass,
            aceptar_terminos = cbTerminos.isChecked
        )


        btnRegistrar.isEnabled = false

        guardarUsuario(usuario)
    }

    private fun guardarUsuario(usuario: RegistroModel) {
        RetrofitClient.instance.registrarUsuario(usuario).enqueue(object : Callback<RegistroModel> {
            override fun onResponse(call: Call<RegistroModel>, response: Response<RegistroModel>) {
                btnRegistrar.isEnabled = true

                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Usuario registrado", Toast.LENGTH_SHORT).show()
                    limpiarCampos()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("RegistroError", "Código: ${response.code()} - Error: $errorBody")

                    if (response.code() == 409) {
                        Toast.makeText(this@MainActivity, "Correo o teléfono ya registrado", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MainActivity, "Error al registrar: código ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<RegistroModel>, t: Throwable) {
                btnRegistrar.isEnabled = true
                Log.e("RegistroError", "Fallo conexión: ${t.message}")
                Toast.makeText(this@MainActivity, "Fallo conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun limpiarCampos() {
        etNombre.text.clear()
        etApellidoPaterno.text.clear()
        etApellidoMaterno.text.clear()
        etTelefono.text.clear()
        etFechaNacimiento.text.clear()
        etPais.text.clear()
        etCorreo.text.clear()
        etContrasenia.text.clear()
        etConfirmar.text.clear()
        cbTerminos.isChecked = false
    }
}
