package com.my.pocketguard.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.Timestamp
import com.my.pocketguard.component.AppBar
import com.my.pocketguard.component.AppButton
import com.my.pocketguard.component.AppCalender
import com.my.pocketguard.component.AppDialog
import com.my.pocketguard.component.AppDisableTextField
import com.my.pocketguard.component.AppDropdown
import com.my.pocketguard.component.AppTextField
import com.my.pocketguard.component.CustomLoader
import com.my.pocketguard.model.CategoryModel
import com.my.pocketguard.model.FundModel
import com.my.pocketguard.navigation.AppRoutes
import com.my.pocketguard.ui.theme.Dimension.SizeL
import com.my.pocketguard.ui.theme.PrimaryColor
import com.my.pocketguard.ui.theme.RedColor
import com.my.pocketguard.util.AppUtils.FAILED
import com.my.pocketguard.util.AppUtils.SUCCESSFUL
import com.my.pocketguard.util.AppUtils.convertMillisToDate
import com.my.pocketguard.util.UIState
import com.my.pocketguard.viewmodel.ExpenseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseView(navController: NavController) {
    val viewModel: ExpenseViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val categories = viewModel.categories.collectAsState()
    val funds = viewModel.funds.collectAsState()
    val expenseAmount = remember { mutableStateOf("") }
    val title = remember { mutableStateOf("") }
    var titleError by remember { mutableStateOf<String>("") }
    var expenseAmountError by remember { mutableStateOf<String>("") }
    var calenderError by remember { mutableStateOf<String>("") }
    var fundError by remember { mutableStateOf<String>("") }
    var categoryError by remember { mutableStateOf<String>("") }
    var selectedDate by remember { mutableStateOf<Long?>(System.currentTimeMillis()) }
    var showDatePicker = remember { mutableStateOf(false) }
    val selectedCategory =
        remember { mutableStateOf(CategoryModel(categoryName = "Category")) }
    val selectedFunds = remember { mutableStateOf(FundModel(fundName = "Fund")) }
    val isLoading = remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogText by remember { mutableStateOf("") }
    var dialogIcon by remember { mutableStateOf(Icons.Default.CheckCircleOutline) }
    val scrollState = rememberScrollState()

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
                if((uiState as UIState.Success).tag == "EXPENSE"){
                    expenseAmount.value = ""
                    title.value = ""
                    selectedDate = System.currentTimeMillis()
                    selectedCategory.value = CategoryModel(categoryName = "Category")
                    selectedFunds.value = FundModel(fundName = "Fund")
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
        AppDialog(title = dialogTitle, text = dialogText, icon = dialogIcon, iconColor = if(dialogTitle == SUCCESSFUL) PrimaryColor else RedColor, confirm = "OK", showDialog = showDialog)
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

    Scaffold(topBar = { AppBar(navController, "Expense", route = AppRoutes.DASHBOARD.route) }) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = SizeL)
        ) {
            Column(modifier = Modifier.verticalScroll(scrollState)) {
                AppDisableTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = convertMillisToDate(
                        selectedDate ?: System.currentTimeMillis()
                    ),
                    placeholder = "Select Date",
                    leadingIcon = {
                        Icon(
                            Icons.Filled.CalendarToday,
                            tint = PrimaryColor,
                            contentDescription = "calender", modifier = Modifier.size(25.dp)
                        )
                    },
                    onClick = {
                        showDatePicker.value = true
                    },
                    isError = calenderError != "",
                    supportingText = calenderError.toString(),
                )
                AppDropdown(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Fund", items = funds.value, onItemSelected = {
                        selectedFunds.value = it
                        fundError = ""
                    }, selectedItem = selectedFunds.value, itemLabel = {
                        "${it.fundName?.replaceFirstChar { it.uppercase() }}${if (it.remainingAmount != null) (" | " + it.remainingAmount) else ""}"
                    },
                    supportingText = fundError,
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Wallet,
                            tint = PrimaryColor,
                            contentDescription = "calender", modifier = Modifier.size(25.dp)
                        )
                    }
                )
                AppDropdown(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Category", items = categories.value, onItemSelected = {
                        selectedCategory.value = it
                        categoryError = ""
                    }, selectedItem = selectedCategory.value, itemLabel = {
                        it.categoryName.toString().replaceFirstChar { it.uppercase() }
                    },
                    supportingText = categoryError.toString(),
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Category,
                            tint = PrimaryColor,
                            contentDescription = "calender", modifier = Modifier.size(25.dp)
                        )
                    }
                )
                AppTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = "Title",
                    onValueChange = { value ->
                        title.value = value
                        if (value.isNotEmpty()) {
                            titleError = ""
                        }
                    },
                    value = title.value,
                    leadingIcon = {
                        Icon(
                            Icons.Filled.NoteAlt,
                            tint = PrimaryColor,
                            contentDescription = "title", modifier = Modifier.size(25.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    isError = titleError != "",
                    supportingText = titleError.toString(),
                )
                AppTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = "Expense Amount",
                    onValueChange = { value ->
                        expenseAmount.value = value
                        if (value.isNotEmpty()) {
                            expenseAmountError = ""
                        }
                    },
                    value = expenseAmount.value,
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Money,
                            tint = PrimaryColor,
                            contentDescription = "amount", modifier = Modifier.size(25.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    isError = expenseAmountError != "",
                    supportingText = expenseAmountError.toString(),
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            AppButton(onClick = {
                if (selectedFunds.value.fundAmount == null) {
                    fundError = "Please select fund"
                }
                if (selectedCategory.value.id == null) {
                    categoryError = "Please select category"
                }
                if (title.value.isEmpty()) {
                    titleError = "Please enter title"
                }
                if (expenseAmount.value.isEmpty()) {
                    expenseAmountError = "Please enter  amount"
                } else if ((expenseAmount.value.toDoubleOrNull()?.toInt() ?: 0) < 1) {
                    expenseAmountError = "Invalid amount"
                }

                if (fundError.isEmpty() && categoryError.isEmpty() && titleError.isEmpty() && expenseAmountError.isEmpty()) {
                    viewModel.storeExpense(
                        date = Timestamp(selectedDate?.div(1000) ?: 0, 0),
                        amount = expenseAmount.value.toLong(),
                        title = title.value,
                        categoryId = selectedCategory.value.id.toString(),
                        fundId = selectedFunds.value.id.toString()
                    )
                }
            }, text = "Submit")

        }
    }
}