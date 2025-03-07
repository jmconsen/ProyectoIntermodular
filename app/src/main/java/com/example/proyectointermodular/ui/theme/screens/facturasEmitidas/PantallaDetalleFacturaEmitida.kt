package com.example.proyectointermodular.ui.theme.screens.facturasEmitidas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyectointermodular.modelo.FacturaEmitida
import com.example.proyectointermodular.ui.theme.FondoPantallas
import com.example.proyectointermodular.viewmodel.FacturaViewModel

@Composable
fun PantallaDetalleFacturaEmitida(
    id: String?,
    navHostController: NavHostController,
    facturaViewModel: FacturaViewModel = viewModel()
) {
    if (id == null) {
        LaunchedEffect(Unit) { navHostController.popBackStack() }
        return
    }

    val factura = facturaViewModel.facturasEmitidas.value?.find { it.id == id }

    if (factura == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Factura no encontrada")
        }
        return
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(Brush.verticalGradient(colors = FondoPantallas))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Detalles de la Factura", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Número: ${factura.numeroFactura}")
            Text(text = "Descripción: ${factura.descripcion}")
            Text(text = "Fecha de emisión: ${factura.fechaEmision}")
            Text(text = "Cliente: ${factura.nombreReceptor}")
            Text(text = "CIF Cliente: ${factura.cifReceptor}")
            Text(text = "Total: ${factura.total}€")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navHostController.popBackStack() }) {
                Text("Volver")
            }
        }
    }
}