package com.example.proyectointermodular.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.proyectointermodular.modelo.FacturaEmitida
import com.example.proyectointermodular.modelo.FacturaRecibida
import com.example.proyectointermodular.controlador.FacturaRepository
import kotlinx.coroutines.launch

class FacturaViewModel : ViewModel() {

    private val repository = FacturaRepository()

    // LiveData para Facturas Emitidas
    private val _facturasEmitidas = MutableLiveData<List<FacturaEmitida>>()
    val facturasEmitidas: LiveData<List<FacturaEmitida>> get() = _facturasEmitidas

    // LiveData para Facturas Recibidas
    private val _facturasRecibidas = MutableLiveData<List<FacturaRecibida>>()
    val facturasRecibidas: LiveData<List<FacturaRecibida>> get() = _facturasRecibidas

    init {
        cargarFacturas()
    }

    private fun cargarFacturas() {
        viewModelScope.launch {
            _facturasEmitidas.value = repository.obtenerFacturasEmitidas()
            _facturasRecibidas.value = repository.obtenerFacturasRecibidas()
        }
    }

    // Métodos para Facturas Emitidas
    fun agregarFacturaEmitida(factura: FacturaEmitida) {
        viewModelScope.launch {
            repository.agregarFacturaEmitida(factura)
            _facturasEmitidas.value = repository.obtenerFacturasEmitidas()
        }
    }

    fun actualizarFacturaEmitida(factura: FacturaEmitida) {
        viewModelScope.launch {
            repository.actualizarFacturaEmitida(factura)
            _facturasEmitidas.value = repository.obtenerFacturasEmitidas()
        }
    }

    fun eliminarFacturaEmitida(id: String) {
        viewModelScope.launch {
            repository.eliminarFacturaEmitida(id)
            _facturasEmitidas.value = repository.obtenerFacturasEmitidas()
        }
    }

    // Métodos para Facturas Recibidas
    fun agregarFacturaRecibida(factura: FacturaRecibida) {
        viewModelScope.launch {
            repository.agregarFacturaRecibida(factura)
            _facturasRecibidas.value = repository.obtenerFacturasRecibidas()
        }
    }

    fun actualizarFacturaRecibida(factura: FacturaRecibida) {
        viewModelScope.launch {
            repository.actualizarFacturaRecibida(factura)
            _facturasRecibidas.value = repository.obtenerFacturasRecibidas()
        }
    }

    fun eliminarFacturaRecibida(id: String) {
        viewModelScope.launch {
            repository.eliminarFacturaRecibida(id)
            _facturasRecibidas.value = repository.obtenerFacturasRecibidas()
        }
    }
}