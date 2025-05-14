package com.my.pocketguard.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.my.pocketguard.model.CategoryModel
import com.my.pocketguard.services.FirestoreService
import com.my.pocketguard.util.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val firestoreService: FirestoreService
) {
    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> get() = _uiState

    private val _categories = MutableStateFlow<List<CategoryModel>>(emptyList())
    val categories: StateFlow<List<CategoryModel>> get() = _categories

    private var listenerRegistration: ListenerRegistration? = null

    fun storeCategory(categoryName: String) {
        _uiState.value = UIState.Loading
        val categoryData = hashMapOf<String, Any>(
            "category_name" to categoryName
        )
        try {
            firestoreService.storeCategory(categoryData) {
                _uiState.value = it
            }
        } catch (e: Exception) {
            _uiState.value = UIState.Error(e.message.toString())
            Log.e("CATEGORY", "Category error", e)
        }
    }

    fun updateCategory(id: String, categoryName: String) {
        _uiState.value = UIState.Loading
        val categoryData = hashMapOf<String, Any>(
            "id" to id,
            "category_name" to categoryName,
        )
        try {
            firestoreService.updateCategory(categoryData) {
                _uiState.value = it
            }
        } catch (e: Exception) {
            _uiState.value = UIState.Error(e.message.toString())
            Log.e("CATEGORY", "Category error", e)
        }
    }

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

    fun removeListener() {
        listenerRegistration?.remove()
    }

}