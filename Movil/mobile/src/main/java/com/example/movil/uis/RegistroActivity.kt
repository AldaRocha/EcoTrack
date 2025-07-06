package com.example.movil.uis

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movil.R
import com.example.movil.daos.UsuarioDAO
import com.example.movil.models.Pais
import com.example.movil.models.Persona
import com.example.movil.models.Usuario
import com.example.movil.models.UsuarioLogin
import com.example.movil.models.UsuarioModel
import com.example.movil.services.RestService
import com.example.movil.services.StorageService
import kotlinx.coroutines.launch
import java.util.Calendar

class RegistroActivity : ComponentActivity() {
    private lateinit var context: Context
    private lateinit var usuarioDao: UsuarioDAO
    private lateinit var _rest: RestService
    private lateinit var _storage: StorageService
    private var loading by mutableStateOf(false)
    private lateinit var paises: List<Pais>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        usuarioDao = UsuarioDAO(this)
        _rest = RestService(this)
        _storage = StorageService(this)
        setContent {
            MaterialTheme {
                Screen()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun ScreenPreview(){
        MaterialTheme {
            Screen()
        }
    }

    @Composable
    fun Screen(){
        var nombre by remember { mutableStateOf("") }
        var apellidopaterno by remember { mutableStateOf("") }
        var apellidomaterno by remember { mutableStateOf("") }
        var celular by remember { mutableStateOf("") }
        val contextCal = LocalContext.current
        val calendar = Calendar.getInstance()
        var fechanacimiento by remember { mutableStateOf("") }
        var email by  remember { mutableStateOf("") }
        var expanded by remember { mutableStateOf(false) }
        var selectedPais by remember { mutableStateOf<Pais?>(null) }
        var cuenta by remember { mutableStateOf("") }
        var contrasena by remember { mutableStateOf("") }
        var terminos by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        LaunchedEffect(Unit) {
            scope.launch {
                ObtenerPaises()
            }
        }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = colorResource(R.color.primary)
                )
                .systemBarsPadding()
                .windowInsetsPadding(
                    WindowInsets(
                        0,
                        0,
                        0,
                        0
                    )
                ),
            contentAlignment = Alignment.Center
        ){
            val screenWidth = maxWidth
            val responsiveWidth = screenWidth * 0.5f
            val fontSizeTitle = responsiveWidth.value / 10

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = colorResource(R.color.white)
                    )
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "Regístrate",
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = colorResource(R.color.black),
                    textAlign = TextAlign.Center,
                    fontSize = fontSizeTitle.sp
                )
                Spacer(
                    modifier = Modifier
                        .height(24.dp)
                )
                OutlinedTextField(
                    value = nombre,
                    onValueChange = {
                        nombre = it
                    },
                    label = {
                        Text(
                            text = "Nombre"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = apellidopaterno,
                    onValueChange = {
                        apellidopaterno = it
                    },
                    label = {
                        Text(
                            text = "Apellido paterno"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = apellidomaterno,
                    onValueChange = {
                        apellidomaterno = it
                    },
                    label = {
                        Text(
                            text = "Apellido materno"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = celular,
                    onValueChange = {
                        celular = it
                    },
                    label = {
                        Text(
                            text = "Celular"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    singleLine = true
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            DatePickerDialog(
                                contextCal,
                                { _, year, month, dayOfMonth ->
                                    fechanacimiento = "$year-${month + 1}-$dayOfMonth"
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        }
                ) {
                    OutlinedTextField(
                        value = fechanacimiento,
                        onValueChange = {},
                        label = {
                            Text(
                                text = "Fecha de nacimiento"
                            )
                        },
                        readOnly = true,
                        enabled = false,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                    },
                    label = {
                        Text(
                            text = "Correo"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = selectedPais?.nombre ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text(
                            text = "País"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            expanded = true
                        },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                expanded = !expanded
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Expandir"
                            )
                        }
                    }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    paises.forEach{
                        pais ->
                        DropdownMenuItem(
                            onClick = {
                                selectedPais = pais
                                expanded = false
                            }
                        ) {
                            Text(
                                text = pais.nombre
                            )
                        }
                    }
                }
                OutlinedTextField(
                    value = cuenta,
                    onValueChange = {
                        cuenta = it
                    },
                    label = {
                        Text(
                            text = "Cuenta"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = contrasena,
                    onValueChange = {
                        contrasena = it
                    },
                    label = {
                        Text(
                            text = "Contraseña"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    singleLine = true
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Checkbox(
                        checked = terminos,
                        onCheckedChange = {
                            terminos = it
                        }
                    )
                    Text(
                        text = "Acepto los Términos y Condiciones",
                        modifier = Modifier
                            .clickable {
                                terminos = !terminos
                            }
                            .padding(
                                start = 8.dp
                            )
                    )
                }
                Spacer(
                    modifier = Modifier
                        .height(24.dp)
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(R.color.primary),
                        contentColor = colorResource(R.color.white)
                    ),
                    onClick = {
                        loading = true
                        try{
                            scope.launch {
                                val persona = selectedPais?.let {
                                    Persona(
                                        0,
                                        nombre,
                                        apellidopaterno,
                                        apellidomaterno,
                                        celular,
                                        fechanacimiento,
                                        email,
                                        fechanacimiento,
                                        it.paisId,
                                        it
                                    )
                                }
                                val usuario = persona?.let {
                                    UsuarioModel(
                                        0,
                                        cuenta,
                                        contrasena,
                                        1,
                                        if (terminos) 1.toByte() else 0.toByte(),
                                        1,
                                        0,
                                        it,
                                        1,
                                        null,
                                        1
                                    )
                                }

                                val res = usuario?.let {
                                    _rest.Post(
                                        "/seguridad/usuario",
                                        it
                                    )
                                }

                                val message = res?.get("message") as? String
                                if (message != null && message == "Se ha creado el usuario"){
                                    Toast.makeText(
                                        context,
                                        "$message. Entrando...",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    val user = UsuarioLogin(
                                        cuenta,
                                        contrasena
                                    )
                                    val login = _rest.Post(
                                        "auth/login",
                                        user,
                                        false,
                                        false
                                    )

                                    val messagelogin = login["message"] as? String
                                    if (messagelogin != null){
                                        Toast.makeText(
                                            context,
                                            messagelogin,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else{
                                        val token = login["token"] as String
                                        val existe = usuarioDao.GetByUser(cuenta)
                                        if (existe != null){
                                            usuarioDao.Update(
                                                Usuario(
                                                    existe.UsuarioId,
                                                    existe.Usuario,
                                                    existe.Contrasena,
                                                    token
                                                )
                                            )
                                        } else{
                                            usuarioDao.Insert(
                                                Usuario(
                                                    0,
                                                    cuenta,
                                                    contrasena,
                                                    token
                                                )
                                            )
                                        }
                                        _storage.SaveItem(
                                            "token",
                                            token
                                        )
                                        val intent = Intent(
                                            context,
                                            HomeActivity::class.java
                                        )
                                        startActivity(intent)
                                        finish()
                                    }
                                } else{
                                    Toast.makeText(
                                        context,
                                        message,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        } catch (ex: Exception){
                            Toast.makeText(
                                context,
                                ex.message,
                                Toast.LENGTH_LONG
                            ).show()
                        } finally {
                            loading = false
                        }
                    }
                ) {
                    Text(
                        text = "Enviar"
                    )
                }
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(R.color.other),
                        contentColor = colorResource(R.color.white)
                    ),
                    onClick = {
                        val intent = Intent(
                            context,
                            LoginActivity::class.java
                        )
                        startActivity(intent)
                        finish()
                    }
                ) {
                    Text(
                        text = "Ya tengo una cuenta"
                    )
                }
                if (loading){
                    Spacer(
                        modifier = Modifier
                            .height(2.dp)
                    )
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(40.dp),
                        color = colorResource(R.color.black)
                    )
                }
            }
        }
    }

    suspend private fun ObtenerPaises(){
        loading = true
        try{
            val res = _rest.Post(
                "/catalogo/pais/buscar",
                mapOf("Activo" to 1)
            )

            val message = res["message"] as? String
            if (message == null){
                val objeto = res["data"] as? Map<*, *>
                val list = objeto?.get("pais") as? List<Map<String, Any>>
                paises = list?.map{
                    Pais(
                        paisId = (it["paisId"] as? Double)?.toInt() ?: 0,
                        nombre = it["nombre"] as? String ?: "",
                        activo = (it["activo"] as? Double)?.let { it.toInt().toByte() } ?: 0
                    )
                } ?: listOf()
            }
        } catch (ex: Exception){
            Toast.makeText(
                context,
                ex.message,
                Toast.LENGTH_LONG
            ).show()
        } finally {
            loading = false
        }
    }
}