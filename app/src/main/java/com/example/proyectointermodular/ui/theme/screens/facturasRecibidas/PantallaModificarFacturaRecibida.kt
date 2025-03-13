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
import com.example.proyectointermodular.modelo.FacturaRecibida
import com.example.proyectointermodular.ui.theme.AzulOscuro
import com.example.proyectointermodular.ui.theme.FondoPantallas
import com.example.proyectointermodular.ui.theme.screens.facturasEmitidas.validarCamposFacturaModif
import com.example.proyectointermodular.viewmodel.FacturaViewModel
import java.text.NumberFormat
import java.util.Locale


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
    var baseImponible by remember { mutableStateOf(numberFormat.format(facturaExistente.baseImponible)) }
    //var tipoIva by remember { mutableStateOf(facturaExistente.tipoIva.toString()) }
    var fechaPago by remember { mutableStateOf(facturaExistente.fechaPago ?: "") }
    var metodoPago by remember { mutableStateOf(facturaExistente.metodoPago ?: "") }
    var estado by remember { mutableStateOf(facturaExistente.estado) }

    var expanded by remember { mutableStateOf(false) }

    // Calculado automáticamente
    //val cuotaIva = (baseImponible.toDoubleOrNull() ?: 0.0) * (tipoIva.toDoubleOrNull() ?: 0.0) / 100
    //val total = (baseImponible.toDoubleOrNull() ?: 0.0) + cuotaIva

    var mostrarDialogoExito by remember { mutableStateOf(false) }
    var mostrarDialogoError by remember { mutableStateOf(false) }
    var mensajeErrorValidacion by remember { mutableStateOf("") }

    var tipoIva by remember {
        mutableStateOf(
            when (facturaExistente.tipoIva) {
                21 -> "21%"
                10 -> "10%"
                4 -> "4%"
                else -> "Exento o 0%"
            }
        )
    }

    val tiposIva = listOf("21%", "10%", "4%", "Exento o 0%")

    val tipoIvaValue = when (tipoIva) {
        "21%" -> 21
        "10%" -> 10
        "4%" -> 4
        else -> 0
    }

    val baseImponibleFormateado = remember(baseImponible) {
        baseImponible.toDoubleOrNull()?.let {
            String.format(Locale.GERMANY, "%,.2f", it) // Formato con coma y punto en España
        } ?: ""
    }

    // Calcular cuota IVA de forma reactiva y Formatear con coma decimal (formato España)
    val cuotaIva by remember(baseImponible, tipoIva) {
        derivedStateOf {
            val base = baseImponible.replace(".", "").replace(",", ".").toDoubleOrNull() ?: 0.00
            val iva = when (tipoIva) {
                "21%" -> 0.21
                "10%" -> 0.10
                "4%" -> 0.04
                else -> 0.0
            }
            com.example.proyectointermodular.ui.theme.screens.facturasEmitidas.numberFormat.format(base * iva)
        }
    }

    // Calcular total de forma reactiva y Formatear con coma decimal (formato España)
    val total by remember(baseImponible, cuotaIva) {
        derivedStateOf {
            val base = baseImponible.replace(".", "").replace(",", ".").toDoubleOrNull() ?: 0.00
            val cuota = cuotaIva.replace(".", "").replace(",", ".").toDoubleOrNull() ?: 0.00
            com.example.proyectointermodular.ui.theme.screens.facturasEmitidas.numberFormat.format(base + cuota)
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
                    onValueChange = { newValue ->
                        // Permitir solo números y una única coma
                        val filteredValue = newValue.filter { it.isDigit() || it == ',' }

                        // Permitir solo una coma
                        val commaCount = filteredValue.count { it == ',' }
                        if (commaCount > 1) return@OutlinedTextField // No actualizar si hay más de una coma

                        // Verificar si hay coma y limitar a dos decimales
                        val parts = filteredValue.split(',')
                        val formattedValue = if (parts.size == 2) {
                            val integerPart = parts[0]
                            val decimalPart = parts[1].take(2) // Tomar solo los primeros dos decimales
                            "$integerPart,$decimalPart"
                        } else {
                            filteredValue
                        }

                        // Asignar el valor filtrado
                        baseImponible = formattedValue
                    },
                    label = { Text("Base Imponible") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                /*
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
                            val fechaPagoOriginal = fechaPago // O el valor original
                            val metodoPagoOriginal = metodoPago
                            val (esValido, mensajeError) = validarCamposFacturaModifRec(

                                numeroFactura = "1",
                                descripcion = descripcion,
                                fechaRecepcion = fechaRecepcion,
                                nombreEmisor = nombreEmisor,
                                cifEmisor = cifEmisor,
                                direccionEmisor = direccionEmisor,

                                baseImponible = baseImponible.replace(".", "")
                                    .replace(",", "."),
                                tipoIva = tipoIva,
                                cuotaIva = cuotaIva,
                                total = total,

                                estado = "Pendiente",
                                fechaPago = fechaPago,
                                metodoPago = metodoPago,
                                fechaPagoOriginal = fechaPagoOriginal, // Pasamos el valor original
                                metodoPagoOriginal = metodoPagoOriginal
                            )

                            if (!esValido) {
                                mensajeErrorValidacion = mensajeError ?: "Error desconocido"
                                mostrarDialogoError = true
                            } else {
                                val facturaActualizada = facturaExistente.copy(
                                //val factura = FacturaRecibida(
                                    id = "",
                                    descripcion = descripcion,
                                    fechaRecepcion = fechaRecepcion,
                                    nombreEmisor = nombreEmisor,
                                    cifEmisor = cifEmisor,
                                    direccionEmisor = direccionEmisor,

                                    baseImponible = baseImponible.replace(".", "").replace(",", ".").toDoubleOrNull() ?: 0.00,
                                    tipoIva = tipoIvaValue,
                                    cuotaIva = cuotaIva.replace(".", "").replace(",", ".").toDoubleOrNull() ?: 0.00,
                                    total = total.replace(".", "").replace(",", ".").toDoubleOrNull() ?: 0.00,

                                    estado = "Pendiente",
                                    fechaPago = fechaPago.ifBlank { null },
                                    metodoPago = metodoPago.ifBlank { null }
                                )

                                facturaViewModel.agregarFacturaRecibida(facturaActualizada, navHostController)
                                mostrarDialogoExito = true // Mostrar diálogo de éxito
                                //facturaGuardada = true
                            }

                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AzulOscuro
                        )
                    ) {
                        Text("Guardar")
                    }

                    // Diálogo de éxito al guardar la factura
                    if (mostrarDialogoExito) {
                        AlertDialog(
                            onDismissRequest = { /* Evitar que el usuario lo cierre sin aceptar */ },
                            title = { Text("Factura Guardada") },
                            text = { Text("La factura se ha guardado correctamente.") },
                            confirmButton = {
                                Button(onClick = {
                                    mostrarDialogoExito = false
                                    navHostController.navigate("PantallaFacturasEmitidas") // Redirigir tras aceptar
                                }) {
                                    Text("Aceptar")
                                }
                            }
                        )
                    }

                    // 🔹 Diálogo de error si la validación falla
                    if (mostrarDialogoError) {
                        AlertDialog(
                            onDismissRequest = { mostrarDialogoError = false },
                            title = { Text("Error de validación") },
                            text = { Text(mensajeErrorValidacion) },
                            confirmButton = {
                                Button(onClick = { mostrarDialogoError = false }) {
                                    Text("Aceptar")
                                }
                            }
                        )
                    }
                }

                /*
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

                 */

                Spacer(modifier = Modifier.height(64.dp))
            }
        }
    }
}

