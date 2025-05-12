package com.my.pocketguard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.Category
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
import com.my.pocketguard.viewmodel.CategoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryBottomSheet(
    sheetState: SheetState,
    showBottomSheet: MutableState<Boolean>
) {
    val viewModel: CategoryViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val categories = viewModel.categories.collectAsState()
    val selectedCategoryId = remember { mutableStateOf("") }
    val categoryTitle = remember { mutableStateOf("") }
    val supportingText = remember { mutableStateOf("") }
    val hasError = remember { mutableStateOf(false) }
    val isLoading = remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
    val dialogTitle = remember { mutableStateOf("") }
    val dialogText = remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
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
                categoryTitle.value = ""
                selectedCategoryId.value = ""
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
            Row(modifier = Modifier.padding(vertical = SizeS), verticalAlignment = Alignment.CenterVertically) {
                HorizontalDivider(
                    modifier = Modifier
                        .width(30.dp)
                        .height(2.dp),
                    color = Color.Gray
                )
                Text(
                    "CATEGORIES",
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
                items(categories.value) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(SizeXS))
                            .background(
                                if (selectedCategoryId.value == it.id.toString())
                                    PrimaryColor
                                else PrimaryColorLite
                            )
                            .clickable {
                                if (selectedCategoryId.value != it.id.toString()) {
                                    selectedCategoryId.value = it.id.toString()
                                    categoryTitle.value =
                                        it.categoryName.toString().replaceFirstChar {
                                            it.uppercase()
                                        }
                                } else {
                                    selectedCategoryId.value = ""
                                    categoryTitle.value = ""
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier.padding(SizeXS),
                            text = it.categoryName.toString().replaceFirstChar {
                                it.uppercase()
                            },
                            style = appTextStyle.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (selectedCategoryId.value == it.id.toString()) WhiteColor else TextColor
                            )
                        )
                    }
                }
            }
            AppTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                label = "Category Name",
                onValueChange = { value ->
                    categoryTitle.value = value
                    if (value.isNotEmpty()) {
                        hasError.value = false
                        supportingText.value = ""
                    }
                },
                value = categoryTitle.value,
                leadingIcon = {
                    Icon(
                        Icons.Filled.Category,
                        tint = PrimaryColor,
                        contentDescription = "category", modifier = Modifier.size(25.dp)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                isError = hasError.value,
                supportingText = supportingText.value,
                visualTransformation = VisualTransformation.None
            )
            AppButton(onClick = {
                if (categoryTitle.value.isEmpty()) {
                    hasError.value = true
                    supportingText.value = "Please enter category name"
                } else {
                    if (selectedCategoryId.value == "") {
                        viewModel.storeCategory(categoryTitle.value)
                    } else {
                        viewModel.updateCategory(selectedCategoryId.value, categoryTitle.value)
                    }
                }
            }, text = if (selectedCategoryId.value != "") "Update" else "Submit")
        }
    }
}