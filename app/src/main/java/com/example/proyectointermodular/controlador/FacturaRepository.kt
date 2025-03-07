package com.example.proyectointermodular.controlador

import com.example.proyectointermodular.modelo.FacturaEmitida
import com.example.proyectointermodular.modelo.FacturaRecibida
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FacturaRepository {

    private val db = FirebaseFirestore.getInstance()

    // Colecciones en Firestore
    private val facturasEmitidasRef = db.collection("facturasEmitidas")
    private val facturasRecibidasRef = db.collection("facturasRecibidas")

    // Métodos para Facturas Emitidas
    suspend fun agregarFacturaEmitida(factura: FacturaEmitida) {
        facturasEmitidasRef.document(factura.id).set(factura).await()
    }

    suspend fun obtenerFacturasEmitidas(): List<FacturaEmitida> {
        return try {
            val snapshot = facturasEmitidasRef.get().await()
            snapshot.toObjects(FacturaEmitida::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun actualizarFacturaEmitida(factura: FacturaEmitida) {
        facturasEmitidasRef.document(factura.id).set(factura).await()
    }

    suspend fun eliminarFacturaEmitida(id: String) {
        facturasEmitidasRef.document(id).delete().await()
    }

    // Métodos para Facturas Recibidas
    suspend fun agregarFacturaRecibida(factura: FacturaRecibida) {
        facturasRecibidasRef.document(factura.id).set(factura).await()
    }

    suspend fun obtenerFacturasRecibidas(): List<FacturaRecibida> {
        return try {
            val snapshot = facturasRecibidasRef.get().await()
            snapshot.toObjects(FacturaRecibida::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun actualizarFacturaRecibida(factura: FacturaRecibida) {
        facturasRecibidasRef.document(factura.id).set(factura).await()
    }

    suspend fun eliminarFacturaRecibida(id: String) {
        facturasRecibidasRef.document(id).delete().await()
    }
}