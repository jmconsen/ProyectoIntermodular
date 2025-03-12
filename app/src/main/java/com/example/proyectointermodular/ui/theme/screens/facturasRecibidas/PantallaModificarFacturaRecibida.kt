package com.example.proyectointermodular.ui.theme.screens.facturasRecibidas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyectointermodular.ui.theme.AzulOscuro
import com.example.proyectointermodular.ui.theme.FondoPantallas
import com.example.proyectointermodular.viewmodel.FacturaViewModel

@OptIn(ExperimentalMaterial3Api::class)
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

    val snackbarHostState = remember { SnackbarHostState() }
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

    var expanded by remember { mutableStateOf(false) }

    // Calculado automáticamente
    //val cuotaIva = (baseImponible.toDoubleOrNull() ?: 0.0) * (tipoIva.toDoubleOrNull() ?: 0.0) / 100
    //val total = (baseImponible.toDoubleOrNull() ?: 0.0) + cuotaIva

    var mostrarDialogoExito by remember { mutableStateOf(false) }

    val tiposIva = listOf("21%", "10%", "4%", "Exento o 0%")

    val tipoIvaValue = when (tipoIva) {
        "21%" -> 0.21
        "10%" -> 0.10
        "4%" -> 0.04
        else -> 0.0
    }


    // Calcular cuota IVA de forma reactiva
    val cuotaIva by remember(baseImponible, tipoIva) {
        derivedStateOf {
            val base = baseImponible.toDoubleOrNull() ?: 0.00
            val iva = when (tipoIva) {
                "21%" -> 0.21
                "10%" -> 0.10
                "4%" -> 0.04
                else -> 0.0
            }
            (base * iva).toString()
        }
    }

    // Calcular total de forma reactiva
    val total by remember(baseImponible, cuotaIva) {
        derivedStateOf {
            val base = baseImponible.toDoubleOrNull() ?: 0.00
            val cuota = cuotaIva.toDoubleOrNull() ?: 0.00
            (base + cuota).toString()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },

        topBar = {
            TopAppBar(
                title = { Text("Modificar Factura Recibida") },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack("PantallaFacturasRecibidas", inclusive = false) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AzulOscuro
                )
            )
        }

    ) { padding ->

        Box(
            modifier = Modifier.fillMaxSize()
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .background(Brush.verticalGradient(colors = FondoPantallas))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                //.verticalScroll(rememberScrollState())
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                /*
                Text("Editar Factura Recibida", style = MaterialTheme.typography.headlineMedium)
                */

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

                OutlinedTextField(
                    value = baseImponible,
                    onValueChange = { baseImponible = it },
                    label = { Text("Base Imponible") },
                    modifier = Modifier.fillMaxWidth()
                )

                /*
                OutlinedTextField(
                    value = tipoIva,
                    onValueChange = { tipoIva = it },
                    label = { Text("IVA (%)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Cuota IVA: ${"%.2f".format(cuotaIva)}€")
                Text("Total: ${"%.2f".format(total)}€")

                */

                // Desplegable para seleccionar el tipo de IVA
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = tipoIva,
                        onValueChange = { tipoIva = it },
                        label = { Text("Tipo IVA") },
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor() // Indica que este campo es un activador del menú
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        tiposIva.forEach { tipo ->
                            DropdownMenuItem(
                                text = { Text(tipo) },
                                onClick = {
                                    tipoIva = tipo
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = cuotaIva,
                    onValueChange = { cuotaIva },
                    label = { Text("Cuota IVA") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = total,
                    onValueChange = { total },
                    label = { Text("Total") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = estado,
                    onValueChange = { estado = it },
                    label = { Text("Estado de la Factura") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Button(
                        onClick = {
                            navHostController.navigate("PantallaFacturasRecibidas")
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AzulOscuro
                        )
                    ) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {
                            val facturaActualizada = facturaExistente.copy(
                                descripcion = descripcion,
                                fechaRecepcion = fechaRecepcion,
                                nombreEmisor = nombreEmisor,
                                cifEmisor = cifEmisor,
                                direccionEmisor = direccionEmisor,
                                baseImponible = baseImponible.toDoubleOrNull() ?: 0.00,
                                tipoIva = tipoIvaValue,
                                cuotaIva = cuotaIva.toDoubleOrNull() ?: 0.00,
                                total = total.toDoubleOrNull() ?: 0.00,
                                estado = estado,
                                fechaPago = fechaPago.ifBlank { null },
                                metodoPago = metodoPago.ifBlank { null }
                            )
                            facturaViewModel.actualizarFacturaRecibida(facturaActualizada)
                            mostrarDialogoExito = true
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AzulOscuro
                        )
                    ) {
                        Text("Guardar")
                    }
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

                Spacer(modifier = Modifier.height(64.dp))
            }
        }
    }
}