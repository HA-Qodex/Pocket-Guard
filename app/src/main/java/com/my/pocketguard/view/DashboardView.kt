package com.my.pocketguard.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.my.pocketguard.component.DashboardAppBar
import com.my.pocketguard.component.AppCalender
import com.my.pocketguard.component.CategoryBottomSheet
import com.my.pocketguard.component.FundBottomSheet
import com.my.pocketguard.model.UserModel
import com.my.pocketguard.navigation.AppRoutes
import com.my.pocketguard.ui.theme.BackgroundColor
import com.my.pocketguard.ui.theme.BackgroundColorLite
import com.my.pocketguard.util.UIState
import com.my.pocketguard.viewmodel.DashboardViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardView(navController: NavController) {
    val context = LocalContext.current
    val viewModel: DashboardViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val currentUser = viewModel.currentUser.collectAsState()
    val users by viewModel.users.collectAsState()
    val fundSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val categorySheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showFundSheet = remember { mutableStateOf(false) }
    var showCategorySheet = remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<Long?>(System.currentTimeMillis()) }
    var showDatePicker = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
    }

    if (showFundSheet.value) {
        FundBottomSheet(showBottomSheet = showFundSheet, sheetState = fundSheetState)
    }

    if (showCategorySheet.value) {
        CategoryBottomSheet(showBottomSheet = showCategorySheet, sheetState = categorySheetState)
    }

    if (showDatePicker.value) {
        AppCalender(
            selectedDate = selectedDate,
            onDateSelected = {
                selectedDate = it
                showDatePicker.value = false
            },
            onDismiss = {
                showDatePicker.value = false
            }
        )
    }

    Scaffold(
        containerColor = BackgroundColorLite,
        topBar = {
            DashboardAppBar(
                context = context,
                imageUrl = currentUser.value?.photoUrl.toString(),
                name = currentUser.value?.displayName.toString(),
                fundClick = { showFundSheet.value = true },
//                    navController.navigate(AppRoutes.FUND.route) },
                addClick = { showDatePicker.value = true },
                categoryClick = {
                    showCategorySheet.value = true
//                    navController.navigate(AppRoutes.CATEGORY.route)
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
                        .background(BackgroundColor)
                ) {
                    when (uiState) {
                        is UIState.Loading -> {
                            CircularProgressIndicator()
                        }

                        is UIState.Success -> {
                            LazyColumn {
                                items(users) {
                                    Text(it.name.toString())
                                }
                            }
                        }

                        is UIState.Error -> {
                            Text(text = (uiState as UIState.Error).message)
                        }
                    }

                }
            }
        }
    }
}