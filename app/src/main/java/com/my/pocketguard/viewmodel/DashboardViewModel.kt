package com.my.pocketguard.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.my.pocketguard.model.AnalyticsModel
import com.my.pocketguard.model.Expense
import com.my.pocketguard.model.UserModel
import com.my.pocketguard.repository.DashboardRepository
import com.my.pocketguard.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val dashboardRepository: DashboardRepository) :
    ViewModel() {
    val currentUser: StateFlow<FirebaseUser?> get() = dashboardRepository.currentUser
    val uiState: StateFlow<UIState> get() = dashboardRepository.uiState
    val expenseList: StateFlow<List<Expense>> get() = dashboardRepository.expenseList
    val categoryList: StateFlow<List<AnalyticsModel>> get() = dashboardRepository.categoryList
    val fundList: StateFlow<List<AnalyticsModel>> get() = dashboardRepository.fundList

    init {
        viewModelScope.launch {
            async { dashboardRepository.getCurrentUser() }
           async { dashboardRepository.fetchExpense() }
            async { dashboardRepository.fetchExpenseAnalytics() }
        }
    }

    override fun onCleared() {
        dashboardRepository.removeListener()
        super.onCleared()
    }
}