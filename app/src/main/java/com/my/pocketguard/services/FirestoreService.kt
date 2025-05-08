package com.my.pocketguard.services

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.my.pocketguard.model.CategoryModel
import com.my.pocketguard.model.FundModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FirestoreService @Inject constructor(
    private val store: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    fun fetchCategory(): Flow<List<CategoryModel>> = callbackFlow {
        val userId = auth.currentUser?.uid
        val listener = store.collection("categories").whereEqualTo("created_by", userId)
            .orderBy("category_name", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                Log.d("FIRESTORE", "fetchCategory: $error")
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.toObjects(CategoryModel::class.java) ?: emptyList())
            }
        awaitClose {
            listener.remove()
        }
    }

    fun fetchFunds(): Flow<List<FundModel>> = callbackFlow {
        val userId = auth.currentUser?.uid
        val listener = store.collection("funds").whereEqualTo("created_by", userId)
            .orderBy("fund_name", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                Log.d("FIRESTORE", "fetchFunds: $error")
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.toObjects(FundModel::class.java) ?: emptyList())
            }
        awaitClose {
            listener.remove()
        }
    }
}