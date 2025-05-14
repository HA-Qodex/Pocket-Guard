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
import kotlin.String

class FundRepository @Inject constructor(
    private val firestoreService: FirestoreService
) {
    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> get() = _uiState

    private val _funds = MutableStateFlow<List<FundModel>>(emptyList())
    val funds: StateFlow<List<FundModel>> get() = _funds

    private var listenerRegistration: ListenerRegistration? = null

    fun storeFund(fundName: String, amount: Int) {
        _uiState.value = UIState.Loading
        val fundData = hashMapOf<String, Any>(
            "fund_name" to fundName,
            "fund_amount" to amount,
            "remaining_amount" to amount
        )
        try {
            firestoreService.storeFund(fundData) {
                _uiState.value = it
            }
        } catch (e: Exception) {
            _uiState.value = UIState.Error("Failed to add fund")
            Log.e("FUNDS", "Fund error", e)
        }
    }

    fun updateFund(id: String, fundName: String, amount: Long, remainingAmount: Long) {
        _uiState.value = UIState.Loading
        val fundData = hashMapOf<String, Any>(
            "id" to id,
            "fund_name" to fundName,
            "fund_amount" to amount,
            "remaining_amount" to remainingAmount
        )
        try {
            if(remainingAmount < 0)
                throw Exception("Remaining balance is negative")
            firestoreService.updateFund(fundData) {
                _uiState.value = it
            }
        } catch (e: Exception) {
            _uiState.value = UIState.Error(e.message.toString())
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