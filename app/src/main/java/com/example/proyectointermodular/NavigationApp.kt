package com.example.proyectointermodular

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.proyectointermodular.ui.theme.screens.MainScreen
import com.example.proyectointermodular.ui.theme.screens.PantallaLogin
import com.example.proyectointermodular.ui.theme.screens.PantallaRegistro
import com.example.proyectointermodular.ui.theme.screens.facturas.PantallaAddFactura
import com.example.proyectointermodular.ui.theme.screens.facturas.PantallaFormularioFacturas
import com.example.proyectointermodular.ui.theme.screens.facturas.PantallaModificarFactura

@Composable
fun NavigationApp(navHostController: NavHostController, authManager: AuthManager, modifier: Modifier = Modifier) {

    val startDestination = if (authManager.isUserLoggedIn()) "PantallaFormularioFacturas" else "PantallaLogin"

    NavHost(
        navController = navHostController,
        startDestination = startDestination,
    ) {
        // FACTURAS
        composable("PantallaAddFactura") { PantallaAddFactura(navHostController) }
        composable("PantallaFormularioFacturas") {
            PantallaFormularioFacturas(
                navHostController = navHostController,
                facturaViewModel = viewModel()
            )
        }
        composable(
            route = "PantallaModificarFactura/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            PantallaModificarFactura(
                id = id,
                navHostController = navHostController
            )
        }


        //LOGIN REGISTRO
        composable("PantallaLogin") { PantallaLogin (navHostController) }
        composable("PantallaRegistro") { PantallaRegistro (navHostController) }

        composable("MainScreen") { MainScreen(authManager, navHostController) }



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


         */
        

    }
}