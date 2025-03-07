package com.example.proyectointermodular.ui.theme.screens.facturas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyectointermodular.modelo.FacturaEmitida
import com.example.proyectointermodular.ui.theme.AzulClaro
import com.example.proyectointermodular.ui.theme.FondoPantallas
import com.example.proyectointermodular.ui.theme.Negro
import com.example.proyectointermodular.ui.theme.Rojizo
import com.example.proyectointermodular.ui.theme.viewmodel.FacturaViewModel
import kotlinx.coroutines.delay

@Composable
fun PantallaFormularioFacturas(
    navHostController: NavHostController,
    facturaViewModel: FacturaViewModel = viewModel()
) {
    val facturas by facturaViewModel.facturas.collectAsState()
    var mensajeBorrado by remember { mutableStateOf("") }
    var facturaEmitidaAEliminar by remember { mutableStateOf<Pair<String, FacturaEmitida>?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    // Diálogo de confirmación para eliminar factura
    facturaEmitidaAEliminar?.let { (id, factura) ->
        AlertDialog(
            onDismissRequest = { facturaEmitidaAEliminar = null },
            title = { Text("Confirmación") },
            text = { Text("¿Estás seguro de que deseas eliminar la factura número '${factura.numeroFactura}'?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        facturaViewModel.eliminarFactura(id)
                        mensajeBorrado = "Factura eliminada correctamente"
                        facturaEmitidaAEliminar = null
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { facturaEmitidaAEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = FondoPantallas
                )
            )
            .padding(bottom = 80.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(top = 16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Campo de búsqueda
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Buscar factura") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    textStyle = TextStyle(color = Negro),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Negro,
                        unfocusedTextColor = Negro,
                        disabledTextColor = Negro,
                        focusedLabelColor = Negro,
                        unfocusedLabelColor = Negro,
                        cursorColor = Negro,
                        focusedBorderColor = AzulClaro,
                        unfocusedBorderColor = Negro
                    )
                )
                // Botón para agregar nueva factura
                FloatingActionButton(
                    onClick = { navHostController.navigate("PantallaAddFactura") },
                    containerColor = AzulClaro,
                    modifier = Modifier.size(56.dp) // Tamaño estándar de un FAB
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Añadir Factura",
                        tint = Negro
                    )
                }
            }
            // Filtra las facturas según el texto de búsqueda
            val filteredFacturas = facturas.filter {
                it.second.numeroFactura.contains(searchQuery, ignoreCase = true)
                        //|| it.second.nombreReceptor.contains(searchQuery, ignoreCase = true)
            }.sortedBy { it.second.numeroFactura.lowercase() }

            // Lista de facturas
            if (filteredFacturas.isEmpty()) {
                Text(
                    text = "No se encontraron facturas.",
                    color = Negro,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    items(filteredFacturas) { (id, factura) ->
                        FacturaItem(
                            facturaEmitida = factura,
                            onEdit = {
                                navHostController.navigate("PantallaModificarFactura/$id")
                            },
                            onDelete = {
                                facturaEmitidaAEliminar = id to factura
                            }
                        )
                    }
                }
            }

            // Muestra mensaje de eliminación si existe
            if (mensajeBorrado.isNotEmpty()) {
                Text(
                    text = mensajeBorrado,
                    color = Color.Red,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                // Quita el mensaje después de 4 segundos
                LaunchedEffect(mensajeBorrado) {
                    delay(4000)
                    mensajeBorrado = ""
                }
            }
        }
    }
}

@Composable
fun FacturaItem(
    facturaEmitida: FacturaEmitida,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${facturaEmitida.numeroFactura}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Descripción Factura: ${facturaEmitida.descFactura ?: "No especificado"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Fecha Factura: ${facturaEmitida.fechaFactura ?: "No especificado"}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Editar Factura")
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar Factura",
                    tint = Rojizo
                )
            }
        }
    }
}