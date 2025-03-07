package com.example.proyectointermodular.ui.theme.screens.facturasEmitidas

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
fun PantallaModificarFacturaEmitida(
    id: String?,
    navHostController: NavHostController,
    facturaViewModel: FacturaViewModel = viewModel()
) {
    if (id == null) {
        LaunchedEffect(Unit) { navHostController.popBackStack() }
        return
    }

    val facturaExistente = facturaViewModel.facturasEmitidas.value?.find { it.id == id }

    if (facturaExistente == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Factura no encontrada")
        }
        return
    }

    // Estado de los valores editables
    var numeroFactura by remember { mutableStateOf(facturaExistente.numeroFactura) }
    var descripcion by remember { mutableStateOf(facturaExistente.descripcion) }
    var fechaEmision by remember { mutableStateOf(facturaExistente.fechaEmision) }
    var nombreReceptor by remember { mutableStateOf(facturaExistente.nombreReceptor) }
    var cifReceptor by remember { mutableStateOf(facturaExistente.cifReceptor) }
    var direccionReceptor by remember { mutableStateOf(facturaExistente.direccionReceptor) }
    var baseImponible by remember { mutableStateOf(facturaExistente.baseImponible.toString()) }
    var tipoIva by remember { mutableStateOf(facturaExistente.tipoIva.toString()) }
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
            Text("Editar Factura", style = MaterialTheme.typography.headlineMedium)

            OutlinedTextField(
                value = numeroFactura.toString(),  // Convertimos el número a String para mostrarlo
                onValueChange = { nuevoValor ->
                    numeroFactura = nuevoValor.toIntOrNull() ?: 0  // Convertimos de nuevo a Int
                },
                label = { Text("Número Factura") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number) // Forzar teclado numérico
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = fechaEmision,
                onValueChange = { fechaEmision = it },
                label = { Text("Fecha de emisión") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = nombreReceptor,
                onValueChange = { nombreReceptor = it },
                label = { Text("Nombre del Cliente") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = cifReceptor,
                onValueChange = { cifReceptor = it },
                label = { Text("CIF del Cliente") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = direccionReceptor,
                onValueChange = { direccionReceptor = it },
                label = { Text("Dirección del Cliente") },
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
                        numeroFactura = numeroFactura,
                        descripcion = descripcion,
                        fechaEmision = fechaEmision,
                        nombreReceptor = nombreReceptor,
                        cifReceptor = cifReceptor,
                        direccionReceptor = direccionReceptor,
                        baseImponible = baseImponible.toDoubleOrNull() ?: 0.0,
                        tipoIva = tipoIva.toDoubleOrNull() ?: 0.0,
                        cuotaIva = cuotaIva,
                        total = total,
                        estado = estado
                    )
                    facturaViewModel.actualizarFacturaEmitida(facturaActualizada)
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