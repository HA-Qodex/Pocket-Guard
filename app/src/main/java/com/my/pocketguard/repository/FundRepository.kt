package com.my.pocketguard.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.my.pocketguard.model.FundModel
import com.my.pocketguard.services.FirestoreService
import com.my.pocketguard.util.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID
import javax.inject.Inject

class FundRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val store: FirebaseFirestore,
    private val firestoreService: FirestoreService
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
            "remaining_amount" to amount,
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
                                _uiState.value = UIState.Success()
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
                                _uiState.value = UIState.Success()
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

    suspend fun fetchFund() {
        _uiState.value = UIState.Loading
        try {
            firestoreService.fetchFunds().collect {
                _funds.value = it
                _uiState.value = UIState.Success()
            }
        } catch (e: Exception) {
            _uiState.value = UIState.Error(e.message.toString())
        }
    }

    fun removeListener() {
        listenerRegistration?.remove()
    }

}