package com.my.pocketguard.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.my.pocketguard.model.CategoryModel
import com.my.pocketguard.model.Expense
import com.my.pocketguard.model.FundModel
import com.my.pocketguard.ui.theme.Dimension.SizeL
import com.my.pocketguard.ui.theme.Dimension.SizeS
import com.my.pocketguard.ui.theme.Dimension.SizeXS
import com.my.pocketguard.ui.theme.Dimension.TextL
import com.my.pocketguard.ui.theme.PrimaryColor
import com.my.pocketguard.ui.theme.RedColor
import com.my.pocketguard.ui.theme.appTextStyle
import com.my.pocketguard.util.AppUtils.SUCCESSFUL
import com.my.pocketguard.util.AppUtils.convertMillisToDate
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseBottomSheet(
    sheetState: SheetState,
    showBottomSheet: MutableState<Boolean>,
    categories: List<CategoryModel>,
    funds: List<FundModel>,
    expenses: MutableState<List<Expense>>
) {
    val scrollState = rememberScrollState()
    val expenseAmount = remember { mutableStateOf("") }
    val title = remember { mutableStateOf("") }
    var titleError by remember { mutableStateOf<String>("") }
    var expenseAmountError by remember { mutableStateOf<String>("") }
    var calenderError by remember { mutableStateOf<String>("") }
    var fundError by remember { mutableStateOf<String>("") }
    var categoryError by remember { mutableStateOf<String>("") }
    var remainingAmount by remember { mutableStateOf<String>("0") }
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

    ModalBottomSheet(
        onDismissRequest = { showBottomSheet.value = false },
        sheetState = sheetState,
        containerColor = Color.White,
        dragHandle = {
            Row(
                modifier = Modifier.padding(vertical = SizeS),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier
                        .width(30.dp)
                        .height(2.dp),
                    color = Color.Gray
                )
                Text(
                    "EXPENSE",
                    modifier = Modifier.padding(horizontal = SizeXS),
                    style = appTextStyle.copy(fontWeight = FontWeight.Bold, fontSize = TextL)
                )
                HorizontalDivider(
                    modifier = Modifier
                        .width(30.dp)
                        .height(2.dp),
                    color = Color.Gray
                )

            }
        }
    ) {
        Column(
            modifier = Modifier
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
                Row {
                    AppDropdown(
                        modifier = Modifier.weight(3f),
                        title = "Fund", items = funds, onItemSelected = {
                            selectedFunds.value = it
                            fundError = ""
                            remainingAmount = it.remainingAmount.toString()
                        }, selectedItem = selectedFunds.value, itemLabel = {
                            "${it.fundName?.replaceFirstChar { it.uppercase() }}"
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
                    Spacer(modifier = Modifier.width(SizeS))
                    AppDisableTextField(
                        modifier = Modifier.weight(2f),
                        value = remainingAmount,
                        placeholder = "Remaining",
                        onClick = {},
                        leadingIcon = {
                            Icon(
                                Icons.Filled.Money,
                                tint = PrimaryColor,
                                contentDescription = "amount", modifier = Modifier.size(25.dp)
                            )
                        }
                    )
                }
                AppDropdown(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Category", items = categories, onItemSelected = {
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
            AppButton(onClick = {
                if (selectedFunds.value.fundAmount == null) {
                    fundError = "Please select fund"
                    remainingAmount = "0"
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
                    expenses.value = expenses.value +
                            Expense(
                                id = UUID.randomUUID().toString(),
                                category = selectedCategory.value,
                                fund = selectedFunds.value,
                                date = Timestamp(selectedDate?.div(1000) ?: 0, 0),
                                amount = expenseAmount.value.toLong(),
                                title = title.value
                            )

                    expenseAmount.value = ""
                    title.value = ""
                    selectedDate = System.currentTimeMillis()
                    selectedCategory.value = CategoryModel(categoryName = "Category")
                    selectedFunds.value = FundModel(fundName = "Fund")
                    remainingAmount = "0"
                }
            }, text = "Add to list")
        }
    }
}