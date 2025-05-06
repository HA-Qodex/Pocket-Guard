package com.my.pocketguard.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.my.pocketguard.component.AppBar
import com.my.pocketguard.component.AppButton
import com.my.pocketguard.component.AppTextField
import com.my.pocketguard.component.CustomLoader
import com.my.pocketguard.model.CategoryModel
import com.my.pocketguard.navigation.AppRoutes
import com.my.pocketguard.ui.theme.BackgroundColor
import com.my.pocketguard.ui.theme.BackgroundColorLite
import com.my.pocketguard.ui.theme.ButtonColor
import com.my.pocketguard.ui.theme.Dimension.LargePadding
import com.my.pocketguard.ui.theme.Dimension.SmallPadding
import com.my.pocketguard.ui.theme.Dimension.SmallSpacing
import com.my.pocketguard.ui.theme.TextColor
import com.my.pocketguard.ui.theme.WhiteColor
import com.my.pocketguard.ui.theme.appTextStyle
import com.my.pocketguard.util.UIState
import com.my.pocketguard.viewmodel.CategoryViewModel
import com.my.pocketguard.viewmodel.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryView(navController: NavController) {
    val viewModel: CategoryViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val categories = viewModel.categories.collectAsState()
    val selectedCategoryId = remember { mutableStateOf("") }
    val categoryTitle = remember { mutableStateOf("") }
    val supportingText = remember { mutableStateOf("") }
    val hasError = remember { mutableStateOf(false) }
    val isLoading = remember { mutableStateOf(true) }

    when (uiState) {
        is UIState.Error -> {
            isLoading.value = false
        }

        is UIState.Loading -> {
            isLoading.value = true
        }

        is UIState.Success -> {
            categoryTitle.value = ""
            selectedCategoryId.value = ""
            isLoading.value = false
        }
    }

    if (isLoading.value){
        CustomLoader()
    }

    Scaffold(topBar = { AppBar(navController, "Category", route = AppRoutes.DASHBOARD.route) }) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = LargePadding)
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = SmallPadding)
            ) {
                items(categories.value) {
                    Box(
                        modifier = Modifier.clip(RoundedCornerShape(SmallSpacing)).background(
                            if(selectedCategoryId.value == it.id.toString())
                                BackgroundColor
                                else BackgroundColorLite).clickable{
                            if(selectedCategoryId.value != it.id.toString()){
                            selectedCategoryId.value = it.id.toString()
                            categoryTitle.value = it.categoryName.toString().replaceFirstChar {
                                it.uppercase()
                            }
                            } else {
                                selectedCategoryId.value = ""
                                categoryTitle.value = ""
                            }
                        },
                        contentAlignment = Alignment.Center) {
                        Text(
                            modifier = Modifier.padding(SmallSpacing),
                            text = it.categoryName.toString().replaceFirstChar {
                                it.uppercase()
                            },
                            style = appTextStyle.copy(fontWeight = FontWeight.Bold, color =  if(selectedCategoryId.value == it.id.toString()) WhiteColor else TextColor)
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
                        tint = BackgroundColor,
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
                }else {
                    if (selectedCategoryId.value == "") {
                        viewModel.storeCategory(categoryTitle.value)
                    } else {
                        viewModel.updateCategory(selectedCategoryId.value, categoryTitle.value)
                    }
                }
            }, text = if(selectedCategoryId.value != "") "Update" else "Submit")
        }
    }
}