package com.example.proyectointermodular.modelo

data class FacturaRecibida(

    var id: String = "",
    val numeroFactura: String = "",
    var descFactura: String = "",
    var fechaFactura: String = "",
    val nombreEmisor: String = "",
    val cifEmisor: String = "",
    val direccionEmisor: String = "",
    val nombreReceptor: String = "",
    val cifReceptor: String = "",
    val direccionReceptor: String = "",
    val baseImponible: String = "",
    val tipoIva: String = "",
    val cuotaIva: String = "",
    val total: String = "",

)
