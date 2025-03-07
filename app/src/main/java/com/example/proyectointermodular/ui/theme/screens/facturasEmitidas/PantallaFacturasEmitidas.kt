package com.example.proyectointermodular.ui.theme.screens.facturasEmitidas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyectointermodular.modelo.FacturaEmitida
import com.example.proyectointermodular.ui.theme.FondoPantallas
import com.example.proyectointermodular.ui.theme.Rojizo
import com.example.proyectointermodular.viewmodel.FacturaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFacturasEmitidas(
    navHostController: NavHostController,
    facturaViewModel: FacturaViewModel = viewModel()
) {
    val facturas by facturaViewModel.facturasEmitidas.observeAsState(initial = emptyList())
    var facturaEmitidaAEliminar by remember { mutableStateOf<Pair<String, FacturaEmitida>?>(null) }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    facturaEmitidaAEliminar?.let { (id, factura) ->
        AlertDialog(
            onDismissRequest = { facturaEmitidaAEliminar = null },
            title = { Text("Confirmación") },
            text = { Text("¿Eliminar la factura '${factura.numeroFactura}'?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        facturaViewModel.eliminarFacturaEmitida(id)
                        scope.launch { snackbarHostState.showSnackbar("Factura eliminada correctamente") }
                        facturaEmitidaAEliminar = null
                    }
                ) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { facturaEmitidaAEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Facturas Emitidas") },
                actions = {
                    IconButton(onClick = { navHostController.navigate("PantallaAddFacturaEmitida") }) {
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
                // Search field
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
                            FacturaItem(
                                facturaEmitida = factura,
                                onView = { navHostController.navigate("PantallaDetalleFacturaEmitida/${factura.id}") },
                                onEdit = { navHostController.navigate("PantallaModificarFacturaEmitida/${factura.id}") },
                                onDelete = { facturaEmitidaAEliminar = factura.id to factura }
                            )
                        }
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No hay facturas emitidas")
                    }
                }
            }
        }
    }
}

@Composable
fun FacturaItem(
    facturaEmitida: FacturaEmitida,
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
                Text(text = "Factura Nº: ${facturaEmitida.numeroFactura}")
                Text(text = "Descripción: ${facturaEmitida.descripcion ?: "No especificado"}")
                Text(text = "Fecha: ${facturaEmitida.fechaEmision ?: "No especificado"}")
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