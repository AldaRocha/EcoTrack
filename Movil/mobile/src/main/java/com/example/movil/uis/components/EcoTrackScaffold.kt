package com.example.movil.uis.components

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.DrawerValue
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.example.movil.daos.UsuarioDAO
import com.example.movil.R
import com.example.movil.services.RestService
import com.example.movil.services.StorageService
import kotlinx.coroutines.launch

@Composable
fun EcoTrackScaffold(
    context: Context,
    title: String,
    currentActivity: ComponentActivity,
    content: @Composable (innerPadding: PaddingValues) -> Unit
){
    val drawerState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val usuarioDao = UsuarioDAO(context)
    val rest = RestService(context)
    val storage = StorageService(context)

    Scaffold(
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
        scaffoldState = drawerState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                drawerState.drawerState.open()
                            }
                        }
                    ) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Menú"
                        )
                    }
                },
                backgroundColor = colorResource(R.color.primary),
                contentColor = colorResource(R.color.white)
            )
        },
        drawerContent = {
            DrawerMenu(
                context = context,
                currentActivity = currentActivity,
                drawerState = drawerState,
                scope = scope,
                usuarioDao = usuarioDao,
                _storage = storage,
                _rest = rest
            )
        },
        content = content
    )
}