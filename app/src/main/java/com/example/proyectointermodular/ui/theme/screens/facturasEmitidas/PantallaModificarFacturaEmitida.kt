package com.example.proyectointermodular.ui.theme.screens.facturasEmitidas

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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

val numberFormat = NumberFormat.getNumberInstance(Locale("es", "ES")).apply {
    maximumFractionDigits = 2 // Máximo 2 decimales
    minimumFractionDigits = 2 // Mínimo 2 decimales
}

@OptIn(ExperimentalMaterial3Api::class)
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

    val snackbarHostState = remember { SnackbarHostState() }
    val facturaExistente = facturaViewModel.facturasEmitidas.value?.find { it.id == id }

    if (facturaExistente == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Factura no encontrada")
        }
        return
    }

    // Estado de los valores editables
    var descripcion by remember { mutableStateOf(facturaExistente.descripcion) }
    var fechaEmision by remember { mutableStateOf(facturaExistente.fechaEmision) }
    var nombreReceptor by remember { mutableStateOf(facturaExistente.nombreReceptor) }
    var cifReceptor by remember { mutableStateOf(facturaExistente.cifReceptor) }
    var direccionReceptor by remember { mutableStateOf(facturaExistente.direccionReceptor) }
    //var baseImponible by remember { mutableStateOf(facturaExistente.baseImponible.toString()) }
    var baseImponible by remember { mutableStateOf(numberFormat.format(facturaExistente.baseImponible)) }
    //var tipoIva by remember { mutableStateOf(facturaExistente.tipoIva.toString()) }
    var expanded by remember { mutableStateOf(false) }
    var estado by remember { mutableStateOf(facturaExistente.estado) }

    var proyectos by remember { mutableStateOf(listOf<String>()) }
    var proyectoSeleccionado by remember { mutableStateOf(facturaExistente.proyecto) }
    var expandedProyectos by remember { mutableStateOf(false) }

    // Calculado automáticamente
    //val cuotaIva = (baseImponible.toDoubleOrNull() ?: 0.00) * (tipoIva.toDoubleOrNull() ?: 0.00) / 100
    //val total = (baseImponible.toDoubleOrNull() ?: 0.00) + cuotaIva

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
            numberFormat.format(base * iva)
        }
    }

    // Calcular total de forma reactiva y Formatear con coma decimal (formato España)
    val total by remember(baseImponible, cuotaIva) {
        derivedStateOf {
            val base = baseImponible.replace(".", "").replace(",", ".").toDoubleOrNull() ?: 0.00
            val cuota = cuotaIva.replace(".", "").replace(",", ".").toDoubleOrNull() ?: 0.00
            numberFormat.format(base + cuota)
        }
    }

    // Obtener proyectos desde Firebase
    LaunchedEffect(Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("proyectos")
            .get()
            .addOnSuccessListener { result ->
                proyectos = result.map { it.getString("nombre") ?: "" }
            }
            .addOnFailureListener { exception ->
                CoroutineScope(Dispatchers.Main).launch {
                    snackbarHostState.showSnackbar("Error al obtener proyectos: ${exception.message}")
                }
            }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },

        topBar = {
            TopAppBar(
                title = { Text("Modificar Factura Emitida") },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack("PantallaFacturasEmitidas", inclusive = false) }) {
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
                Text("Editar Factura", style = MaterialTheme.typography.headlineMedium)
                */

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

                 */

                ExposedDropdownMenuBox(
                    expanded = expandedProyectos,
                    onExpandedChange = { expandedProyectos = it }
                ) {
                    OutlinedTextField(
                        value = proyectoSeleccionado,
                        onValueChange = { },
                        label = { Text("Proyecto") },
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedProyectos,
                        onDismissRequest = { expandedProyectos = false }
                    ) {
                        proyectos.forEach { proyecto ->
                            DropdownMenuItem(
                                text = { Text(proyecto) },
                                onClick = {
                                    proyectoSeleccionado = proyecto
                                    expandedProyectos = false
                                }
                            )
                        }
                    }
                }



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

                //Text("Cuota IVA: ${"%.2f".format(cuotaIva)}€")
                //Text("Total: ${"%.2f".format(total)}€")

                OutlinedTextField(
                    value = cuotaIva,
                    onValueChange = {},
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
                            navHostController.navigate("PantallaFacturasEmitidas")
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
                            // Validar los campos antes de proceder
                            val (esValido, mensajeError) = validarCamposFacturaModif(
                                numeroFactura = "1",  // Aquí puedes usar el número de factura real
                                descripcion = descripcion,
                                fechaEmision = fechaEmision,
                                nombreReceptor = nombreReceptor,
                                cifReceptor = cifReceptor,
                                direccionReceptor = direccionReceptor,
                                baseImponible = baseImponible.replace(".", "")
                                    .replace(",", "."),
                                tipoIva = tipoIva,
                                cuotaIva = cuotaIva.replace(".", "").replace(",", "."),
                                total = total.replace(".", "").replace(",", "."),
                                estado = estado,
                                proyecto = proyectoSeleccionado
                            )

                            if (!esValido) {
                                // Mostrar el mensaje de error de validación si no es válido
                                mensajeErrorValidacion = mensajeError ?: "Error desconocido"
                                mostrarDialogoError = true
                            } else {
                                // Si la validación es exitosa, actualizar la factura
                                val facturaActualizada = facturaExistente.copy(
                                    descripcion = descripcion,
                                    fechaEmision = fechaEmision,
                                    nombreReceptor = nombreReceptor,
                                    cifReceptor = cifReceptor,
                                    direccionReceptor = direccionReceptor,
                                    baseImponible = baseImponible.replace(".", "").replace(",", ".")
                                        .toDoubleOrNull() ?: 0.00,
                                    tipoIva = tipoIvaValue,
                                    cuotaIva = cuotaIva.replace(".", "").replace(",", ".")
                                        .toDoubleOrNull() ?: 0.00,
                                    total = total.replace(".", "").replace(",", ".")
                                        .toDoubleOrNull() ?: 0.00,
                                    estado = estado,
                                    proyecto = proyectoSeleccionado
                                )

                                // Actualizar la factura en la base de datos o en el ViewModel
                                facturaViewModel.actualizarFacturaEmitida(facturaActualizada)
                                mostrarDialogoExito = true // Mostrar diálogo de éxito
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = AzulOscuro)
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
                Spacer(modifier = Modifier.height(64.dp))
            }
        }
    }
}


