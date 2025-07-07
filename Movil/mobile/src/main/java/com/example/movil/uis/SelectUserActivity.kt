package com.example.movil.uis

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AlertDialog
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.example.movil.R
import com.example.movil.daos.UsuarioDAO
import com.example.movil.models.Usuario
import com.example.movil.models.UsuarioLogin
import com.example.movil.services.RestService
import com.example.movil.services.StorageService
import kotlinx.coroutines.launch

class SelectUserActivity : FragmentActivity() {
    private lateinit var context: Context
    private lateinit var users: List<Usuario>
    private lateinit var usuarioDao: UsuarioDAO
    private lateinit var _rest: RestService
    private lateinit var _storage: StorageService
    private var loading by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        users = intent.getParcelableArrayListExtra<Usuario>("Users") ?: emptyList()
        usuarioDao = UsuarioDAO(this)
        _rest = RestService(this)
        _storage = StorageService(this)
        setContent{
            MaterialTheme{
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
            val responsiveWidth = screenWidth * 0.5f
            val fontSizeTitle = responsiveWidth.value / 10
            val fontSizeText = responsiveWidth.value / 15
            val columns = 2
            val isOdd = users.size % 2 != 0

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = colorResource(R.color.white)
                    )
                    .padding(
                        horizontal = 16.dp
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                item{
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = colorResource(R.color.white)
                            ),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = "Selecciona tu usuario:",
                            modifier = Modifier
                                .width(responsiveWidth),
                            color = colorResource(R.color.black),
                            textAlign = TextAlign.Center,
                            fontSize = fontSizeTitle.sp
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .height(24.dp)
                    )
                }
                items(
                    users.chunked(columns)
                ){ row ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = colorResource(R.color.white)
                                ),
                            horizontalArrangement = if(row.size == 1 && isOdd){
                                Arrangement.Center
                            } else{
                                Arrangement.SpaceAround
                            },
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            row.forEach{ user ->
                                Card(
                                    modifier = Modifier
                                        .width(screenWidth * 0.4f)
                                        .background(
                                            color = colorResource(R.color.other)
                                        )
                                        .padding(16.dp)
                                        .clickable {
                                            ShowBiometricPrompt(user)
                                        }
                                ){
                                    Column(
                                        modifier = Modifier
                                            .background(
                                                color = colorResource(R.color.white)
                                            )
                                            .padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ){
                                        Image(
                                            painter = painterResource(R.drawable.logo_eco_track_sf),
                                            contentDescription = "User image",
                                            modifier = Modifier
                                                .size(80.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    color = colorResource(R.color.white)
                                                )
                                        )
                                        Spacer(
                                            modifier = Modifier
                                                .height(24.dp)
                                        )
                                        Text(
                                            text = user.Usuario,
                                            modifier = Modifier
                                                .width(responsiveWidth),
                                            color = colorResource(R.color.black),
                                            textAlign = TextAlign.Center,
                                            fontSize = fontSizeText.sp
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(
                            modifier = Modifier
                                .height(15.dp)
                        )
                    }
                }
                item{
                    Column(
                        modifier = Modifier
                            .background(
                                color = colorResource(R.color.white)
                            ),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(
                            text = "¿No ves tu cuenta?",
                            modifier = Modifier
                                .width(responsiveWidth),
                            color = colorResource(R.color.black),
                            textAlign = TextAlign.Center,
                            fontSize = fontSizeText.sp
                        )
                        Spacer(
                            modifier = Modifier
                                .height(5.dp)
                        )
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = colorResource(R.color.primary),
                                contentColor = colorResource(R.color.white)
                            ),
                            onClick = {
                                val intent = Intent(context, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        ){
                            Text(
                                text = "Iniciar sesión con otra cuenta"
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
        }
    }

    private fun ShowBiometricPrompt(usuario: Usuario){
        val executor = ContextCompat.getMainExecutor(this)

        val biometricPrompt = BiometricPrompt(
            this,
            executor,
            object: BiometricPrompt.AuthenticationCallback(){
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)

                    loading = true
                    try{
                        lifecycleScope.launch{
                            val res = _rest.Post(
                                "auth/login",
                                UsuarioLogin(
                                    usuario.Usuario,
                                    usuario.Contrasena
                                ),
                                false,
                                false
                            )

                            val message = res["message"] as? String

                            if (message != null){
                                ShowErrorDialog(usuario)
                            } else{
                                val token = res["token"] as String
                                usuarioDao.Update(
                                    Usuario(
                                        usuario.UsuarioId,
                                        usuario.Usuario,
                                        usuario.Contrasena,
                                        token
                                    )
                                )
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
                        }
                    } catch (ex: Exception) {
                        ShowErrorDialog(usuario)
                    } finally{
                        loading = false
                    }
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    runOnUiThread{
                        ShowErrorDialog(usuario)
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    runOnUiThread{
                        ShowErrorDialog(usuario)
                    }
                }
            }
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticación requerida")
            .setSubtitle("Verifica tu identidad para iniciar sesión")
            .setDeviceCredentialAllowed(true)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun ShowErrorDialog(usuario: Usuario){
        AlertDialog.Builder(this)
            .setTitle("Ocurrió un error")
            .setMessage("No pudimos autenticarte. Te recomendamos iniciar sesión con usuario y contraseña.")
            .setPositiveButton("Reintentar"){
                dialog, _ ->
                dialog.dismiss()
                ShowBiometricPrompt(usuario)
            }
            .setNegativeButton("Iniciar sesión"){
                _, _ ->
                val intent = Intent(
                    this,
                    LoginActivity::class.java
                )
                startActivity(intent)
                finish()
            }
            .show()
    }
}