package com.example.proyectointermodular.controlador

import com.example.proyectointermodular.modelo.FacturaRecibida
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FacturaRecibidaRepository {

    private val db = FirebaseFirestore.getInstance()

    fun obtenerFacturas(onComplete: (List<Pair<String, FacturaRecibida>>) -> Unit) {
        db.collection("facturas").addSnapshotListener { snapshot, e ->
            if (e != null) {
                println("Error al obtener las facturas: ${e.message}")
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val facturas = snapshot.documents.mapNotNull { document ->
                    val FacturaRecibida = document.toObject(FacturaRecibida::class.java)
                    FacturaRecibida?.let { document.id to it }
                }
                onComplete(facturas)
            }
        }
    }

    suspend fun eliminarFactura(id: String) {
        try {
            db.collection("facturas").document(id).delete().await()
            println("Factura eliminada con éxito")
        } catch (e: Exception) {
            println("Error al eliminar factura: ${e.message}")
        }
    }

    suspend fun agregarFactura(facturaRecibida: FacturaRecibida): String {
        return try {
            val docRef = db.collection("facturas").add(facturaRecibida).await()
            docRef.id
        } catch (e: Exception) {
            throw Exception("Error al agregar factura: ${e.message}")
        }
    }

    suspend fun actualizarFactura(idDocumento: String, facturaRecibida: FacturaRecibida) {
        try {
            db.collection("facturas").document(idDocumento).set(facturaRecibida).await()
        } catch (e: Exception) {
            throw Exception("Error al actualizar factura: ${e.message}")
        }
    }
}