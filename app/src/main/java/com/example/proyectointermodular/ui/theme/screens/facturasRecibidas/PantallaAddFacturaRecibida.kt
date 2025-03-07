package com.example.proyectointermodular.ui.theme.screens.facturasRecibidas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyectointermodular.modelo.FacturaRecibida
import com.example.proyectointermodular.viewmodel.FacturaViewModel

@Composable
fun PantallaAddFacturaRecibida(
    navHostController: NavHostController,
    facturaViewModel: FacturaViewModel = viewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var facturaGuardada by remember { mutableStateOf(false) }

    // Datos de la factura recibida
    var descripcion by remember { mutableStateOf("") }
    var fechaRecepcion by remember { mutableStateOf("") }
    var nombreEmisor by remember { mutableStateOf("") }
    var cifEmisor by remember { mutableStateOf("") }
    var direccionEmisor by remember { mutableStateOf("") }
    var baseImponibleText by remember { mutableStateOf("") }
    var tipoIvaText by remember { mutableStateOf("") }
    var fechaPago by remember { mutableStateOf("") }
    var metodoPago by remember { mutableStateOf("") }

    val baseImponible = baseImponibleText.toDoubleOrNull() ?: 0.0
    val tipoIva = tipoIvaText.toDoubleOrNull() ?: 0.0
    val cuotaIva = baseImponible * (tipoIva / 100)
    val total = baseImponible + cuotaIva

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") }
            )
            OutlinedTextField(
                value = fechaRecepcion,
                onValueChange = { fechaRecepcion = it },
                label = { Text("Fecha de Recepción") }
            )
            OutlinedTextField(
                value = nombreEmisor,
                onValueChange = { nombreEmisor = it },
                label = { Text("Nombre del Emisor") }
            )
            OutlinedTextField(
                value = cifEmisor,
                onValueChange = { cifEmisor = it },
                label = { Text("CIF del Emisor") }
            )
            OutlinedTextField(
                value = direccionEmisor,
                onValueChange = { direccionEmisor = it },
                label = { Text("Dirección del Emisor") }
            )
            OutlinedTextField(
                value = baseImponibleText,
                onValueChange = { baseImponibleText = it },
                label = { Text("Base Imponible") }
            )
            OutlinedTextField(
                value = tipoIvaText,
                onValueChange = { tipoIvaText = it },
                label = { Text("Tipo IVA (%)") }
            )
            OutlinedTextField(
                value = fechaPago,
                onValueChange = { fechaPago = it },
                label = { Text("Fecha de Pago (Opcional)") }
            )
            OutlinedTextField(
                value = metodoPago,
                onValueChange = { metodoPago = it },
                label = { Text("Método de Pago (Opcional)") }
            )
            Text(text = "Cuota IVA: $cuotaIva")
            Text(text = "Total: $total")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                if (descripcion.isNotBlank() && fechaRecepcion.isNotBlank() &&
                    nombreEmisor.isNotBlank() && cifEmisor.isNotBlank() && direccionEmisor.isNotBlank() &&
                    baseImponibleText.isNotBlank() && tipoIvaText.isNotBlank()
                ) {
                    val factura = FacturaRecibida(
                        id = "",
                        descripcion = descripcion,
                        fechaRecepcion = fechaRecepcion,
                        nombreEmisor = nombreEmisor,
                        cifEmisor = cifEmisor,
                        direccionEmisor = direccionEmisor,
                        baseImponible = baseImponible,
                        tipoIva = tipoIva,
                        cuotaIva = cuotaIva,
                        total = total,
                        estado = "Pendiente",
                        fechaPago = fechaPago.ifBlank { null },
                        metodoPago = metodoPago.ifBlank { null }
                    )
                    facturaViewModel.agregarFacturaRecibida(factura, navHostController)
                    facturaGuardada = true
                }
            }) {
                Text("Guardar Factura")
            }
        }
        LaunchedEffect(facturaGuardada) {
            if (facturaGuardada) {
                snackbarHostState.showSnackbar("Factura creada exitosamente")
                facturaGuardada = false
            }
        }
    }
}