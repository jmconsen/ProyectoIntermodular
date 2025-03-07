package com.example.proyectointermodular.ui.theme.screens.facturasRecibidas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyectointermodular.modelo.FacturaRecibida
import com.example.proyectointermodular.ui.theme.FondoPantallas
import com.example.proyectointermodular.ui.theme.Rojizo
import com.example.proyectointermodular.viewmodel.FacturaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFacturasRecibidas(
    navHostController: NavHostController,
    facturaViewModel: FacturaViewModel = viewModel()
) {
    val facturas by facturaViewModel.facturasRecibidas.observeAsState(initial = emptyList())
    var facturaRecibidaAEliminar by remember { mutableStateOf<Pair<String, FacturaRecibida>?>(null) }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    facturaRecibidaAEliminar?.let { (id, factura) ->
        AlertDialog(
            onDismissRequest = { facturaRecibidaAEliminar = null },
            title = { Text("Confirmación") },
            text = { Text("¿Eliminar la factura '${factura.numeroFactura}'?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        facturaViewModel.eliminarFacturaRecibida(id)
                        scope.launch { snackbarHostState.showSnackbar("Factura eliminada correctamente") }
                        facturaRecibidaAEliminar = null
                    }
                ) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { facturaRecibidaAEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Facturas Recibidas") },
                actions = {
                    IconButton(onClick = { navHostController.navigate("PantallaAddFacturaRecibida") }) {
                        Icon(Icons.Filled.Add, contentDescription = "Añadir Factura")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Brush.verticalGradient(colors = FondoPantallas))
                .padding(bottom = 80.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(top = 16.dp)) {
                // Campo de búsqueda
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Buscar por número de factura") },
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                )
                val filteredFacturas = facturas.filter {
                    it.numeroFactura.toString().contains(searchQuery.text, ignoreCase = true)
                }
                if (filteredFacturas.isNotEmpty()) {
                    LazyColumn(modifier = Modifier.weight(1f).padding(16.dp)) {
                        items(filteredFacturas) { factura ->
                            FacturaRecibidaItem(
                                facturaRecibida = factura,
                                onView = { navHostController.navigate("PantallaDetalleFacturaRecibida/${factura.id}") },
                                onEdit = { navHostController.navigate("PantallaModificarFacturaRecibida/${factura.id}") },
                                onDelete = { facturaRecibidaAEliminar = factura.id to factura }
                            )
                        }
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No hay facturas recibidas")
                    }
                }
            }
        }
    }
}

@Composable
fun FacturaRecibidaItem(
    facturaRecibida: FacturaRecibida,
    onView: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Factura Nº: ${facturaRecibida.numeroFactura}")
                Text(text = "Descripción: ${facturaRecibida.descripcion ?: "No especificado"}")
                Text(text = "Fecha de Recepción: ${facturaRecibida.fechaRecepcion ?: "No especificado"}")
            }
            IconButton(onClick = { onView() }) {
                Icon(Icons.Filled.Visibility, contentDescription = "Ver detalles")
            }
            IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription = "Editar") }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Rojizo)
            }
        }
    }
}