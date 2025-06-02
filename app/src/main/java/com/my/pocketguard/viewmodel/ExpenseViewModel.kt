package com.my.pocketguard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.my.pocketguard.model.CategoryModel
import com.my.pocketguard.model.Expense
import com.my.pocketguard.model.FundModel
import com.my.pocketguard.repository.ExpenseRepository
import com.my.pocketguard.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(private val expenseRepository: ExpenseRepository) :
    ViewModel() {

    val uiState: StateFlow<UIState> get() = expenseRepository.uiState
    val categories: StateFlow<List<CategoryModel>> get() = expenseRepository.categories
    val funds: StateFlow<List<FundModel>> get() = expenseRepository.funds

    init {
        viewModelScope.launch {
            async { expenseRepository.fetchFunds() }
            async { expenseRepository.fetchCategory() }
        }
    }

    fun storeExpense(
        expenseList: List<Expense>
    ) {
        viewModelScope.launch {
            expenseRepository.storeExpense(
                expenseList.groupBy { it.fund?.id.toString() }
            )
        }
    }
}