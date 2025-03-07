package com.example.proyectointermodular.ui.theme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectointermodular.controlador.FacturaRepository
import com.example.proyectointermodular.modelo.FacturaEmitida
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FacturaViewModel : ViewModel() {

    private val facturaRepository = FacturaRepository()

    // Flujo para mantener la lista de facturas
    private val _facturas = MutableStateFlow<List<Pair<String, FacturaEmitida>>>(emptyList())
    val facturas: StateFlow<List<Pair<String, FacturaEmitida>>> get() = _facturas

    private val _cargando = MutableStateFlow(true)
    val cargando: StateFlow<Boolean> get() = _cargando

    init {
        cargarFacturasEnTiempoReal()
    }

    private fun cargarFacturasEnTiempoReal() {
        facturaRepository.obtenerFacturas { facturasObtenidos ->
            _facturas.value = facturasObtenidos
            _cargando.value = false  // Se cambia el estado de carga cuando se completan los datos
        }
    }

    // Elimina una factura por su ID
    fun eliminarFactura(id: String) {
        viewModelScope.launch {
            try {
                // Verifica si la factura existe antes de intentar eliminarla
                val facturaExistente = _facturas.value.any { it.first == id }
                if (!facturaExistente) {
                    println("Error: La factura con ID $id no existe.")
                    return@launch
                }

                facturaRepository.eliminarFactura(id)
                // Actualiza la lista local después de eliminar
                _facturas.value = _facturas.value.filter { it.first != id }
            } catch (e: Exception) {
                println("Error al eliminar factura: ${e.message}")
            }
        }
    }

    fun agregarFactura(facturaEmitida: FacturaEmitida) {
        viewModelScope.launch {
            try {
                val nuevoId = facturaRepository.agregarFactura(facturaEmitida)
                // Actualiza la factura con el nuevo ID y la añade a la lista local
                val facturaConId = facturaEmitida.copy(id = nuevoId)
                _facturas.value = _facturas.value + (nuevoId to facturaConId)
            } catch (e: Exception) {
                println("Error al agregar factura: ${e.message}")
            }
        }
    }

    fun actualizarFactura(id: String, facturaEmitidaActualizado: FacturaEmitida) {
        viewModelScope.launch {
            try {
                // Verifica si la factura existe antes de intentar actualizarla
                val facturaExistente = _facturas.value.find { it.first == id }
                if (facturaExistente == null) {
                    println("Error: No se encontró factura con ID $id. Facturas disponibles:")
                    _facturas.value.forEach { println("Factura ID: ${it.first}, Factura: ${it.second}") }
                    return@launch
                }

                // Actualiza la factura en el repositorio
                facturaRepository.actualizarFactura(id, facturaEmitidaActualizado)
                println("Factura con ID $id actualizado en el repositorio.")

                // Actualiza la lista local con la factura actualizada
                _facturas.value = _facturas.value.map {
                    if (it.first == id) id to facturaEmitidaActualizado else it
                }
                println("Lista local de facturas actualizada: $_facturas")

            } catch (e: Exception) {
                println("Error al actualizar factura: ${e.message}")
            }
        }
    }

    fun obtenerFacturaPorId(id: String): FacturaEmitida? {
        println("Buscando factura con ID: $id")
        println("Facturas disponibles:")
        _facturas.value.forEach { println("ID: ${it.first}, Factura: ${it.second}") }

        return _facturas.value.find { it.first == id }?.second
    }
    
}