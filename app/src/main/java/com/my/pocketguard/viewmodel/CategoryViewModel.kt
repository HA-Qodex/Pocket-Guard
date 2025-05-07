package com.my.pocketguard.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.pocketguard.model.CategoryModel
import com.my.pocketguard.model.UserModel
import com.my.pocketguard.repository.CategoryRepository
import com.my.pocketguard.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val categoryRepository: CategoryRepository) :
    ViewModel() {

    val uiState: StateFlow<UIState> get() = categoryRepository.uiState
    val categories: StateFlow<List<CategoryModel>> get() = categoryRepository.categories

    init {
        viewModelScope.launch {
            categoryRepository.fetchNewCategory()
        }
    }

    fun storeCategory(categoryName: String){
        categoryRepository.storeCategory(categoryName.trim().lowercase())
    }

    fun updateCategory(id: String, categoryName: String){
        categoryRepository.updateCategory(id, categoryName.trim().lowercase())

    }

    override fun onCleared() {
        categoryRepository.removeListener()
        super.onCleared()
    }
}