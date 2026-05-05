package com.example.flufiaigo

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class IngresoRepository(private val dao: IngresoDao) {
    private val collection = FirebaseFirestore.getInstance().collection("ingresos")

    val ingresos = dao.getAllIngresos()

    suspend fun insertIngreso(ingreso: IngresoModel) {
        dao.insertIngreso(ingreso)
        collection.document(ingreso.id).set(ingreso.toMap())
    }

    suspend fun deleteIngreso(ingreso: IngresoModel) {
        dao.deleteIngreso(ingreso)
        collection.document(ingreso.id).delete()
    }
    fun syncFromFirestore(scope: CoroutineScope) {
        collection.addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) return@addSnapshotListener
            scope.launch {
                snapshot.documents
                    .mapNotNull { it.toIngresoModel() }
                    .forEach { dao.insertIngreso(it) }
            }
        }
    }

}