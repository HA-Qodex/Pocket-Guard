package com.my.pocketguard.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.my.pocketguard.component.AppBar
import com.my.pocketguard.component.AppDropdown
import com.my.pocketguard.component.CustomLoader
import com.my.pocketguard.model.CategoryModel
import com.my.pocketguard.model.FundModel
import com.my.pocketguard.navigation.AppRoutes
import com.my.pocketguard.ui.theme.Dimension.LargePadding
import com.my.pocketguard.util.UIState
import com.my.pocketguard.viewmodel.CategoryViewModel
import com.my.pocketguard.viewmodel.ExpenseViewModel

@Composable
fun ExpenseView(navController: NavController) {
    val viewModel: ExpenseViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val categories = viewModel.categories.collectAsState()
    val funds = viewModel.funds.collectAsState()
    val selectedCategory =
        remember { mutableStateOf(CategoryModel(categoryName = "Select Category")) }
    val selectedFunds = remember { mutableStateOf(FundModel(fundName = "Select Fund")) }
    val isLoading = remember { mutableStateOf(false) }

    when (uiState) {
        is UIState.Error -> {
            isLoading.value = false
        }

        is UIState.Loading -> {
            isLoading.value = true
        }

        is UIState.Success -> {
            isLoading.value = false
        }
    }

    if (isLoading.value) {
        CustomLoader()
    }

    Scaffold(topBar = { AppBar(navController, "Expense", route = AppRoutes.DASHBOARD.route) }) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = LargePadding)
        ) {
            Row {
                AppDropdown(
                    modifier = Modifier.weight(1f),
                    title = "Fund", items = funds.value, onItemSelected = {
                        selectedFunds.value = it
                    }, selectedItem = selectedFunds.value, itemLabel = {
                        "${it.fundName?.replaceFirstChar { it.uppercase() }} | ${it.fundAmount ?: ""}"
                    })
                AppDropdown(
                    modifier = Modifier.weight(1f),
                    title = "Category", items = categories.value, onItemSelected = {
                        selectedCategory.value = it
                    }, selectedItem = selectedCategory.value, itemLabel = {
                        it.categoryName.toString().replaceFirstChar { it.uppercase() }
                    })
            }
        }
    }
}