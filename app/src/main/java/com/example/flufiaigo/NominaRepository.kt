package com.example.flufiaigo

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class NominaRepository(private val dao: NominaDao) {
    private val collection = FirebaseFirestore.getInstance().collection("nominas")

    val nominas = dao.getAllNominas()

    suspend fun insertNomina(nomina: NominaModel) {
        dao.insertNomina(nomina)
        collection.document(nomina.id).set(nomina.toMap())
    }

    suspend fun deleteNomina(nomina: NominaModel) {
        dao.deleteNomina(nomina)
        collection.document(nomina.id).delete()
    }
    
    fun syncFromFirestore(scope: CoroutineScope) {
        collection.addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) return@addSnapshotListener
            scope.launch {
                snapshot.documents
                    .mapNotNull { it.toNominaModel() }
                    .forEach { dao.insertNomina(it) }
            }
        }
    }
}
