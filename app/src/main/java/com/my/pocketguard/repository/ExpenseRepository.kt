package com.my.pocketguard.repository

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.my.pocketguard.model.CategoryModel
import com.my.pocketguard.model.Expense
import com.my.pocketguard.model.FundModel
import com.my.pocketguard.services.FirestoreService
import com.my.pocketguard.util.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID
import javax.inject.Inject

class ExpenseRepository @Inject constructor(
    private val firestoreService: FirestoreService
) {
    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> get() = _uiState

    private val _categories = MutableStateFlow<List<CategoryModel>>(emptyList())
    val categories: StateFlow<List<CategoryModel>> get() = _categories

    private val _funds = MutableStateFlow<List<FundModel>>(emptyList())
    val funds: StateFlow<List<FundModel>> get() = _funds

    suspend fun fetchCategory() {
        _uiState.value = UIState.Loading
        try {
            firestoreService.fetchCategory().collect {
                _categories.value = it
                _uiState.value = UIState.Success()
            }
        } catch (e: Exception) {
            _uiState.value = UIState.Error(e.message.toString())
        }
    }

    suspend fun fetchFunds() {
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

    fun storeExpense(
        expenseList: Map<String, List<Expense>>
    ) {
        _uiState.value = UIState.Loading
        firestoreService.storeExpense(expenseList) { uiState ->
            _uiState.value = uiState
        }
    }
}