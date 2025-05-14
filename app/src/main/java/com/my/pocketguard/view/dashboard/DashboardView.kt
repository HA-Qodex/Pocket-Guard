package com.my.pocketguard.view.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.my.pocketguard.component.CategoryBottomSheet
import com.my.pocketguard.component.CustomLoader
import com.my.pocketguard.component.DashboardAppBar
import com.my.pocketguard.component.FundBottomSheet
import com.my.pocketguard.navigation.AppRoutes
import com.my.pocketguard.ui.theme.PrimaryColorLite
import com.my.pocketguard.util.UIState
import com.my.pocketguard.viewmodel.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardView(navController: NavController) {
    val context = LocalContext.current
    val viewModel: DashboardViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val currentUser = viewModel.currentUser.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    val expenses by viewModel.expenseList.collectAsState()
    val fundSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val categorySheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showFundSheet = remember { mutableStateOf(false) }
    var showCategorySheet = remember { mutableStateOf(false) }


    LaunchedEffect(uiState) {
        when (uiState) {
            is UIState.Error -> {
                isLoading = false
            }

            is UIState.Loading -> {
                isLoading = true
            }

            is UIState.Success -> {
                isLoading = false
            }
        }
    }

    if (isLoading) {
        CustomLoader()
    }

    if (showFundSheet.value) {
        FundBottomSheet(showBottomSheet = showFundSheet, sheetState = fundSheetState)
    }

    if (showCategorySheet.value) {
        CategoryBottomSheet(showBottomSheet = showCategorySheet, sheetState = categorySheetState)
    }

    Scaffold(
        containerColor = PrimaryColorLite,
        topBar = {
            DashboardAppBar(
                context = context,
                imageUrl = currentUser.value?.photoUrl.toString(),
                name = currentUser.value?.displayName.toString(),
                fundClick = { showFundSheet.value = true },
                addClick = { navController.navigate(AppRoutes.EXPENSE.route) },
                categoryClick = {
                    showCategorySheet.value = true
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    ExpenseList(expenses)
                }
            }
        }
    }
}