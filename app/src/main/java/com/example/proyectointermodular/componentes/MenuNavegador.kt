package com.example.proyectointermodular.componentes

import android.util.Log
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.RequestQuote
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.proyectointermodular.AuthManager
import com.example.proyectointermodular.ui.theme.AzulOscuro
import com.example.proyectointermodular.ui.theme.GrisOscuro2

@Composable
fun MenuNavegador(navController: NavHostController, authManager: AuthManager) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    Surface(
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        shadowElevation = 6.dp,
        modifier = Modifier.height(98.dp).padding(top = 10.dp)
    ) {
        NavigationBar(
            containerColor = AzulOscuro,
        ) {


            // FACTURAS EMITIDAS
            NavigationBarItem(
                icon = {
                    Icon(
                        Icons.AutoMirrored.Filled.ReceiptLong,
                        contentDescription = "Facturas-emitidas",
                        tint = GrisOscuro2
                    )
                },
                label = {
                    Text(
                        "Facturas emitidas",
                        color = GrisOscuro2
                    )
                },
                selected = currentRoute == "PantallaFormularioFacturas",
                onClick = {
                    navController.navigate("PantallaFormularioFacturasEmitidas") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )

            // FACTURAS RECIBIDAS
            NavigationBarItem(
                icon = {
                    Icon(
                        Icons.Filled.RequestQuote,
                        contentDescription = "Facturas-recibidas",
                        tint = GrisOscuro2
                    )
                },
                label = {
                    Text(
                        "Facturas recibidas",
                        color = GrisOscuro2
                    )
                },
                selected = currentRoute == "PantallaFormularioFacturasRecibidas",
                onClick = {
                    navController.navigate("PantallaFormularioFacturasRecibidas") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )


            // CIERRE DE SESIÓN
            NavigationBarItem(
                icon = {
                    Icon(
                        Icons.Filled.ExitToApp,
                        contentDescription = "Salir",
                        tint = GrisOscuro2
                    )
                },
                label = {
                    Text(
                        "Salir",
                        color = GrisOscuro2,
                    )
                },
                selected = false,
                onClick = {
                    authManager.logout(
                        onSuccess = {
                            navController.navigate("PantallaLogin") {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        onFailure = { exception ->
                            Log.e("MenuNavegador", "Error al cerrar sesión", exception)
                        }
                    )
                }
            )
        }
    }
}