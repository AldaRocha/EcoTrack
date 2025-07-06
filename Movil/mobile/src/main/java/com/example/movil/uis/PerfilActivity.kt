package com.example.movil.uis

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movil.R
import com.example.movil.uis.components.EcoTrackScaffold

class PerfilActivity : ComponentActivity() {
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        setContent {
            MaterialTheme {
                Screen()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun ScreenPreview(){
        MaterialTheme{
            Screen()
        }
    }

    @Composable
    fun Screen(){
        EcoTrackScaffold(
            context = context,
            title = "Perfil",
            currentActivity = this
        ) {
            padding ->
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                val screenWidth = maxWidth

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
                        text = "Pantalla Perfil"
                    )
                }
            }
        }
    }
}