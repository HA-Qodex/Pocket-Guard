package com.my.pocketguard.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.my.pocketguard.util.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID
import javax.inject.Inject

class ExpenseRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val store: FirebaseFirestore
) {
    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> get() = _uiState

//    fun storeExpense(expenseName: String) {
//        _uiState.value = UIState.Loading
//        val userId = firebaseAuth.currentUser?.uid
//        val uid = UUID.randomUUID().toString()
//        val expenseData = hashMapOf(
//            "id" to uid,
//            "expense_title" to expenseName,
//            "expense_date" to expenseName,
//            "expense_amount" to expenseName,
//            "expense_id" to categoryId,
//            "fund_id" to fundId,
//            "user_id" to userId,
//            "created_at" to FieldValue.serverTimestamp()
//        )
//        try {
//            store.collection("expenses").document(uid).set(expenseData)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        Log.d("EXPENSES", "Successfully added expense.")
//                    } else {
//                        Log.d("EXPENSES", "Failed to add expense")
//                    }
//                }
//        } catch (e: Exception) {
//            Log.e("EXPENSES", "Expense error", e)
//        }
//    }
}