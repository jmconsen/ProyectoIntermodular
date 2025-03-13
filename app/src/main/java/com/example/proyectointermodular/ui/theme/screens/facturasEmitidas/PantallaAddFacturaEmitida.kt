package com.example.proyectointermodular.ui.theme.screens.facturasEmitidas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyectointermodular.modelo.FacturaEmitida
import com.example.proyectointermodular.viewmodel.FacturaViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.input.KeyboardType
import com.example.proyectointermodular.ui.theme.AzulOscuro
import com.example.proyectointermodular.ui.theme.FondoPantallas
import com.google.firebase.firestore.FirebaseFirestore
import java.text.NumberFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAddFacturaEmitida(
    navHostController: NavHostController,
    facturaViewModel: FacturaViewModel = viewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var facturaGuardada by remember { mutableStateOf(false) }
    var descripcion by remember { mutableStateOf("") }
    var fechaEmision by remember { mutableStateOf("") }
    var nombreReceptor by remember { mutableStateOf("") }
    var cifReceptor by remember { mutableStateOf("") }
    var direccionReceptor by remember { mutableStateOf("") }
    var baseImponibleText by remember { mutableStateOf("") }
    var tipoIva by remember { mutableStateOf("21%") }
    var expanded by remember { mutableStateOf(false) }
    var mostrarDialogoExito by remember { mutableStateOf(false) }
    var mostrarDialogoError by remember { mutableStateOf(false) }
    var mensajeErrorValidacion by remember { mutableStateOf("") }

    var proyectos by remember { mutableStateOf(listOf<String>()) }
    var proyectoSeleccionado by remember { mutableStateOf("") }
    var proyectoSeleccionadoId by remember { mutableStateOf("") }
    var expandedProyectos by remember { mutableStateOf(false) }

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

    var proyectosMap by remember { mutableStateOf(mapOf<String, String>()) }

    // Obtener proyectos desde Firebase
    LaunchedEffect(Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("proyectos")
            .get()
            .addOnSuccessListener { result ->
                val map = mutableMapOf<String, String>()
                for (document in result) {
                    val id = document.id
                    val nombre = document.getString("nombre") ?: ""
                    map[id] = nombre
                }
                proyectosMap = map
            }
            .addOnFailureListener { exception ->
                CoroutineScope(Dispatchers.Main).launch {
                    snackbarHostState.showSnackbar("Error al obtener proyectos: ${exception.message}")
                }
            }
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
/*
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
*/
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },

        topBar = {
            TopAppBar(
                title = { Text("Añadir Factura Emitida") },
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

                /*
                Text(
                    "Añadir Nueva Factura",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
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
                    label = { Text("Fecha de Emisión") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = nombreReceptor,
                    onValueChange = { nombreReceptor = it },
                    label = { Text("Nombre del Receptor") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = cifReceptor,
                    onValueChange = { cifReceptor = it },
                    label = { Text("CIF del Receptor") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = direccionReceptor,
                    onValueChange = { direccionReceptor = it },
                    label = { Text("Dirección del Receptor") },
                    modifier = Modifier.fillMaxWidth()
                )
                /*
                OutlinedTextField(
                    value = baseImponibleText,
                    onValueChange = { baseImponibleText = it },
                    label = { Text("Base Imponible") },
                    modifier = Modifier.fillMaxWidth()
                )
                */

                OutlinedTextField(
                    value = baseImponibleText,
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
                        baseImponibleText = formattedValue
                    },
                    label = { Text("Base Imponible") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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

                // Desplegable para seleccionar el proyecto
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
                        proyectosMap.forEach { (id, nombre) ->
                            DropdownMenuItem(
                                text = { Text(nombre) },
                                onClick = {
                                    proyectoSeleccionado = nombre
                                    proyectoSeleccionadoId = id
                                    expandedProyectos = false
                                }
                            )
                        }
                    }
                }

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

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
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
                            val (esValido, mensajeError) = validarCamposFacturaAdd(
                                numeroFactura = "",
                                descripcion = descripcion,
                                fechaEmision = fechaEmision,
                                nombreReceptor = nombreReceptor,
                                cifReceptor = cifReceptor,
                                direccionReceptor = direccionReceptor,
                                baseImponibleText = baseImponibleText,
                                tipoIva = tipoIva,
                                cuotaIva = cuotaIva,
                                total = total,
                                estado = "Pendiente",
                                proyecto = proyectoSeleccionado
                            )

                            if (!esValido) {
                                mensajeErrorValidacion = mensajeError ?: "Error desconocido"
                                mostrarDialogoError = true
                            } else {
                                val nuevaFactura = FacturaEmitida(
                                    id = "",
                                    descripcion = descripcion,
                                    fechaEmision = fechaEmision,
                                    nombreReceptor = nombreReceptor,
                                    cifReceptor = cifReceptor,
                                    direccionReceptor = direccionReceptor,
                                    baseImponible = baseImponibleText.replace(".", "").replace(",", ".").toDoubleOrNull() ?: 0.00,
                                    tipoIva = tipoIvaValue,
                                    cuotaIva = cuotaIva.replace(".", "").replace(",", ".").toDoubleOrNull() ?: 0.00,
                                    total = total.replace(".", "").replace(",", ".").toDoubleOrNull() ?: 0.00,
                                    estado = "Pendiente",
                                    proyecto = proyectoSeleccionado
                                )

                                facturaViewModel.agregarFacturaEmitida(nuevaFactura, navHostController)
                                mostrarDialogoExito = true  // Mostramos el diálogo de éxito
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = AzulOscuro)
                    ) {
                        Text("Guardar")
                    }

                    // 🔹 Diálogo de éxito al guardar la factura
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

                /*
                LaunchedEffect(facturaGuardada) {
                    if (facturaGuardada) {
                        snackbarHostState.showSnackbar("Factura creada exitosamente")
                        facturaGuardada = false
                    }
                }
                 */

            }
        }
        Spacer(modifier = Modifier.height(64.dp))
    }
}


fun validarCamposFacturaAdd(
    //id: Int,
    numeroFactura: String,
    descripcion: String,
    fechaEmision: String,
    nombreReceptor: String,
    cifReceptor: String,
    direccionReceptor: String,
    baseImponibleText: String,
    tipoIva: String,
    cuotaIva: String,
    total: String,
    estado: String,
    proyecto: String



): Pair<Boolean, String?> {
    // Validación de campos obligatorios y longitud máxima
    if (descripcion.isBlank() ||
        fechaEmision.isBlank() || nombreReceptor.isBlank() ||
        cifReceptor.isBlank() || direccionReceptor.isBlank() ||
        baseImponibleText.isBlank() || tipoIva.isBlank() ||
        cuotaIva.isBlank() ||  total.isBlank() ||
        proyecto.isBlank()

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
