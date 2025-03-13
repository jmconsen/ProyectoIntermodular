package com.example.proyectointermodular.controlador

import com.example.proyectointermodular.modelo.FacturaEmitida
import com.example.proyectointermodular.modelo.FacturaRecibida
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import androidx.navigation.NavHostController

class FacturaRepository {

    private val db = FirebaseFirestore.getInstance()

    // Colecciones en Firestore
    private val facturasEmitidasRef = db.collection("facturasEmitidas")
    private val facturasRecibidasRef = db.collection("facturasRecibidas")

    suspend fun obtenerProximoNumeroFactura(): Int {
        val snapshot = facturasEmitidasRef
            .orderBy("numeroFactura", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .await()

        return if (snapshot.isEmpty) 1 else (snapshot.documents.first().getLong("numeroFactura")?.toInt() ?: 1) + 1
    }

    /** 🔹 Obtiene el siguiente número de factura recibida (Nueva función) */
    suspend fun obtenerProximoNumeroFacturaRecibida(): Int {
        val snapshot = facturasRecibidasRef
            .orderBy("numeroFactura", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .await()

        return if (snapshot.isEmpty) 1 else (snapshot.documents.first().getLong("numeroFactura")?.toInt() ?: 1) + 1
    }

    // Métodos para Facturas Emitidas
    suspend fun agregarFacturaEmitida(factura: FacturaEmitida, navHostController: NavHostController) {
        val numeroFactura = obtenerProximoNumeroFactura() // Obtiene el siguiente número autoincrementado
        val documentRef = facturasEmitidasRef.document() // Genera ID automático en Firestore
        val nuevaFactura = factura.copy(id = documentRef.id, numeroFactura = numeroFactura)

        documentRef.set(nuevaFactura).await() // Guarda en Firestore
        navHostController.navigate("PantallaFacturasEmitidas") // Navega de vuelta a PantallaFacturasEmitidas
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
        if (!factura.id.isNullOrEmpty()) {
            facturasEmitidasRef.document(factura.id).set(factura.copy(id = factura.id)).await()
        }
    }

    suspend fun eliminarFacturaEmitida(id: String) {
        facturasEmitidasRef.document(id).delete().await()
    }

    // Métodos para Facturas Recibidas
    suspend fun agregarFacturaRecibida(factura: FacturaRecibida, navHostController: NavHostController) {
        val numeroFactura = obtenerProximoNumeroFacturaRecibida() // Obtiene el siguiente número autoincrementado
        val documentRef = facturasRecibidasRef.document() // Genera ID automático en Firestore
        val nuevaFactura = factura.copy(id = documentRef.id, numeroFactura = numeroFactura)

        documentRef.set(nuevaFactura).await() // Guarda en Firestore
        navHostController.navigate("PantallaFacturasRecibidas") // Navega de vuelta a PantallaFacturasRecibidas
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
        if (!factura.id.isNullOrEmpty()) {
            facturasRecibidasRef.document(factura.id).set(factura.copy(id = factura.id)).await()
        }
    }

    suspend fun eliminarFacturaRecibida(id: String) {
        facturasRecibidasRef.document(id).delete().await()
    }
}