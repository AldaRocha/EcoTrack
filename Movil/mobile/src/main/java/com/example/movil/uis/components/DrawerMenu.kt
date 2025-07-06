package com.example.movil.uis.components

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.movil.MainActivity
import com.example.movil.daos.UsuarioDAO
import com.example.movil.models.AuthData
import com.example.movil.models.Bearer
import com.example.movil.models.Usuario
import com.example.movil.services.RestService
import com.example.movil.services.StorageService
import com.example.movil.uis.HomeActivity
import com.example.movil.uis.PerfilActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DrawerMenu(
    context: Context,
    currentActivity: ComponentActivity,
    drawerState: ScaffoldState,
    scope: CoroutineScope,
    usuarioDao: UsuarioDAO,
    _storage: StorageService,
    _rest: RestService
){
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ){
        Text(
            text = "EcoTrack",
            modifier = Modifier
                .fillMaxWidth(),
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center
        )
        Spacer(
            modifier = Modifier
                .height(24.dp)
        )
        fun Navigate(destination: Class<*>){
            scope.launch {
                drawerState.drawerState.close()
                val intent = Intent(
                    context,
                    destination
                )
                context.startActivity(intent)
                currentActivity.finish()
            }
        }
        Text(
            text = "Inicio",
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    Navigate(
                        HomeActivity::class.java
                    )
                }
                .padding(
                    vertical = 8.dp
                )
        )
        Text(
            text = "Perfil",
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    Navigate(
                        PerfilActivity::class.java
                    )
                }
                .padding(
                    vertical = 8.dp
                )
        )
        Text(
            text = "Cerrar sesión",
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    try{
                        currentActivity.lifecycleScope.launch{
                            val token = _storage.GetItem("token")
                            val authData = AuthData(_rest.Decode(token))
                            val res = _rest.Post(
                                "auth/logout",
                                Bearer(
                                    authData.UsuarioId,
                                    authData.sesion.sesionId
                                ),
                                false,
                                false
                            )

                            val message = res["message"] as? String

                            if (message != null){
                                Toast.makeText(
                                    context,
                                    message,
                                    Toast.LENGTH_LONG
                                ).show()
                            } else{
                                val existe = usuarioDao.GetByUser(authData.Usuario)!!
                                usuarioDao.Update(
                                    Usuario(
                                        authData.UsuarioId,
                                        existe.Usuario,
                                        existe.Contrasena,
                                        null
                                    )
                                )
                                _storage.ClearSession()
                                val intent = Intent(
                                    context,
                                    MainActivity::class.java
                                )
                                context.startActivity(intent)
                                currentActivity.finish()
                            }
                        }
                    } catch (ex: Exception){
                        Toast.makeText(
                            context,
                            ex.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                .padding(
                    vertical = 8.dp
                )
        )
    }
}