package com.example.proyectointermodular.modelo

data class FacturaEmitida(

    var id: String = "",
    val numeroFactura: Int = 0,
    var descripcion: String = "",
    var fechaEmision: String = "",

    // Información del Cliente (Receptor)
    val nombreReceptor: String = "",
    val cifReceptor: String = "",
    val direccionReceptor: String = "",

    // Datos financieros
    val baseImponible: Double = 0.0,
    val tipoIva: Double = 0.0,
    val cuotaIva: Double = 0.0,
    val total: Double = 0.0,

    // Estado de la factura
    val estado: String = "Pendiente"
) {
    // Los datos del emisor siempre son los de la empresa
    val nombreEmisor: String = Empresa.NOMBRE
    val cifEmisor: String = Empresa.CIF
    val direccionEmisor: String = Empresa.DIRECCION
}
