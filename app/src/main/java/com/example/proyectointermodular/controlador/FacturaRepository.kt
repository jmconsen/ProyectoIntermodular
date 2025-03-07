package com.example.proyectointermodular.controlador

import com.example.proyectointermodular.modelo.FacturaEmitida
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FacturaRepository {

    private val db = FirebaseFirestore.getInstance()

    fun obtenerFacturas(onComplete: (List<Pair<String, FacturaEmitida>>) -> Unit) {
        db.collection("facturas").addSnapshotListener { snapshot, e ->
            if (e != null) {
                println("Error al obtener los facturas: ${e.message}")
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val facturas = snapshot.documents.mapNotNull { document ->
                    val facturaEmitida = document.toObject(FacturaEmitida::class.java)
                    facturaEmitida?.let { document.id to it }
                }
                onComplete(facturas)
            }
        }
    }

    suspend fun eliminarFactura(idFactura: String) {
        try {
            db.collection("facturas").document(idFactura).delete().await()
            println("Factura eliminado con éxito")
        } catch (e: Exception) {
            println("Error al eliminar factura: ${e.message}")
        }
    }

    suspend fun agregarFactura(facturaEmitida: FacturaEmitida): String {
        return try {
            val docRef = db.collection("facturas").add(facturaEmitida).await()
            docRef.id
        } catch (e: Exception) {
            throw Exception("Error al agregar factura: ${e.message}")
        }
    }

    suspend fun actualizarFactura(idDocumento: String, facturaEmitida: FacturaEmitida) {
        try {
            db.collection("facturas").document(idDocumento).set(facturaEmitida).await()
        } catch (e: Exception) {
            throw Exception("Error al actualizar factura: ${e.message}")
        }
    }
}