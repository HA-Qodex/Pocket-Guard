package com.my.pocketguard.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.my.pocketguard.component.AppBar
import com.my.pocketguard.component.AppButton
import com.my.pocketguard.component.AppDialog
import com.my.pocketguard.component.AppFAB
import com.my.pocketguard.component.CustomLoader
import com.my.pocketguard.component.ExpenseBottomSheet
import com.my.pocketguard.model.Expense
import com.my.pocketguard.navigation.AppRoutes
import com.my.pocketguard.ui.theme.Dimension.SizeL
import com.my.pocketguard.ui.theme.Dimension.SizeS
import com.my.pocketguard.ui.theme.Dimension.SizeXS
import com.my.pocketguard.ui.theme.Dimension.SizeXXL
import com.my.pocketguard.ui.theme.Dimension.TextM
import com.my.pocketguard.ui.theme.Dimension.TextS
import com.my.pocketguard.ui.theme.GrayColor
import com.my.pocketguard.ui.theme.PrimaryColor
import com.my.pocketguard.ui.theme.RedColor
import com.my.pocketguard.ui.theme.appTextStyle
import com.my.pocketguard.util.AppUtils.FAILED
import com.my.pocketguard.util.AppUtils.SUCCESSFUL
import com.my.pocketguard.util.UIState
import com.my.pocketguard.view.dashboard.ExpenseList
import com.my.pocketguard.viewmodel.ExpenseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseView(navController: NavController) {
    val viewModel: ExpenseViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val categories = viewModel.categories.collectAsState()
    val funds = viewModel.funds.collectAsState()
    val isLoading = remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogText by remember { mutableStateOf("") }
    var dialogIcon by remember { mutableStateOf(Icons.Default.CheckCircleOutline) }
    var showCreateDialog = remember { mutableStateOf(false) }
    val expenseSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var expenses = remember { mutableStateOf(emptyList<Expense>()) }


    LaunchedEffect(uiState) {
        when (uiState) {
            is UIState.Error -> {
                isLoading.value = false
                showDialog.value = true
                dialogIcon = Icons.Default.WarningAmber
                dialogTitle = FAILED
                dialogText = (uiState as UIState.Error).message
            }

            is UIState.Loading -> {
                isLoading.value = true
            }

            is UIState.Success -> {
                isLoading.value = false
                if ((uiState as UIState.Success).tag == "EXPENSE") {
                    expenses.value = emptyList<Expense>()
                    showDialog.value = true
                    dialogIcon = Icons.Default.CheckCircleOutline
                    dialogTitle = SUCCESSFUL
                    dialogText = "Data stored successfully"
                }
            }
        }
    }

    if (isLoading.value) {
        CustomLoader()
    }

    if (showDialog.value) {
        AppDialog(
            title = dialogTitle,
            text = dialogText,
            icon = dialogIcon,
            iconColor = if (dialogTitle == SUCCESSFUL) PrimaryColor else RedColor,
            confirm = "OK",
            showDialog = showDialog
        )
    }

    if (showCreateDialog.value) {
        ExpenseBottomSheet(
            expenseSheetState,
            showCreateDialog,
            categories.value,
            funds.value,
            expenses
        )
    }

    Scaffold(
        topBar = { AppBar(navController, "Expense", route = AppRoutes.DASHBOARD.route) },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = SizeL, vertical = SizeXS)
        ) {
            if (expenses.value.isNotEmpty()) {
                ExpenseList(expenses.value)
                Spacer(modifier = Modifier.weight(1f))
                HorizontalDivider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SizeL),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        "Total",
                        style = appTextStyle.copy(
                            fontSize = TextS,
                            fontWeight = FontWeight.Bold,
                            color = RedColor
                        )
                    )
                    Spacer(modifier = Modifier.width(SizeS))
                    Text(
                        "৳${
                            expenses.value.sumOf {
                                it.amount?.toLong() ?: 0L
                            }
                        }",
                        style = appTextStyle.copy(
                            fontSize = TextS,
                            fontWeight = FontWeight.Bold,
                            color = RedColor
                        )
                    )
                }
                Spacer(modifier = Modifier.height(SizeS))
                Row {
                    AppButton(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            showCreateDialog.value = true
                        }, text = "Add More"
                    )
                    Spacer(modifier = Modifier.width(SizeXS))
                    AppButton(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            viewModel.storeExpense(expenses.value)
                        }, text = "Submit"
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(onClick = {
                            showCreateDialog.value = true
                        }),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Receipt, contentDescription = "expense",
                        modifier = Modifier.size(SizeXXL),
                        tint = GrayColor.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(SizeXS))
                    Text(
                        "Add Expense",
                        style = appTextStyle.copy(
                            fontSize = TextM,
                            color = GrayColor.copy(alpha = 0.6f)
                        )
                    )
                }
            }
        }
    }
}