package com.my.pocketguard.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.my.pocketguard.model.AnalyticsModel
import com.my.pocketguard.model.Expense
import com.my.pocketguard.model.FundModel
import com.my.pocketguard.model.UserModel
import com.my.pocketguard.services.FirestoreService
import com.my.pocketguard.util.UIState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject

class DashboardRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestoreService: FirestoreService
) {
    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val currentUser: StateFlow<FirebaseUser?> get() = _user

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> get() = _uiState

    private val _expenseList = MutableStateFlow<List<Expense>>(emptyList())
    val expenseList: StateFlow<List<Expense>> get() = _expenseList

    private val _categoryList = MutableStateFlow<List<AnalyticsModel>>(emptyList())
    val categoryList: StateFlow<List<AnalyticsModel>> get() = _categoryList

    private val _fundList = MutableStateFlow<List<AnalyticsModel>>(emptyList())
    val fundList: StateFlow<List<AnalyticsModel>> get() = _fundList

    private var listenerRegistration: ListenerRegistration? = null


    fun getCurrentUser() {
        _user.value = firebaseAuth.currentUser
    }

    suspend fun fetchExpense() {
        _uiState.value = UIState.Loading
        try {
            firestoreService.fetchExpense().collect {
                _expenseList.emit(it)
                _uiState.value = UIState.Success()
            }
        } catch (e: Exception) {
            _uiState.value = UIState.Error(e.message.toString())
        }
    }

    suspend fun fetchExpenseAnalytics() {
        _uiState.value = UIState.Loading
        try {
            firestoreService.fetchExpenseAnalytics().collect { expenses ->
                _categoryList.emit(expenses.sortedByDescending { data -> data.amount }.groupBy { expense ->
                    expense.category
                }.map { (category, data) ->
                    AnalyticsModel(
                        id = category?.id.toString(),
                        title = category?.categoryName.toString(),
                        amount = data.sumOf { it.amount?.toLong() ?: 0L })
                }
                )

                _fundList.emit(expenses.groupBy { expense ->
                    expense.fund
                }.map { (category, data) ->
                    AnalyticsModel(
                        id = category?.id.toString(),
                        title = category?.fundName.toString(),
                        amount = data.sumOf { it.amount?.toLong() ?: 0L })
                }
                )
                _uiState.value = UIState.Success()
            }
        } catch (e: Exception) {
            Log.e("ANALYTICS", e.message.toString(), )
            _uiState.value = UIState.Error(e.message.toString())
        }
    }

    fun removeListener() {
        listenerRegistration?.remove()
    }

}