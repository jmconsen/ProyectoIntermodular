package com.example.proyectointermodular.ui.theme.screens.facturasRecibidas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyectointermodular.modelo.FacturaEmitida
import com.example.proyectointermodular.ui.theme.AzulOscuro
import com.example.proyectointermodular.ui.theme.FondoPantallas
import com.example.proyectointermodular.ui.theme.GrisOscuro2
import com.example.proyectointermodular.ui.theme.screens.facturasEmitidas.formatoNumero
import com.example.proyectointermodular.viewmodel.FacturaViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalleFacturaRecibida(
    id: String?,
    navHostController: NavHostController,
    facturaViewModel: FacturaViewModel = viewModel()
) {
    if (id == null) {
        LaunchedEffect(Unit) { navHostController.popBackStack() }
        return
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val factura = facturaViewModel.facturasRecibidas.value?.find { it.id == id }

    if (factura == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Factura no encontrada")
        }
        return
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },

        topBar = {
            TopAppBar(
                title = { Text("Detalle Factura Recibida") },
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
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {

                /*
                Text(
                    text = "Detalles de la Factura",
                    style = MaterialTheme.typography.headlineMedium,
                    color = AzulOscuro
                )

                 */

                Spacer(modifier = Modifier.height(16.dp))

                // Información de la factura
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Número Factura: ${factura.numeroFactura}", style = MaterialTheme.typography.bodyLarge, color = GrisOscuro2)
                    Text(text = "Fecha Recepción: ${factura.fechaRecepcion}", style = MaterialTheme.typography.bodyLarge, color = GrisOscuro2)
                    Text(text = "Método Pago: ${factura.metodoPago ?: "N/A"}", style = MaterialTheme.typography.bodyLarge, color = GrisOscuro2)
                    Text(text = "Fecha Pago: ${factura.fechaPago ?: "No pagada"}", style = MaterialTheme.typography.bodyLarge, color = GrisOscuro2)
                }


                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = GrisOscuro2, thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))

                // Datos del cliente
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Nombre Emisor: ${factura.nombreEmisor}", style = MaterialTheme.typography.bodyLarge, color = GrisOscuro2)
                    Text(text = "CIF Emisor: ${factura.cifEmisor}", style = MaterialTheme.typography.bodyLarge, color = GrisOscuro2)
                    Text(text = "Dirección Emisor: ${factura.direccionEmisor}", style = MaterialTheme.typography.bodyLarge, color = GrisOscuro2)
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = GrisOscuro2, thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))

                // Descripción
                Text(text = "Descripción: ${factura.descripcion}", style = MaterialTheme.typography.bodyLarge, color = GrisOscuro2)

                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = GrisOscuro2, thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))

                // Información financiera
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "Base Imponible: ${formatoNumero.format(factura.baseImponible)}€", style = MaterialTheme.typography.bodyLarge, color = GrisOscuro2)
                    Text(text = "Tipo IVA: ${factura.tipoIva}%", style = MaterialTheme.typography.bodyLarge, color = GrisOscuro2)
                    Text(text = "Cuota IVA: ${formatoNumero.format(factura.cuotaIva)}€", style = MaterialTheme.typography.bodyLarge, color = GrisOscuro2)
                    Text(text = "Total: ${formatoNumero.format(factura.total)}€", style = MaterialTheme.typography.bodyLarge, color = GrisOscuro2)
   }

                Spacer(modifier = Modifier.height(32.dp))

                // Botón de volver
                Button(
                    onClick = { navHostController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulOscuro
                    )
                ) {
                    Text("Volver", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}