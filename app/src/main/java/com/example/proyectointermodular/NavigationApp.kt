package com.example.proyectointermodular

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.proyectointermodular.ui.theme.screens.PantallaLogin
import com.example.proyectointermodular.ui.theme.screens.PantallaMenu
import com.example.proyectointermodular.ui.theme.screens.PantallaRegistro
import com.example.proyectointermodular.ui.theme.screens.facturasEmitidas.PantallaAddFacturaEmitida
import com.example.proyectointermodular.ui.theme.screens.facturasEmitidas.PantallaDetalleFacturaEmitida
import com.example.proyectointermodular.ui.theme.screens.facturasEmitidas.PantallaFacturasEmitidas
import com.example.proyectointermodular.ui.theme.screens.facturasEmitidas.PantallaModificarFacturaEmitida
import com.example.proyectointermodular.ui.theme.screens.facturasRecibidas.PantallaAddFacturaRecibida
import com.example.proyectointermodular.ui.theme.screens.facturasRecibidas.PantallaDetalleFacturaRecibida
import com.example.proyectointermodular.ui.theme.screens.facturasRecibidas.PantallaFacturasRecibidas
import com.example.proyectointermodular.ui.theme.screens.facturasRecibidas.PantallaModificarFacturaRecibida
import com.example.proyectointermodular.viewmodel.FacturaViewModel

@Composable
fun NavigationApp(
    navHostController: NavHostController,
    authManager: AuthManager,
    modifier: Modifier = Modifier) {
    val startDestination = if (authManager.isUserLoggedIn()) "PantallaMenu" else "PantallaLogin"

    NavHost(
        navController = navHostController,
        startDestination = startDestination,
    ) {
        composable("PantallaMenu") { PantallaMenu(navHostController) }

        // Facturas Emitidas
        composable("PantallaFacturasEmitidas") {
            PantallaFacturasEmitidas(navHostController = navHostController, facturaViewModel = viewModel<FacturaViewModel>())
        }
        composable("PantallaDetalleFacturaEmitida/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            PantallaDetalleFacturaEmitida(id, navHostController)
        }
        composable("PantallaModificarFacturaEmitida/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            PantallaModificarFacturaEmitida(id, navHostController)
        }
        composable("PantallaAddFacturaEmitida") {
            PantallaAddFacturaEmitida(navHostController = navHostController, facturaViewModel = viewModel<FacturaViewModel>())
        }

        // Facturas Recibidas
        composable("PantallaFacturasRecibidas") {
            PantallaFacturasRecibidas(navHostController = navHostController, facturaViewModel = viewModel<FacturaViewModel>())
        }
        composable("PantallaDetalleFacturaRecibida/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            PantallaDetalleFacturaRecibida(id, navHostController)
        }
        composable("PantallaModificarFacturaRecibida/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            PantallaModificarFacturaRecibida(id, navHostController)
        }
        composable("PantallaAddFacturaRecibida") {
            PantallaAddFacturaRecibida(navHostController = navHostController, facturaViewModel = viewModel<FacturaViewModel>())
        }

        // Login / Registro
        composable("PantallaLogin") { PantallaLogin(navHostController) }
        composable("PantallaRegistro") { PantallaRegistro(navHostController) }
    }
}
