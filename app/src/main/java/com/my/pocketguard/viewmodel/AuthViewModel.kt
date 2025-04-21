package com.my.pocketguard.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseUser
import com.my.pocketguard.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository): ViewModel() {

    val currentUser: StateFlow<FirebaseUser?> get() = authRepository.currentUser


    init {
        viewModelScope.launch {
            authRepository.getCurrentUser()
        }
    }

    fun handleGoogleAuth(activity: Activity, navController: NavController) {
        viewModelScope.launch {
        authRepository.handleGoogleAuth(activity, navController)
        }
    }

    fun sighOut(activity: Activity){
        viewModelScope.launch {
            authRepository.signOut(activity)
        }

    }

}