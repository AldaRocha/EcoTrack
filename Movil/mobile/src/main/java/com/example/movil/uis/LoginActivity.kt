package com.example.movil.uis

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movil.R
import com.example.movil.daos.UsuarioDAO
import com.example.movil.models.Usuario
import com.example.movil.models.UsuarioLogin
import com.example.movil.services.RestService
import com.example.movil.services.StorageService
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {
    private lateinit var context: Context
    private lateinit var usuarioDao: UsuarioDAO
    private lateinit var _rest: RestService
    private lateinit var _storage: StorageService
    private lateinit var usuarios: List<Usuario>
    private var existen by mutableStateOf(false)

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
        var usuario by remember { mutableStateOf("") }
        var contrasena by remember { mutableStateOf("") }
        var loading by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        LaunchedEffect(Unit) {
            CheckForLoginUser()
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
        ) {
            val screenWidth = maxWidth
            val responsiveWidthImage = screenWidth * 0.3f

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
            ) {
                Image(
                    painter = painterResource(R.drawable.logo_eco_track_sf),
                    contentDescription = "Logotipo",
                    modifier = Modifier
                        .width(responsiveWidthImage)
                        .aspectRatio(1f)
                )
                Spacer(
                    modifier = Modifier
                        .height(24.dp)
                )
                OutlinedTextField(
                    value = usuario,
                    onValueChange = {
                        usuario = it
                    },
                    label = {
                        Text(
                            text = "Usuario"
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
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    enabled = !loading,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(R.color.primary),
                        contentColor = colorResource(R.color.white)
                    ),
                    onClick = {
                        loading = true
                        scope.launch{
                            try{
                                val res = _rest.Post(
                                    "auth/login",
                                    UsuarioLogin(
                                        usuario,
                                        contrasena
                                    ),
                                    false,
                                    false
                                )
                                val message = res["message"] as? String

                                if(message != null){
                                    Toast.makeText(
                                        context,
                                        message,
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else{
                                    val token = res["token"] as String
                                    val existe = usuarioDao.GetByUser(usuario)
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
                                                usuario,
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
                ) {
                    Text(
                        text = "Iniciar sesión"
                    )
                }
                if (existen){
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(),
                        enabled = !loading,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(R.color.secondary),
                            contentColor = colorResource(R.color.white)
                        ),
                        onClick = {
                            loading = true
                            try{
                                val intent = Intent(
                                    context,
                                    SelectUserActivity::class.java
                                )
                                intent.putParcelableArrayListExtra(
                                    "Users",
                                    ArrayList(usuarios)
                                )
                                startActivity(intent)
                                finish()
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
                            text = "Ver perfiles"
                        )
                    }
                }
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    enabled = !loading,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(R.color.other),
                        contentColor = colorResource(R.color.white)
                    ),
                    onClick = {
                        loading = true
                        try{
                            val intent = Intent(
                                context,
                                RegistroActivity::class.java
                            )
                            startActivity(intent)
                            finish()
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
                        text = "Regístrate"
                    )
                }
                if (loading){
                    Spacer(
                        modifier = Modifier
                            .height(24.dp)
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

    private fun CheckForLoginUser(){
        try{
            usuarios = usuarioDao.GetAll()
            existen = usuarios.isNotEmpty()
        } catch (ex: Exception){
            Toast.makeText(
                context,
                ex.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
