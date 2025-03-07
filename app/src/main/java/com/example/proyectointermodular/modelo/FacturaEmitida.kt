package com.example.proyectointermodular.modelo

data class FacturaEmitida(

    var id: String = "",
    val numeroFactura: String = "",
    var descripcion: String = "",
    var fechaEmision: String = "",

    // Información del Emisor (Tu empresa)
    val nombreEmisor: String = "",
    val cifEmisor: String = "",
    val direccionEmisor: String = "",

    // Información del Receptor (El Cliente)
    val nombreReceptor: String = "",
    val cifReceptor: String = "",
    val direccionReceptor: String = "",

    // Datos financieros
    val baseImponible: Double = 0.0,
    val tipoIva: Double = 0.0,
    val cuotaIva: Double = 0.0,
    val total: Double = 0.0,

    // Estado de la factura
    val estado: String = "Pendiente" // Puede ser: Pendiente, Pagada, Cancelada

)
