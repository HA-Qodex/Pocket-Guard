package com.my.pocketguard.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.my.pocketguard.model.FundModel
import com.my.pocketguard.model.UserModel
import com.my.pocketguard.util.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID
import javax.inject.Inject

class DashboardRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val store: FirebaseFirestore
) {
    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val currentUser: StateFlow<FirebaseUser?> get() = _user

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> get() = _uiState

    private val _users = MutableStateFlow<List<UserModel>>(emptyList())
    val users: StateFlow<List<UserModel>> get() = _users

    private var listenerRegistration: ListenerRegistration? = null


    fun getCurrentUser() {
        _user.value = firebaseAuth.currentUser
    }

    fun fetchUser(){
        listenerRegistration = store.collection("users").addSnapshotListener { snapshot, e ->
            if(e != null){
                _uiState.value = UIState.Error(e.message.toString())
                return@addSnapshotListener
            }

            if(snapshot != null){
                val users = snapshot.toObjects(UserModel::class.java)
                _uiState.value = UIState.Success
                _users.value = users
            }
        }
    }

    fun removeListener(){
        listenerRegistration?.remove()
    }

}