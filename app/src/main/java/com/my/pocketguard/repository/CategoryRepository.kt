package com.my.pocketguard.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.my.pocketguard.model.CategoryModel
import com.my.pocketguard.services.FirestoreService
import com.my.pocketguard.util.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val store: FirebaseFirestore,
    private val firestoreService: FirestoreService
) {
    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> get() = _uiState

    private val _categories = MutableStateFlow<List<CategoryModel>>(emptyList())
    val categories: StateFlow<List<CategoryModel>> get() = _categories

    private var listenerRegistration: ListenerRegistration? = null

    fun storeCategory(categoryName: String) {
        _uiState.value = UIState.Loading
        val userId = firebaseAuth.currentUser?.uid
        val uid = UUID.randomUUID().toString()
        val categoryData = hashMapOf(
            "id" to uid,
            "category_name" to categoryName,
            "created_by" to userId,
            "created_at" to FieldValue.serverTimestamp()
        )
        try {
            store.collection("categories").whereEqualTo("created_by", userId).whereEqualTo("category_name", categoryName).get().addOnSuccessListener {
                if (it.isEmpty) {
                    store.collection("categories").document(uid).set(categoryData)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("CATEGORY", "Successfully added category.")
                            } else {
                                Log.d("CATEGORY", "Failed to add category")
                            }
                        }
                } else {
                    _uiState.value = UIState.Error("Category already exists")
                }
            }
        } catch (e: Exception){
            Log.e("CATEGORY", "Category error", e)
        }
    }

    fun updateCategory(id: String, categoryName: String) {
        _uiState.value = UIState.Loading
        val categoryData = mapOf(
            "category_name" to categoryName,
        )
        try {
                    store.collection("categories").document(id).update(categoryData)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                _uiState.value = UIState.Success
                                Log.d("CATEGORY", "Successfully updated category.")
                            } else {
                                _uiState.value = UIState.Error("Failed to update category")
                                Log.d("CATEGORY", "Failed to update category")
                            }
                        }
        } catch (e: Exception){
            _uiState.value = UIState.Error(e.message.toString())
            Log.e("CATEGORY", "Category error", e)
        }
    }

    suspend fun fetchNewCategory(){
        _uiState.value = UIState.Loading
        try {
            firestoreService.fetchCategory().collect {
                _categories.value = it
                _uiState.value = UIState.Success
            }
        } catch (e: Exception) {
            _uiState.value = UIState.Error(e.message.toString())
        }
    }

    fun fetchCategory(){
        val userId = firebaseAuth.currentUser?.uid
        try {
        listenerRegistration = store.collection("categories").whereEqualTo("created_by", userId).orderBy("category_name", Query.Direction.ASCENDING)
        .addSnapshotListener { snapshot, e ->
            Log.d("FIRESTORE", "fetchCategory: $e")
            if(e != null){
                _uiState.value = UIState.Error(e.message.toString())
                return@addSnapshotListener
            }

            if(snapshot != null){
                val categories = snapshot.toObjects(CategoryModel::class.java)
                _uiState.value = UIState.Success
                _categories.value = categories
            }
        }
        } catch (e: Exception){
            _uiState.value = UIState.Error(e.message.toString())
            Log.e("CATEGORY", "Category fetch error", e)
        }
    }

    fun removeListener(){
        listenerRegistration?.remove()
    }

}