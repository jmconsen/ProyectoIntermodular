package com.example.proyectointermodular.controlador

import com.example.proyectointermodular.modelo.Factura
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FacturaRepository {

    private val db = FirebaseFirestore.getInstance()

    fun obtenerFacturas(onComplete: (List<Pair<String, Factura>>) -> Unit) {
        db.collection("facturas").addSnapshotListener { snapshot, e ->
            if (e != null) {
                println("Error al obtener los facturas: ${e.message}")
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val facturas = snapshot.documents.mapNotNull { document ->
                    val factura = document.toObject(Factura::class.java)
                    factura?.let { document.id to it }
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

    suspend fun agregarFactura(factura: Factura): String {
        return try {
            val docRef = db.collection("facturas").add(factura).await()
            docRef.id
        } catch (e: Exception) {
            throw Exception("Error al agregar factura: ${e.message}")
        }
    }

    suspend fun actualizarFactura(idDocumento: String, factura: Factura) {
        try {
            db.collection("facturas").document(idDocumento).set(factura).await()
        } catch (e: Exception) {
            throw Exception("Error al actualizar factura: ${e.message}")
        }
    }
}