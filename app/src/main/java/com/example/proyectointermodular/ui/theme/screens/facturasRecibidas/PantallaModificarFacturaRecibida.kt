package com.example.proyectointermodular.ui.theme.screens.facturasRecibidas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyectointermodular.ui.theme.FondoPantallas
import com.example.proyectointermodular.viewmodel.FacturaViewModel

@Composable
fun PantallaModificarFacturaRecibida(
    id: String?,
    navHostController: NavHostController,
    facturaViewModel: FacturaViewModel = viewModel()
) {
    if (id == null) {
        LaunchedEffect(Unit) { navHostController.popBackStack() }
        return
    }

    val facturaExistente = facturaViewModel.facturasRecibidas.value?.find { it.id == id }

    if (facturaExistente == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Factura no encontrada")
        }
        return
    }

    // Estado de los valores editables
    var descripcion by remember { mutableStateOf(facturaExistente.descripcion) }
    var fechaRecepcion by remember { mutableStateOf(facturaExistente.fechaRecepcion) }
    var nombreEmisor by remember { mutableStateOf(facturaExistente.nombreEmisor) }
    var cifEmisor by remember { mutableStateOf(facturaExistente.cifEmisor) }
    var direccionEmisor by remember { mutableStateOf(facturaExistente.direccionEmisor) }
    var baseImponible by remember { mutableStateOf(facturaExistente.baseImponible.toString()) }
    var tipoIva by remember { mutableStateOf(facturaExistente.tipoIva.toString()) }
    var fechaPago by remember { mutableStateOf(facturaExistente.fechaPago ?: "") }
    var metodoPago by remember { mutableStateOf(facturaExistente.metodoPago ?: "") }
    var estado by remember { mutableStateOf(facturaExistente.estado) }

    // Calculado automáticamente
    val cuotaIva = (baseImponible.toDoubleOrNull() ?: 0.0) * (tipoIva.toDoubleOrNull() ?: 0.0) / 100
    val total = (baseImponible.toDoubleOrNull() ?: 0.0) + cuotaIva

    var mostrarDialogoExito by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(Brush.verticalGradient(colors = FondoPantallas))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Editar Factura Recibida", style = MaterialTheme.typography.headlineMedium)

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = fechaRecepcion,
                onValueChange = { fechaRecepcion = it },
                label = { Text("Fecha de Recepción") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = nombreEmisor,
                onValueChange = { nombreEmisor = it },
                label = { Text("Nombre del Emisor") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = cifEmisor,
                onValueChange = { cifEmisor = it },
                label = { Text("CIF del Emisor") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = direccionEmisor,
                onValueChange = { direccionEmisor = it },
                label = { Text("Dirección del Emisor") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = baseImponible,
                onValueChange = { baseImponible = it },
                label = { Text("Base Imponible") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = tipoIva,
                onValueChange = { tipoIva = it },
                label = { Text("IVA (%)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = fechaPago,
                onValueChange = { fechaPago = it },
                label = { Text("Fecha de Pago (Opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = metodoPago,
                onValueChange = { metodoPago = it },
                label = { Text("Método de Pago (Opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Cuota IVA: ${"%.2f".format(cuotaIva)}€")
            Text("Total: ${"%.2f".format(total)}€")

            OutlinedTextField(
                value = estado,
                onValueChange = { estado = it },
                label = { Text("Estado de la Factura") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val facturaActualizada = facturaExistente.copy(
                        descripcion = descripcion,
                        fechaRecepcion = fechaRecepcion,
                        nombreEmisor = nombreEmisor,
                        cifEmisor = cifEmisor,
                        direccionEmisor = direccionEmisor,
                        baseImponible = baseImponible.toDoubleOrNull() ?: 0.0,
                        tipoIva = tipoIva.toDoubleOrNull() ?: 0.0,
                        cuotaIva = cuotaIva,
                        total = total,
                        estado = estado,
                        fechaPago = fechaPago.ifBlank { null },
                        metodoPago = metodoPago.ifBlank { null }
                    )
                    facturaViewModel.actualizarFacturaRecibida(facturaActualizada)
                    mostrarDialogoExito = true
                },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Text("Guardar Cambios")
            }

            if (mostrarDialogoExito) {
                Snackbar(
                    action = {
                        TextButton(onClick = {
                            mostrarDialogoExito = false
                            navHostController.popBackStack()
                        }) { Text("Aceptar") }
                    }
                ) { Text("Factura actualizada correctamente.") }
            }
        }
    }
}