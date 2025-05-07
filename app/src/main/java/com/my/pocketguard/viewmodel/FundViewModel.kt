package com.my.pocketguard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.pocketguard.model.FundModel
import com.my.pocketguard.repository.FundRepository
import com.my.pocketguard.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FundViewModel @Inject constructor(private val fundRepository: FundRepository) :
    ViewModel() {
    val uiState: StateFlow<UIState> get() = fundRepository.uiState
    val funds: StateFlow<List<FundModel>> get() = fundRepository.funds

    init {
        viewModelScope.launch {
            fundRepository.fetchFund()
        }
    }

    fun storeFund(categoryName: String, amount: Int) {
        fundRepository.storeFund(categoryName.trim().lowercase(), amount)
    }

    override fun onCleared() {
        fundRepository.removeListener()
        super.onCleared()
    }
}