package com.example.proyectointermodular.ui.theme.screens.facturas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyectointermodular.componentes.BotonEstandar
import com.example.proyectointermodular.modelo.Factura
import com.example.proyectointermodular.ui.theme.AzulClaro
import com.example.proyectointermodular.ui.theme.FondoPantallas
import com.example.proyectointermodular.ui.theme.GrisOscuro2
import com.example.proyectointermodular.ui.theme.Negro
import com.example.proyectointermodular.ui.theme.viewmodel.FacturaViewModel

@Composable
fun PantallaAddFacturaCopy(
    navHostController: NavHostController,
    facturaViewModel: FacturaViewModel = viewModel()
) {
    var numeroFactura by remember { mutableStateOf("") }
    var descFactura by remember { mutableStateOf("") }
    var fechaFactura by remember { mutableStateOf("") }
    var nombreEmisor by remember { mutableStateOf("") }
    var cifEmisor by remember { mutableStateOf("") }
    var direccionEmisor by remember { mutableStateOf("") }
    var nombreReceptor by remember { mutableStateOf("") }
    var cifReceptor by remember { mutableStateOf("") }
    var direccionReceptor by remember { mutableStateOf("") }
    var baseImponible by remember { mutableStateOf("") }
    var tipoIva by remember { mutableStateOf("") }
    var cuotaIva by remember { mutableStateOf("") }
    var total by remember { mutableStateOf("") }
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
                .padding(top = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Añadir Nueva Factura",
                style = MaterialTheme.typography.headlineMedium,
                color = GrisOscuro2,
                modifier = Modifier.padding(bottom = 16.dp)
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
                textStyle = TextStyle(color = Color.White),
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
                label = { Text(" CIF Emisor") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
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
                texto = "Guardar Factura",
                onClick = {
                    val (esValido, mensaje) = validarCamposFacturaAddCopy(
                        //id,
                        numeroFactura,
                        descFactura,
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
                        val nuevaFactura = Factura(
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

                        facturaViewModel.agregarFactura(nuevaFactura)
                        mostrarDialogoExito = true
                    } else {
                        mensajeErrorValidacion = mensaje ?: "Error de validación"
                        mostrarDialogoError = true
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
                title = { Text("Alta") },
                text = { Text("Factura creada correctamente.") },
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

fun validarCamposFacturaAddCopy(
    //id: Int,
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
    if (numeroFactura.isBlank() || descFactura.isBlank() ||
        fechaFactura.isBlank() || nombreEmisor.isBlank() ||
        cifEmisor.isBlank() || direccionEmisor.isBlank() ||
        nombreReceptor.isBlank() || cifReceptor.isBlank() ||
        direccionReceptor.isBlank() || baseImponible.isBlank() ||
        tipoIva.isBlank() || cuotaIva.isBlank() ||
        total.isBlank()

        ) {
        return Pair(false, "Todos los campos son obligatorios.")
    }
    if (cifEmisor.length > 9 || cifReceptor.length > 9) {

        return Pair(false, "El CIF no puede tener más de 9 caracteres.")
    }
    if (descFactura.length > 50 ||
        nombreEmisor.length > 50 ||
        direccionEmisor.length > 50 ||
        nombreReceptor.length > 50 ||
        direccionReceptor.length > 50) {

        return Pair(false, "Descripción, Nombre y Dirección no pueden exceder los 50 caracteres.")
    }

    // Validación de CIF
    val dniRegex = "^[0-9]{8}[TRWAGMYFPDXBNJZSQVHLCKE]$".toRegex()
    if (!cifEmisor.matches(dniRegex) || !cifReceptor.matches(dniRegex)) {
        return Pair(false, "El formato del CIF no es válido.")
    }

    // Validación de longitud y contenido de Descripción, Nombre y Dirección
    if (descFactura.length < 2 ||
        nombreEmisor.length < 2 ||
        direccionEmisor.length < 2 ||
        nombreReceptor.length < 2 ||
        direccionReceptor.length < 2) {

        return Pair(false, "Descripción, Nombre y Dirección deben tener al menos 2 caracteres.")
    }
    if (descFactura.any { it.isDigit() } ||
        nombreEmisor.any { it.isDigit() } ||
        direccionEmisor.any { it.isDigit() } ||
        nombreReceptor.any { it.isDigit() } ||
        direccionReceptor.any { it.isDigit() }
        ) {
        return Pair(false, "Descripción, Nombre y Dirección no deben contener números.")
    }

    return Pair(true, null)
}

