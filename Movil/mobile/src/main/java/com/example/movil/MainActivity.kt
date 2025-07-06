package com.example.movil

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.CircularProgressIndicator
import com.example.movil.daos.UsuarioDAO
import com.example.movil.services.StorageService
import com.example.movil.uis.HomeActivity
import com.example.movil.uis.LoginActivity
import com.example.movil.uis.SelectUserActivity

class MainActivity : AppCompatActivity() {
    private lateinit var context: Context
    private lateinit var usuarioDao: UsuarioDAO
    private lateinit var _storage: StorageService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        usuarioDao = UsuarioDAO(context)
        _storage = StorageService(this)
        setContent {
            Screen()
        }
        CheckForLoginUser()
    }

    @Composable
    @Preview
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = colorResource(R.color.white)
                    )
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Image(
                    painter = painterResource(id = R.drawable.logo_eco_track_sf),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .width(responsiveWidth)
                        .aspectRatio(1f)
                )
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

    private fun CheckForLoginUser(){
        try{
            val token = _storage.GetItem("token")
            if (token != null){
                val intent = Intent(
                    context,
                    HomeActivity::class.java
                )
                startActivity(intent)
                finish()
            } else{
                val users = usuarioDao.GetAll()
                if(users.isEmpty()){
                    val intent = Intent(
                        context,
                        LoginActivity::class.java
                    )
                    startActivity(intent)
                    finish()
                } else{
                    val intent = Intent(
                        context,
                        SelectUserActivity::class.java
                    )
                    intent.putParcelableArrayListExtra(
                        "Users",
                        ArrayList(users)
                    )
                    startActivity(intent)
                    finish()
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
}
