package com.example.proyectointermodular.ui.theme.screens.facturasEmitidas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import com.example.proyectointermodular.ui.theme.AzulOscuro
import com.example.proyectointermodular.ui.theme.FondoPantallas
import com.google.firebase.firestore.FirebaseFirestore

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
    var expandedProyectos by remember { mutableStateOf(false) }

    val tiposIva = listOf("21%", "10%", "4%", "Exento o 0%")
    /*
    val tipoIvaValue = when (tipoIva) {
        "21%" -> 0.21
        "10%" -> 0.10
        "4%" -> 0.04
        else -> 0.0
    }

     */

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

    // Calcular cuota IVA de forma reactiva
    val cuotaIva by remember(baseImponibleText, tipoIva) {
        derivedStateOf {
            val base = baseImponibleText.toDoubleOrNull() ?: 0.00
            val iva = when (tipoIva) {
                "21%" -> 0.21
                "10%" -> 0.10
                "4%" -> 0.04
                else -> 0.00
            }
            (base * iva).toString()
        }
    }

    // Calcular total de forma reactiva
    val total by remember(baseImponibleText, cuotaIva) {
        derivedStateOf {
            val base = baseImponibleText.toDoubleOrNull() ?: 0.00
            val cuota = cuotaIva.toDoubleOrNull() ?: 0.00
            (base + cuota).toString()
        }
    }

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
                            if (descripcion.isNotBlank() && fechaEmision.isNotBlank() &&
                                nombreReceptor.isNotBlank() && cifReceptor.isNotBlank() && direccionReceptor.isNotBlank() &&
                                baseImponibleText.isNotBlank() && tipoIva.isNotBlank()
                            ) {
                                val factura = FacturaEmitida(
                                    id = "",
                                    descripcion = descripcion,
                                    fechaEmision = fechaEmision,
                                    nombreReceptor = nombreReceptor,
                                    cifReceptor = cifReceptor,
                                    direccionReceptor = direccionReceptor,
                                    baseImponible = baseImponibleText.toDouble(),
                                    //tipoIva = tipoIvaValue,
                                    tipoIva = tipoIva.toDouble(),
                                    cuotaIva = cuotaIva.toDouble(),
                                    total = total.toDouble(),
                                    estado = "Pendiente",
                                    proyecto = proyectoSeleccionado
                                )
                                facturaViewModel.agregarFacturaEmitida(factura, navHostController)
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
