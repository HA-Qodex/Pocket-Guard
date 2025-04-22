package com.my.pocketguard.view

import androidx.compose.foundation.background
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
import com.my.pocketguard.component.AppBar
import com.my.pocketguard.component.AppCalender
import com.my.pocketguard.component.DatePickerModal
import com.my.pocketguard.component.FundBottomSheet
import com.my.pocketguard.ui.theme.BackgroundColor
import com.my.pocketguard.ui.theme.BackgroundColorLite
import com.my.pocketguard.viewmodel.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardView(navController: NavController) {
    val context = LocalContext.current
    val viewModel: DashboardViewModel = hiltViewModel()
    val currentUser = viewModel.currentUser.collectAsState()
    val fundSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showFundSheet = remember { mutableStateOf(false) }

    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var showDatePicker = remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
    }

    if(showFundSheet.value){
        FundBottomSheet(showBottomSheet = showFundSheet, sheetState = fundSheetState)
    }
    if(showDatePicker.value){
        AppCalender(
            onDateSelected = {selectedDate = it},
            onDismiss = {
                showDatePicker.value = false
            }
        )
    }

    Scaffold(
        containerColor = BackgroundColorLite,
        topBar = {
            AppBar(
                context = context,
                imageUrl = currentUser.value?.photoUrl.toString(),
                name = currentUser.value?.displayName.toString(),
                fundClick = {showFundSheet.value = true},
                addClick = {showDatePicker.value = true},
                categoryClick = {}
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ){
            Column {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .background(BackgroundColor)) {
//                    Row(
//                        modifier = Modifier.padding(horizontal = 15.dp),
//                        horizontalArrangement = Arrangement.SpaceAround) {
//                        Button(
//                            shape = CircleShape,
//                            colors = ButtonDefaults.buttonColors()
//                                .copy(containerColor = Color.White),
//                            onClick = {}
//                        ) {
//                            Icon(Icons.Filled.Wallet, contentDescription = "fund")
//                        }
//                    }
                }
            }
        }
    }
}