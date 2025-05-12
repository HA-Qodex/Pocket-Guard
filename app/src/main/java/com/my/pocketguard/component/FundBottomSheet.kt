package com.my.pocketguard.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.my.pocketguard.ui.theme.PrimaryColor
import com.my.pocketguard.ui.theme.PrimaryColorLite
import com.my.pocketguard.ui.theme.Dimension.SizeL
import com.my.pocketguard.ui.theme.Dimension.LargeText
import com.my.pocketguard.ui.theme.Dimension.SizeM
import com.my.pocketguard.ui.theme.Dimension.SizeS
import com.my.pocketguard.ui.theme.Dimension.SizeXS
import com.my.pocketguard.ui.theme.TextColor
import com.my.pocketguard.ui.theme.WhiteColor
import com.my.pocketguard.ui.theme.appTextStyle
import com.my.pocketguard.util.UIState
import com.my.pocketguard.viewmodel.FundViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundBottomSheet(
    sheetState: SheetState,
    showBottomSheet: MutableState<Boolean>
) {
    val viewModel: FundViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val funds = viewModel.funds.collectAsState()
    val selectedFundId = remember { mutableStateOf("") }
    val fundTitle = remember { mutableStateOf("") }
    val fundAmount = remember { mutableStateOf("") }
    var fundTitleError by remember { mutableStateOf<String>("") }
    var fundAmountError by remember { mutableStateOf<String>("") }
    val isLoading = remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
    val dialogTitle = remember { mutableStateOf("") }
    val dialogText = remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        Log.d("CATEGORY_SHEET", "LaunchedEffect called")
        when (uiState) {
            is UIState.Error -> {
                isLoading.value = false
                showDialog.value = true
                dialogTitle.value = "Failed"
                dialogText.value = (uiState as UIState.Error).message
            }

            is UIState.Loading -> {
                isLoading.value = true
                showDialog.value = false
            }

            is UIState.Success -> {
                fundTitle.value = ""
                fundAmount.value = ""
                fundTitleError = ""
                fundAmountError = ""
                selectedFundId.value = ""
                isLoading.value = false
                showDialog.value = false
            }
        }
    }

    if (showDialog.value) {
        AppDialog(
            title = dialogTitle.value,
            text = dialogText.value,
            confirm = "OK",
            showDialog = showDialog
        )
    }

    if (isLoading.value) {
        CustomLoader()
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
                    "FUNDS",
                    modifier = Modifier.padding(horizontal = SizeXS),
                    style = appTextStyle.copy(fontWeight = FontWeight.Bold, fontSize = LargeText)
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
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = SizeM, top = SizeS)
            ) {
                items(funds.value) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(SizeXS))
                            .background(
                                if (selectedFundId.value == it.id.toString())
                                    PrimaryColor
                                else PrimaryColorLite
                            )
                            .clickable {
                                if (selectedFundId.value != it.id.toString()) {
                                    selectedFundId.value = it.id.toString()
                                    fundTitle.value =
                                        it.fundName.toString().replaceFirstChar {
                                            it.uppercase()
                                        }
                                    fundAmount.value = it.fundAmount.toString()
                                    fundTitleError = ""
                                    fundAmountError = ""
                                } else {
                                    selectedFundId.value = ""
                                    fundTitle.value = ""
                                    fundAmount.value = ""
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.padding(SizeM),
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = it.fundName.toString().replaceFirstChar {
                                    it.uppercase()
                                },
                                style = appTextStyle.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (selectedFundId.value == it.id.toString()) WhiteColor else TextColor
                                )
                            )
                            Spacer(modifier = Modifier.height(SizeXS))
                            Text(
                                text = "৳ ${NumberFormat.getInstance(Locale("en", "IN")).format(it.fundAmount)}",
                                style = appTextStyle.copy(
                                    fontWeight = FontWeight.Normal,
                                    color = if (selectedFundId.value == it.id.toString()) WhiteColor else TextColor
                                )
                            )
                        }
                    }
                }
            }
            AppTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                label = "Fund Name",
                onValueChange = { value ->
                    fundTitle.value = value
                    if (value.isNotEmpty()) {
                        fundTitleError = ""
                    }
                },
                value = fundTitle.value,
                leadingIcon = {
                    Icon(
                        Icons.Filled.Wallet,
                        tint = PrimaryColor,
                        contentDescription = "fund", modifier = Modifier.size(25.dp)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                isError = fundTitleError != "",
                supportingText = fundTitleError.toString(),
                visualTransformation = VisualTransformation.None
            )
            AppTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                label = "Amount",
                onValueChange = { value ->
                    fundAmount.value = value
                    if (value.isNotEmpty()) {
                        fundAmountError = ""
                    }
                },
                value = fundAmount.value,
                leadingIcon = {
                    Icon(
                        Icons.Filled.Money,
                        tint = PrimaryColor,
                        contentDescription = "fund", modifier = Modifier.size(25.dp)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                isError = fundAmountError != "",
                supportingText = fundAmountError.toString(),
                visualTransformation = VisualTransformation.None
            )
            AppButton(onClick = {
                if (fundTitle.value.isEmpty()) {
                    fundTitleError = "Please enter fund name"
                }
                if (fundAmount.value.isEmpty()) {
                    fundAmountError = "Please enter  amount"
                } else if ((fundAmount.value.toDoubleOrNull()?.toInt() ?: 0) < 1) {
                    fundAmountError = "Invalid amount"
                }

                if(fundTitleError.isEmpty() && fundAmountError.isEmpty()){
                    viewModel.storeFund(fundTitle.value, fundAmount.value.toDoubleOrNull()?.toInt() ?: 0)
                }
            }, text = if (selectedFundId.value != "") "Update" else "Submit")
        }
    }
}