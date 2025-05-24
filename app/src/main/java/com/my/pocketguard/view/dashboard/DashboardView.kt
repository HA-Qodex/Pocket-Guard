package com.my.pocketguard.view.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.himanshoe.charty.common.ChartColor
import com.himanshoe.charty.pie.model.PieChartData
import com.my.pocketguard.component.CategoryBottomSheet
import com.my.pocketguard.component.CategoryChart
import com.my.pocketguard.component.CustomLoader
import com.my.pocketguard.component.DashboardAppBar
import com.my.pocketguard.component.FundBottomSheet
import com.my.pocketguard.navigation.AppRoutes
import com.my.pocketguard.ui.theme.ChartColors
import com.my.pocketguard.ui.theme.PrimaryColorLite
import com.my.pocketguard.ui.theme.TextColor
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
    val categories by viewModel.categoryList.collectAsState()
    val fund by viewModel.fundList.collectAsState()
    val fundSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val categorySheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showFundSheet = remember { mutableStateOf(false) }
    var showCategorySheet = remember { mutableStateOf(false) }


    LaunchedEffect(uiState) {
        isLoading = when (uiState) {
            is UIState.Error -> {
                false
            }

            is UIState.Loading -> {
                true
            }

            is UIState.Success -> {
                false
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
            Column(
            ) {
                CategoryChart(data = categories.mapIndexed { index, category ->
                    PieChartData(
                        label = category.title.replaceFirstChar { it.uppercase() },
                        value = category.amount.toFloat(),
                        color = ChartColor.Solid(
                            ChartColors[index]
                        ),
                        labelColor = ChartColor.Solid(TextColor)
                    )
                }.toList())
                ExpenseList(expenses)
            }
        }
    }
}