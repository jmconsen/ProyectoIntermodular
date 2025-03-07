package com.example.proyectointermodular

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.proyectointermodular.ui.theme.screens.PantallaLogin
import com.example.proyectointermodular.ui.theme.screens.PantallaMenu
import com.example.proyectointermodular.ui.theme.screens.PantallaRegistro
import com.example.proyectointermodular.ui.theme.screens.facturas.PantallaFormularioFacturas
import com.example.proyectointermodular.ui.theme.screens.facturasRecibidas.PantallaFormularioFacturasRecibidas
import com.example.proyectointermodular.ui.theme.viewmodel.FacturaViewModel

@Composable
fun NavigationApp(navHostController: NavHostController, authManager: AuthManager) {
    val startDestination = if (authManager.isUserLoggedIn()) "PantallaMenu" else "PantallaLogin"

    NavHost(
        navController = navHostController,
        startDestination = startDestination,
    ) {
        composable("PantallaMenu") { PantallaMenu(navHostController) }

        // Facturas
        composable("PantallaFormularioFacturas") {
            PantallaFormularioFacturas(navHostController = navHostController, facturaViewModel = viewModel<FacturaViewModel>())
        }
        composable("PantallaFormularioFacturasRecibidas") {
            PantallaFormularioFacturasRecibidas(navHostController = navHostController, facturaRecibidaViewModel = viewModel<FacturaRecibidaViewModel>())
        }




        // Login / Registro
        composable("PantallaLogin") { PantallaLogin(navHostController) }
        composable("PantallaRegistro") { PantallaRegistro(navHostController) }
    }
}

        /*
        //EMPLEADO
        composable("PantallaAddEmpleado") { PantallaAddEmpleado (navHostController) }
        //composable("PantallaFormularioEmpleados") { PantallaFormularioEmpleados (navHostController) }
        composable("PantallaAgenda") { PantallaAgenda (navHostController)}

        composable("PantallaFormularioEmpleados") {
            PantallaFormularioEmpleados(
                navHostController = navHostController,
                empleadoViewModel = viewModel()
            )
        }
        composable(
            route = "PantallaModificarEmpleado/{idEmpleado}",
            arguments = listOf(navArgument("idEmpleado") { type = NavType.StringType })
        ) { backStackEntry ->
            val idEmpleado = backStackEntry.arguments?.getString("idEmpleado")
            PantallaModificarEmpleado(
                idEmpleado = idEmpleado,
                navHostController = navHostController
            )
        }


        //VENTAS
        composable("PantallaDashboardVentas") { PantallaDashboardVentas ( viewModel = VentaViewModel(), navHostController = navHostController) }
        composable("PantallaVentas") { PantallaVentas(ventaViewModel = viewModel(), navHostController = navHostController) }
        composable("PantallaAddVentas") { PantallaAddVentas(ventaViewModel = viewModel(), productoViewModel = viewModel(), navHostController = navHostController) }

        //PRODUCTOS
        composable("PantallaAddProducto") { PantallaAddProducto(navHostController = navHostController, productoViewModel = viewModel()) }
        composable("PantallaFormularioProductos") { PantallaFormularioProductos (navHostController, productoViewModel = viewModel()) }




    }
}
  */