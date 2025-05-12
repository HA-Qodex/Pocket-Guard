package com.my.pocketguard.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.my.pocketguard.model.Expense
import com.my.pocketguard.model.UserModel
import com.my.pocketguard.repository.DashboardRepository
import com.my.pocketguard.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val dashboardRepository: DashboardRepository) :
    ViewModel() {
    val currentUser: StateFlow<FirebaseUser?> get() = dashboardRepository.currentUser
    val uiState: StateFlow<UIState> get() = dashboardRepository.uiState
    val users: StateFlow<List<UserModel>> get() = dashboardRepository.users
    val expenseList: StateFlow<List<Expense>> get() = dashboardRepository.expenseList



    init {
        viewModelScope.launch {
            dashboardRepository.getCurrentUser()
            dashboardRepository.fetchExpense()
//            delay(3000)
//            dashboardRepository.fetchUser()
        }
    }

    override fun onCleared() {
        dashboardRepository.removeListener()
        super.onCleared()
    }
}