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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyectointermodular.componentes.BotonEstandar
import com.example.proyectointermodular.modelo.FacturaEmitida
import com.example.proyectointermodular.ui.theme.AzulClaro
import com.example.proyectointermodular.ui.theme.FondoPantallas
import com.example.proyectointermodular.ui.theme.GrisOscuro2
import com.example.proyectointermodular.ui.theme.Negro
import com.example.proyectointermodular.ui.theme.viewmodel.FacturaViewModel
import androidx.compose.material3.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAddFactura(
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
    var tipoIva by remember { mutableStateOf("21%") }  // Valor por defecto
    //var cuotaIva by remember { mutableStateOf("0.0") }
    //var total by remember { mutableStateOf("0.0") }
    var expanded by remember { mutableStateOf(false) }
    var mostrarDialogoExito by remember { mutableStateOf(false) }
    var mostrarDialogoError by remember { mutableStateOf(false) }
    var mensajeErrorValidacion by remember { mutableStateOf("") }

    val tiposIva = listOf("21%", "10%", "4%", "Exento o 0%")

    // Calcular cuota IVA de forma reactiva
    val cuotaIva by remember(baseImponible, tipoIva) {
        derivedStateOf {
            val base = baseImponible.toDoubleOrNull() ?: 0.0
            val iva = when (tipoIva) {
                "21%" -> 0.21
                "10%" -> 0.10
                "4%" -> 0.04
                else -> 0.0
            }
            (base * iva).toString()
        }
    }

    // Calcular total de forma reactiva
    val total by remember(baseImponible, cuotaIva) {
        derivedStateOf {
            val base = baseImponible.toDoubleOrNull() ?: 0.0
            val cuota = cuotaIva.toDoubleOrNull() ?: 0.0
            (base + cuota).toString()
        }
    }

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

            /*
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
            */

            OutlinedTextField(
                value = baseImponible,
                onValueChange = { baseImponible = it },
                label = { Text("Base Imponible") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                        .fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AzulClaro,
                        unfocusedBorderColor = Negro
                    )
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

            // Campos calculados automáticamente
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


            BotonEstandar(
                texto = "Guardar Factura",
                onClick = {
                    val (esValido, mensaje) = validarCamposFacturaAdd(
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
                        val nuevaFacturaEmitida = FacturaEmitida(
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

                        facturaViewModel.agregarFactura(nuevaFacturaEmitida)
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

fun validarCamposFacturaAdd(
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
    val cifRegex = "^[ABCDEFGHJNPQRSUVW][0-9]{7}[0-9A-J]$".toRegex()

    if (!cifEmisor.matches(cifRegex) || !cifReceptor.matches(cifRegex)) {
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
        nombreReceptor.any { it.isDigit() }
        ) {
        return Pair(false, "Descripción y Nombre no deben contener números.")
    }

    return Pair(true, null)
}

