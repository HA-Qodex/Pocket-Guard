package com.my.pocketguard.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class DashboardRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val currentUser: StateFlow<FirebaseUser?> get() = _user

    fun getCurrentUser() {
        _user.value = firebaseAuth.currentUser
    }
}