package com.example.flufiaigo

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class GastoRepository(private val dao: GastoDao) {
    private val collection = FirebaseFirestore.getInstance().collection("gastos")

    val gastos = dao.getAllGastos()

    suspend fun insertGasto(gasto: GastoModel) {
        dao.insertGasto(gasto)
        collection.document(gasto.id).set(gasto.toMap())
    }

    suspend fun deleteGasto(gasto: GastoModel) {
        dao.deleteGasto(gasto)
        collection.document(gasto.id).delete()
    }
    
    fun syncFromFirestore(scope: CoroutineScope) {
        collection.addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) return@addSnapshotListener
            scope.launch {
                snapshot.documents
                    .mapNotNull { it.toGastoModel() }
                    .forEach { dao.insertGasto(it) }
            }
        }
    }
}
