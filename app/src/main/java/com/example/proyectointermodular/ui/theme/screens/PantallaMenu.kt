package com.example.proyectointermodular.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.proyectointermodular.componentes.BotonEstandar
import com.example.proyectointermodular.ui.theme.FondoPantallas
import com.example.proyectointermodular.ui.theme.GrisOscuro2

@Composable
fun PantallaMenu(navHostController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = FondoPantallas // Mantiene la estética del login
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Menú Principal",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = GrisOscuro2,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            BotonEstandar(
                texto = "Facturas Emitidas",
                onClick = { navHostController.navigate("PantallaFacturasEmitidas") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            BotonEstandar(
                texto = "Facturas Recibidas",
                onClick = { navHostController.navigate("PantallaFacturasRecibidas") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}