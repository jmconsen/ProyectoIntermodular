package com.example.proyectointermodular

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.proyectointermodular.R.string.default_web_client_id
import com.example.proyectointermodular.ui.theme.ProyectoIntermodularTheme
import com.example.proyectointermodular.ui.theme.screens.MainScreen

class MainActivity : ComponentActivity() {
    private lateinit var navHostController: NavHostController
    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configuración del inicio de sesión con Google
        val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            authManager.handleGoogleSignInResult(
                result.data,
                onSuccess = {
                    Log.d("MainActivity", "Inicio de sesión exitoso con Google.")
                    if (::navHostController.isInitialized) {
                        navHostController.navigate("PantallaMenu") {
                            popUpTo("PantallaLogin") { inclusive = true }
                        }
                    } else {
                        Log.e("MainActivity", "navHostController no inicializado")
                    }
                },
                onFailure = { exception ->
                    Log.e("MainActivity", "Error en autenticación de Google", exception)
                }
            )
        }

        // Inicializar AuthManager
        authManager = AuthManager(
            activity = this,
            clientId = getString(default_web_client_id),
            googleSignInLauncher = googleSignInLauncher
        )

        // Configurar UI
        enableEdgeToEdge()
        setContent {
            navHostController = rememberNavController()
            MainScreen(authManager = authManager, navController = navHostController)
        }
    }

    fun signInWithGoogle() {
        authManager.signInWithGoogle()
    }
}


/*
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProyectoIntermodularTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProyectoIntermodularTheme {
        Greeting("Android")
    }
}
*/