package com.my.pocketguard.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.my.pocketguard.model.FundModel
import com.my.pocketguard.util.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID
import javax.inject.Inject

class FundRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val store: FirebaseFirestore
) {
    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> get() = _uiState

    private val _funds = MutableStateFlow<List<FundModel>>(emptyList())
    val funds: StateFlow<List<FundModel>> get() = _funds

    private var listenerRegistration: ListenerRegistration? = null

    fun storeFund(fundName: String, amount: Int) {
        _uiState.value = UIState.Loading
        val userId = firebaseAuth.currentUser?.uid
        val uid = UUID.randomUUID().toString()
        val fundData = hashMapOf(
            "id" to uid,
            "fund_name" to fundName,
            "fund_amount" to amount,
            "created_by" to userId,
            "created_at" to FieldValue.serverTimestamp()
        )
        try {
            store.collection("funds").whereEqualTo("created_by", userId)
                .whereEqualTo("fund_name", fundName).get().addOnSuccessListener {
                if (it.isEmpty) {
                    store.collection("funds").document(uid).set(fundData)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                _uiState.value = UIState.Success
                                Log.d("FUNDS", "Successfully added fund.")
                            } else {
                                _uiState.value = UIState.Error("Failed to update fund")
                                Log.e("FUNDS", "Failed to add fund")
                            }
                        }
                } else {
                    val updateFundData = mapOf(
                        "fund_name" to fundName,
                        "fund_amount" to amount
                    )
                    store.collection("funds").document(it.documents.first().id)
                        .update(updateFundData)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                _uiState.value = UIState.Success
                                Log.d("FUNDS", "Successfully updated fund.")
                            } else {
                                _uiState.value = UIState.Error("Failed to update fund")
                                Log.e("FUNDS", "Failed to update fund")
                            }
                        }
                }
            }
        } catch (e: Exception) {
            Log.e("FUNDS", "Fund error", e)
        }
    }

//    fun updateFund(id: String, fundName: String) {
//        _uiState.value = UIState.Loading
//        val fundData = mapOf(
//            "fund_name" to fundName,
//        )
//        try {
//            store.collection("funds").document(id).update(fundData)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        _uiState.value = UIState.Success
//                        Log.d("FUNDS", "Successfully updated fund.")
//                    } else {
//                        _uiState.value = UIState.Error("Failed to update fund")
//                        Log.d("FUNDS", "Failed to update fund")
//                    }
//                }
//        } catch (e: Exception) {
//            _uiState.value = UIState.Error(e.message.toString())
//            Log.e("FUNDS", "Fund error", e)
//        }
//    }

    fun fetchFund() {
        val userId = firebaseAuth.currentUser?.uid
        try {
        listenerRegistration = store.collection("funds").whereEqualTo("created_by", userId)
            .orderBy("fund_name", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    _uiState.value = UIState.Error(e.message.toString())
                    Log.e("FUNDS", e.message.toString())
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val funds = snapshot.toObjects(FundModel::class.java)
                    _uiState.value = UIState.Success
                    _funds.value = funds
                }
            }
        } catch (e: Exception){
            _uiState.value = UIState.Error(e.message.toString())
            Log.e("FUNDS", "Fund fetch error", e)
        }
    }

    fun removeListener() {
        listenerRegistration?.remove()
    }

}