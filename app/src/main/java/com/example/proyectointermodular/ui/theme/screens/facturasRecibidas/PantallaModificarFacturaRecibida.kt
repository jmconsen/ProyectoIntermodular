package com.example.proyectointermodular.ui.theme.screens.facturasRecibidas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.proyectointermodular.componentes.BotonEstandar
import com.example.proyectointermodular.modelo.FacturaRecibida
import com.example.proyectointermodular.ui.theme.AzulClaro
import com.example.proyectointermodular.ui.theme.FondoPantallas
import com.example.proyectointermodular.ui.theme.GrisOscuro2
import com.example.proyectointermodular.ui.theme.Negro

@Composable
fun PantallaModificarFacturaRecibida(
    id: String?,
    navHostController: NavHostController,
    facturaRecibidaViewModel: FacturaRecibidaViewModel = viewModel()
) {
    if (id == null) {
        LaunchedEffect(Unit) {
            navHostController.popBackStack()
        }
        return
    }

    val cargando = facturaRecibidaViewModel.cargando.collectAsState().value
    val facturaExistente = facturaRecibidaViewModel.obtenerFacturaPorId(id)

    if (cargando || facturaExistente == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
        return
    }

    //var id by remember { mutableStateOf(facturaExistente.id) }
    var numeroFactura by remember { mutableStateOf(facturaExistente.numeroFactura) }
    var descFactura by remember { mutableStateOf(facturaExistente.descFactura) }
    var fechaFactura by remember { mutableStateOf(facturaExistente.fechaFactura) }
    var nombreEmisor by remember { mutableStateOf(facturaExistente.nombreEmisor) }
    var cifEmisor by remember { mutableStateOf(facturaExistente.cifEmisor) }
    var direccionEmisor by remember { mutableStateOf(facturaExistente.direccionEmisor) }
    var nombreReceptor by remember { mutableStateOf(facturaExistente.nombreReceptor) }
    var cifReceptor by remember { mutableStateOf(facturaExistente.cifReceptor) }
    var direccionReceptor by remember { mutableStateOf(facturaExistente.direccionReceptor) }
    var baseImponible by remember { mutableStateOf(facturaExistente.baseImponible) }
    var tipoIva by remember { mutableStateOf(facturaExistente.tipoIva) }
    var cuotaIva by remember { mutableStateOf(facturaExistente.cuotaIva) }
    var total by remember { mutableStateOf(facturaExistente.total) }

    var mostrarDialogoExito by remember { mutableStateOf(false) }
    var mostrarDialogoError by remember { mutableStateOf(false) }
    var mensajeErrorValidacion by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = FondoPantallas
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Editar Factura",
                style = MaterialTheme.typography.headlineMedium,
                color = GrisOscuro2,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .padding(top = 20.dp)
            )

            OutlinedTextField(
                value = numeroFactura,
                onValueChange = { numeroFactura = it },
                label = { Text("Número Factura") },
                modifier = Modifier.fillMaxWidth(),
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


            OutlinedTextField(
                value = descFactura,
                onValueChange = { descFactura = it },
                label = { Text("Descripción Factura") },
                modifier = Modifier.fillMaxWidth(),
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

            OutlinedTextField(
                value = fechaFactura,
                onValueChange = { fechaFactura = it },
                label = { Text("Fecha Factura") },
                modifier = Modifier.fillMaxWidth(),
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

            OutlinedTextField(
                value = nombreEmisor,
                onValueChange = { nombreEmisor = it },
                label = { Text("Nombre Emisor") },
                modifier = Modifier.fillMaxWidth(),
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

            OutlinedTextField(
                value = direccionEmisor,
                onValueChange = { direccionEmisor = it },
                label = { Text("Dirección Emisor") },
                modifier = Modifier.fillMaxWidth(),
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

            OutlinedTextField(
                value = cifEmisor,
                onValueChange = { cifEmisor = it },
                label = { Text("CIF Emisor") },
                modifier = Modifier.fillMaxWidth(),
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

            OutlinedTextField(
                value = nombreReceptor,
                onValueChange = { nombreReceptor = it },
                label = { Text("Nombre Receptor") },
                modifier = Modifier.fillMaxWidth(),
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

            OutlinedTextField(
                value = direccionReceptor,
                onValueChange = { direccionReceptor = it },
                label = { Text("Dirección Receptor") },
                modifier = Modifier.fillMaxWidth(),
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

            OutlinedTextField(
                value = cifReceptor,
                onValueChange = { cifReceptor = it },
                label = { Text("CIF Receptor") },
                modifier = Modifier.fillMaxWidth(),
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

            OutlinedTextField(
                value = baseImponible,
                onValueChange = { baseImponible = it },
                label = { Text("Base Imponible") },
                modifier = Modifier.fillMaxWidth(),
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

            OutlinedTextField(
                value = tipoIva,
                onValueChange = { tipoIva = it },
                label = { Text("Tipo IVA") },
                modifier = Modifier.fillMaxWidth(),
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

            OutlinedTextField(
                value = cuotaIva,
                onValueChange = { cuotaIva = it },
                label = { Text("Cuota IVA") },
                modifier = Modifier.fillMaxWidth(),
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

            OutlinedTextField(
                value = total,
                onValueChange = { total = it },
                label = { Text("Total") },
                modifier = Modifier.fillMaxWidth(),
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

            BotonEstandar(
                texto = "Guardar Cambios",
                onClick = {
                    val (esValido, mensajeError) = validarCamposFacturaRecibidaMod(

                        numeroFactura,
                        descFactura ,
                        fechaFactura,
                        nombreEmisor,
                        cifEmisor,
                        direccionEmisor,
                        nombreReceptor,
                        cifReceptor,
                        direccionReceptor,
                        baseImponible,
                        tipoIva,
                        cuotaIva,
                        total
                        )
                    if (esValido) {
                        val facturaActualizado = FacturaRecibida(
                            id = id,
                            numeroFactura = numeroFactura,
                            descFactura = descFactura,
                            fechaFactura = fechaFactura,
                            nombreEmisor = nombreEmisor,
                            cifEmisor = cifEmisor,
                            direccionEmisor = direccionEmisor,
                            nombreReceptor = nombreReceptor,
                            cifReceptor = cifReceptor,
                            direccionReceptor = direccionReceptor,
                            baseImponible = baseImponible,
                            tipoIva = tipoIva,
                            cuotaIva = cuotaIva,
                            total = total

                        )
                        facturaRecibidaViewModel.actualizarFactura(id, facturaActualizado)
                        mostrarDialogoExito = true
                    } else {
                        mostrarDialogoError = true
                        mensajeErrorValidacion = mensajeError ?: "Error de validación"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )

            Spacer(modifier = Modifier.height(200.dp))
        }

        if (mostrarDialogoExito) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("Actualizada") },
                text = { Text("Factura actualizada correctamente.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            mostrarDialogoExito = false
                            navHostController.popBackStack()
                        }
                    ) {
                        Text("Aceptar")
                    }
                }
            )
        }

        if (mostrarDialogoError) {
            AlertDialog(
                onDismissRequest = { mostrarDialogoError = false },
                title = { Text("Error de Validación") },
                text = { Text(mensajeErrorValidacion) },
                confirmButton = {
                    TextButton(onClick = { mostrarDialogoError = false }) {
                        Text("Aceptar")
                    }
                }
            )
        }
    }
}

fun validarCamposFacturaRecibidaMod(

    numeroFactura: String,
    descFactura: String,
    fechaFactura: String,
    nombreEmisor: String,
    cifEmisor: String,
    direccionEmisor: String,
    nombreReceptor: String,
    cifReceptor: String,
    direccionReceptor: String,
    baseImponible: String,
    tipoIva: String,
    cuotaIva: String,
    total: String

): Pair<Boolean, String?> {
    // Validación de campos obligatorios y longitud máxima
    if (numeroFactura.isBlank() ||
        descFactura.isBlank() ||
        fechaFactura.isBlank() ||
        nombreEmisor.isBlank() ||
        cifEmisor.isBlank() ||
        direccionEmisor.isBlank() ||
        nombreReceptor.isBlank() ||
        cifReceptor.isBlank() ||
        direccionReceptor.isBlank() ||
        baseImponible.isBlank() ||
        tipoIva.isBlank() ||
        cuotaIva.isBlank() ||
        total.isBlank())
    {
        return Pair(false, "Todos los campos son obligatorios.")
    }

    if (numeroFactura.length > 50 ||
        descFactura.length > 50 ||
        fechaFactura.length > 50 ||
        nombreEmisor.length > 50 ||
        cifEmisor.length > 50 ||
        direccionEmisor.length > 50 ||
        nombreReceptor.length > 50 ||
        cifReceptor.length > 50 ||
        direccionReceptor.length > 50 ||
        baseImponible.length > 50 ||
        tipoIva.length > 50 ||
        cuotaIva.length > 50 ||
        total.length > 50)
    {
        return Pair(false, "Los campos no pueden exceder los 50 caracteres.")
    }

    if (cifEmisor.length > 9 || cifReceptor.length > 9)
    {
        return Pair(false, "El DNI no puede tener más de 9 caracteres.")
    }

    // Validación de CIF
    val cifRegex = "^[0-9]{8}[TRWAGMYFPDXBNJZSQVHLCKE]$".toRegex()
    if (!cifEmisor.matches(cifRegex) || !cifReceptor.matches(cifRegex))
    {
        return Pair(false, "El formato del CIF no es válido.")
    }

    // Validación de longitud y contenido de Descripción, Nombre y Dirección
    if (descFactura.length < 2 ||
        nombreEmisor.length < 2 ||
        direccionEmisor.length < 2 ||
        nombreReceptor.length < 2 ||
        direccionReceptor.length < 2)
    {

        return Pair(false, "Descripción, Nombre y Dirección deben tener al menos 2 caracteres.")
    }

    if (nombreEmisor.any { it.isDigit() } ||
        nombreReceptor.any { it.isDigit() }

    )
    {
        return Pair(false, "Nombre no debe contener números.")
    }

    // If all validations pass
    return Pair(true, null)

}