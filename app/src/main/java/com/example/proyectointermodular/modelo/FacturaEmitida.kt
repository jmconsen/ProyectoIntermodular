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
    val baseImponible: Double = 0.00,
    val tipoIva: Double = 0.00,
    val cuotaIva: Double = 0.00,
    val total: Double = 0.00,

    // Estado de la factura
    val estado: String = "Pendiente",

    //Proyecto asociado
    val proyecto: String = "",
    val proyectoId: String = ""

) {
    // Los datos del emisor siempre son los de la empresa
    val nombreEmisor: String = Empresa.NOMBRE
    val cifEmisor: String = Empresa.CIF
    val direccionEmisor: String = Empresa.DIRECCION
}