fun validarCamposFacturaModif(
    //id: Int,
    numeroFactura: String,
    descripcion: String,
    fechaEmision: String,
    nombreReceptor: String,
    cifReceptor: String,
    direccionReceptor: String,
    baseImponible: String,
    tipoIva: String,
    cuotaIva: String,
    total: String,
    estado: String,
    proyecto: String



): Pair<Boolean, String?> {
    // Validación de campos obligatorios y longitud máxima
    if (numeroFactura.isBlank() || descripcion.isBlank() ||
        fechaEmision.isBlank() || nombreReceptor.isBlank() ||
        cifReceptor.isBlank() || direccionReceptor.isBlank() ||
        baseImponible.isBlank() || tipoIva.isBlank() ||
        cuotaIva.isBlank() ||  total.isBlank() ||
        estado.isBlank() || proyecto.isBlank()

    ) {
        return Pair(false, "Todos los campos son obligatorios.")
    }
    if (cifReceptor.length > 9) {

        return Pair(false, "El CIF no puede tener más de 9 caracteres.")
    }
    if (descripcion.length > 50 ||
        nombreReceptor.length > 50 ||
        direccionReceptor.length > 50) {

        return Pair(false, "Descripción, Nombre y Dirección no pueden exceder los 50 caracteres.")
    }

    // Validación de la fecha en formato dd/mm/aaaa
    val fechaRegex = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/([12]\\d{3})$".toRegex()
    if (!fechaEmision.matches(fechaRegex)) {
        return Pair(false, "El formato de la fecha debe ser dd/mm/aaaa.")
    }

    // Validación de CIF y DNI
    val cifRegex = "^[ABCDEFGHJNPQRSUVW][0-9]{7}[0-9A-J]$".toRegex()
    val dniRegex = "^[0-9]{8}[A-HJ-NP-TV-Z]$".toRegex() // DNI español

    if (!cifReceptor.matches(cifRegex) && !cifReceptor.matches(dniRegex)) {
        return Pair(false, "El CIF o DNI introducido no es válido.")
    }

    // Validación de longitud y contenido de Descripción, Nombre y Dirección
    if (descripcion.length < 2 ||
        nombreReceptor.length < 2 ||
        direccionReceptor.length < 2) {

        return Pair(false, "Descripción, Nombre y Dirección deben tener al menos 2 caracteres.")
    }

    /*
    if (descripcion.any { it.isDigit() } ||
        nombreReceptor.any { it.isDigit() }
    ) {
        return Pair(false, "Descripción y Nombre no deben contener números.")
    }
    */

    return Pair(true, null)
}