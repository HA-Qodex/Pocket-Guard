package com.my.pocketguard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.my.pocketguard.repository.DashboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val dashboardRepository: DashboardRepository): ViewModel() {
    val currentUser: StateFlow<FirebaseUser?> get() = dashboardRepository.currentUser

    init {
        viewModelScope.launch {
            dashboardRepository.getCurrentUser()
        }
    }

}