package com.example.proyectointermodular.ui.theme.screens.facturasRecibidas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyectointermodular.modelo.FacturaRecibida
import com.example.proyectointermodular.ui.theme.AzulOscuro
import com.example.proyectointermodular.ui.theme.FondoPantallas
import com.example.proyectointermodular.viewmodel.FacturaViewModel
import java.text.NumberFormat
import java.util.Locale

val numberFormat = NumberFormat.getNumberInstance(Locale("es", "ES")).apply {
    maximumFractionDigits = 2 // Máximo 2 decimales
    minimumFractionDigits = 2 // Mínimo 2 decimales
}

@OptIn(ExperimentalMaterial3Api::class)
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
    var tipoIva by remember { mutableStateOf("21%") }
    var fechaPago by remember { mutableStateOf("") }
    var metodoPago by remember { mutableStateOf("") }
    val baseImponible = baseImponibleText.toDoubleOrNull() ?: 0.00

    var expanded by remember { mutableStateOf(false) }

    //val tipoIva = tipoIvaText.toDoubleOrNull() ?: 0.0
    //val cuotaIva = baseImponible * (tipoIva / 100)
    //val total = baseImponible + cuotaIva

    // Formatear base imponible con coma decimal (formato España)
    val baseImponibleFormateado = remember(baseImponibleText) {
        baseImponibleText.toDoubleOrNull()?.let {
            String.format(Locale.GERMANY, "%,.2f", it) // Formato con coma y punto en España
        } ?: ""
    }

    val tiposIva = listOf("21%", "10%", "4%", "Exento o 0%")

    val tipoIvaValue = when (tipoIva) {
        "21%" -> 21
        "10%" -> 10
        "4%" -> 4
        else -> 0
    }

    // Calcular cuota IVA de forma reactiva y Formatear con coma decimal (formato España)
    val cuotaIva by remember(baseImponibleText, tipoIva) {
        derivedStateOf {
            val base = baseImponibleText.replace(".", "").replace(",", ".").toDoubleOrNull() ?: 0.00
            val iva = when (tipoIva) {
                "21%" -> 0.21
                "10%" -> 0.10
                "4%" -> 0.04
                else -> 0.0
            }
            numberFormat.format(base * iva)
        }
    }

    // Calcular total de forma reactiva y Formatear con coma decimal (formato España)
    val total by remember(baseImponibleText, cuotaIva) {
        derivedStateOf {
            val base = baseImponibleText.replace(".", "").replace(",", ".").toDoubleOrNull() ?: 0.00
            val cuota = cuotaIva.replace(".", "").replace(",", ".").toDoubleOrNull() ?: 0.00
            numberFormat.format(base + cuota)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },

        topBar = {
            TopAppBar(
                title = { Text("Añadir FacturaRecibida") },
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
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .background(Brush.verticalGradient(colors = FondoPantallas))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

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
                    value = baseImponibleText,
                    onValueChange = { baseImponibleText = it },
                    label = { Text("Base Imponible") },
                    modifier = Modifier.fillMaxWidth()
                )


                // Desplegable para seleccionar el tipo de IVA
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = tipoIva,
                        onValueChange = { },
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

                /*
                OutlinedTextField(
                    value = tipoIva,
                    onValueChange = { tipoIva = it },
                    label = { Text("Tipo IVA (%)") },
                    modifier = Modifier.fillMaxWidth()
                )
                */

                OutlinedTextField(
                    value = cuotaIva,
                    onValueChange = { },
                    label = { Text("Cuota IVA") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = total,
                    onValueChange = { },
                    label = { Text("Total") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )

                /*
                Text(text = "Cuota IVA: $cuotaIva")
                Text(text = "Total: $total")
                */

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
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

                    Button(onClick = {
                        if (descripcion.isNotBlank() && fechaRecepcion.isNotBlank() &&
                            nombreEmisor.isNotBlank() && cifEmisor.isNotBlank() && direccionEmisor.isNotBlank() &&
                            baseImponibleText.isNotBlank() && tipoIva.isNotBlank()
                        ) {
                            val factura = FacturaRecibida(
                                id = "",
                                descripcion = descripcion,
                                fechaRecepcion = fechaRecepcion,
                                nombreEmisor = nombreEmisor,
                                cifEmisor = cifEmisor,
                                direccionEmisor = direccionEmisor,

                                baseImponible = baseImponibleText.replace(".", "").replace(",", ".").toDoubleOrNull() ?: 0.00,
                                tipoIva = tipoIvaValue,
                                cuotaIva = cuotaIva.replace(".", "").replace(",", ".").toDoubleOrNull() ?: 0.00,
                                total = total.replace(".", "").replace(",", ".").toDoubleOrNull() ?: 0.00,

                                estado = "Pendiente",
                                fechaPago = fechaPago.ifBlank { null },
                                metodoPago = metodoPago.ifBlank { null }
                            )
                            facturaViewModel.agregarFacturaRecibida(factura, navHostController)
                            facturaGuardada = true
                        }
                    },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AzulOscuro
                        )
                    ) {
                        Text("Guardar")
                    }
                }

                Spacer(modifier = Modifier.height(64.dp))

                LaunchedEffect(facturaGuardada) {
                    if (facturaGuardada) {
                        snackbarHostState.showSnackbar("Factura creada exitosamente")
                        facturaGuardada = false
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(64.dp))
    }
}