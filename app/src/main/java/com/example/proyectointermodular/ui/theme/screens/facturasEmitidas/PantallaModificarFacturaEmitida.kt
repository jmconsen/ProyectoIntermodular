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
    var baseImponible by remember { mutableStateOf(facturaExistente.baseImponible.toString()) }
    var tipoIva by remember { mutableStateOf(facturaExistente.tipoIva.toString()) }
    var expanded by remember { mutableStateOf(false) }
    var estado by remember { mutableStateOf(facturaExistente.estado) }

    var proyectos by remember { mutableStateOf(listOf<String>()) }
    var proyectoSeleccionado by remember { mutableStateOf(facturaExistente.proyecto) }
    var expandedProyectos by remember { mutableStateOf(false) }

    // Calculado automáticamente
    //val cuotaIva = (baseImponible.toDoubleOrNull() ?: 0.00) * (tipoIva.toDoubleOrNull() ?: 0.00) / 100
    //val total = (baseImponible.toDoubleOrNull() ?: 0.00) + cuotaIva

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
            //"%.2f".format(base * iva)
        }
    }

    // Calcular total de forma reactiva
    val total by remember(baseImponible, cuotaIva) {
        derivedStateOf {
            val base = baseImponible.toDoubleOrNull() ?: 0.00
            val cuota = cuotaIva.toDoubleOrNull() ?: 0.00
            (base + cuota).toString()
            //"%.2f".format(base + cuota)
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
                            val facturaActualizada = facturaExistente.copy(
                                descripcion = descripcion,
                                fechaEmision = fechaEmision,
                                nombreReceptor = nombreReceptor,
                                cifReceptor = cifReceptor,
                                direccionReceptor = direccionReceptor,
                                baseImponible = baseImponible.toDoubleOrNull() ?: 0.00,
                                tipoIva = tipoIvaValue,
                                cuotaIva = cuotaIva.toDoubleOrNull() ?: 0.00,
                                total = total.toDoubleOrNull() ?: 0.00,
                                estado = estado,
                                proyecto = proyectoSeleccionado
                            )
                            facturaViewModel.actualizarFacturaEmitida(facturaActualizada)
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

                Spacer(modifier = Modifier.height(16.dp))

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