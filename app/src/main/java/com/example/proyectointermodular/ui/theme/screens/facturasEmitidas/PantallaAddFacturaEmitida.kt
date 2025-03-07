package com.example.proyectointermodular.ui.theme.screens.facturasEmitidas

import androidx.compose.foundation.layout.*
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyectointermodular.modelo.FacturaEmitida
import com.example.proyectointermodular.viewmodel.FacturaViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun PantallaAddFacturaEmitida(
    navHostController: NavHostController,
    facturaViewModel: FacturaViewModel = viewModel())
{
    val scaffoldState = rememberScaffoldState()
    var numeroFactura by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fechaEmision by remember { mutableStateOf("") }
    var nombreReceptor by remember { mutableStateOf("") }
    var cifReceptor by remember { mutableStateOf("") }
    var direccionReceptor by remember { mutableStateOf("") }
    var baseImponibleText by remember { mutableStateOf("") }
    var tipoIvaText by remember { mutableStateOf("") }

    val baseImponible = baseImponibleText.toDoubleOrNull() ?: 0.0
    val tipoIva = tipoIvaText.toDoubleOrNull() ?: 0.0
    val cuotaIva = baseImponible * (tipoIva / 100)
    val total = baseImponible + cuotaIva

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = remember { SnackbarHostState() }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = numeroFactura,
                onValueChange = { numeroFactura = it },
                label = { Text("Número de Factura") }
            )
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") }
            )
            OutlinedTextField(
                value = fechaEmision,
                onValueChange = { fechaEmision = it },
                label = { Text("Fecha de Emisión") }
            )
            OutlinedTextField(
                value = nombreReceptor,
                onValueChange = { nombreReceptor = it },
                label = { Text("Nombre del Receptor") }
            )
            OutlinedTextField(
                value = cifReceptor,
                onValueChange = { cifReceptor = it },
                label = { Text("CIF del Receptor") }
            )
            OutlinedTextField(
                value = direccionReceptor,
                onValueChange = { direccionReceptor = it },
                label = { Text("Dirección del Receptor") }
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
            Text(text = "Cuota IVA: $cuotaIva")
            Text(text = "Total: $total")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                if (numeroFactura.isNotBlank() && descripcion.isNotBlank() && fechaEmision.isNotBlank() &&
                    nombreReceptor.isNotBlank() && cifReceptor.isNotBlank() && direccionReceptor.isNotBlank() &&
                    baseImponibleText.isNotBlank() && tipoIvaText.isNotBlank()
                ) {
                    val factura = FacturaEmitida(
                        id = "",
                        numeroFactura = numeroFactura,
                        descripcion = descripcion,
                        fechaEmision = fechaEmision,
                        nombreReceptor = nombreReceptor,
                        cifReceptor = cifReceptor,
                        direccionReceptor = direccionReceptor,
                        baseImponible = baseImponible,
                        tipoIva = tipoIva,
                        cuotaIva = cuotaIva,
                        total = total,
                        estado = "Pendiente"
                    )
                    facturaViewModel.agregarFacturaEmitida(factura)
                    CoroutineScope(Dispatchers.Main).launch {
                        scaffoldState.snackbarHostState.showSnackbar("Factura creada exitosamente")
                    }
                }
            }) {
                Text("Guardar Factura")
            }
        }
    }
}

