package com.example.proyectointermodular.modelo

data class FacturaRecibida(
    var id: String = "",
    val numeroFactura: String = "",
    var descripcion: String = "",
    var fechaRecepcion: String = "",

    // Información del Proveedor (Emisor)
    val nombreEmisor: String = "",
    val cifEmisor: String = "",
    val direccionEmisor: String = "",

    // Datos financieros
    val baseImponible: Double = 0.0,
    val tipoIva: Double = 0.0,
    val cuotaIva: Double = 0.0,
    val total: Double = 0.0,

    // Estado de la factura
    val estado: String = "Pendiente",
    val fechaPago: String? = null,
    val metodoPago: String? = null
) {
    // Los datos del receptor siempre son los de la empresa
    val nombreReceptor: String = Empresa.NOMBRE
    val cifReceptor: String = Empresa.CIF
    val direccionReceptor: String = Empresa.DIRECCION
}