fun validarCamposFacturaModifRec(
    numeroFactura: String,
    descripcion: String,
    fechaRecepcion: String,
    nombreEmisor: String,
    cifEmisor: String,
    direccionEmisor: String,
    baseImponible: String,
    tipoIva: String,
    cuotaIva: String,
    total: String,
    estado: String,
    fechaPago: String?,
    metodoPago: String?,
    fechaPagoOriginal: String?,
    metodoPagoOriginal: String?

): Pair<Boolean, String?> {
    // Validación de campos obligatorios
    if (numeroFactura.isBlank() || descripcion.isBlank() ||
        fechaRecepcion.isBlank() || nombreEmisor.isBlank() ||
        cifEmisor.isBlank() || direccionEmisor.isBlank() ||
        baseImponible.isBlank() || tipoIva.isBlank() ||
        cuotaIva.isBlank() || total.isBlank() || estado.isBlank()) {
        return Pair(false, "Todos los campos son obligatorios.")
    }

    if (cifEmisor.length > 9) {

        return Pair(false, "El CIF no puede tener más de 9 caracteres.")
    }

    // Validación de longitudes máximas
    if (descripcion.length > 50 ||
        nombreEmisor.length > 50 ||
        direccionEmisor.length > 50 ) {
        return Pair(false, "Descripción, Nombre y Dirección no pueden exceder los 50 caracteres.")
    }

    // Validación de formato de fecha (dd/mm/aaaa)
    val fechaRegex = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/([12]\\d{3})$".toRegex()

    if (!fechaRecepcion.matches(fechaRegex)) {
        return Pair(false, "El formato de la fecha debe ser dd/mm/aaaa.")
    }

    // Validación del CIF (común para facturas)
    val cifRegex = "^[ABCDEFGHJNPQRSUVW][0-9]{7}[0-9A-J]$".toRegex()
    val dniRegex = "^[0-9]{8}[A-HJ-NP-TV-Z]$".toRegex() // DNI español

    if (!cifEmisor.matches(cifRegex) && !cifEmisor.matches(dniRegex)) {
        return Pair(false, "El CIF o DNI del emisor no es válido.")
    }

    // Validación de longitud y contenido de Descripción, Nombre y Dirección
    if (descripcion.length < 2 ||
        nombreEmisor.length < 2 ||
        direccionEmisor.length < 2) {

        return Pair(false, "Descripción, Nombre, Dirección y Método de Pago deben tener al menos 2 caracteres.")
    }

    /*
    // Validación de los valores numéricos (base imponible, cuota IVA y total)
    if (baseImponibleText.toDoubleOrNull() == null || cuotaIva.toDoubleOrNull() == null || total.toDoubleOrNull() == null) {
        return Pair(false, "La base imponible, cuota IVA y total deben ser valores numéricos válidos.")
    }
    */

    // Validación de la fecha de pago solo si se ha modificado
    if (fechaPago != fechaPagoOriginal && !fechaPago.isNullOrBlank()) {
        if (!fechaPago.matches(fechaRegex)) {
            return Pair(false, "El formato de la fecha de pago debe ser dd/mm/aaaa.")
        }
    }

    // Validación de metodo de pago solo si se ha modificado
    if (metodoPago != metodoPagoOriginal && !metodoPago.isNullOrBlank()) {
        if (metodoPago !in listOf("Efectivo", "Transferencia", "Tarjeta", "Otro")) {
            return Pair(false, "El método de pago debe ser uno de los siguientes: Efectivo, Transferencia, Tarjeta, Otro.")
        }
    }

    return Pair(true, null)
}