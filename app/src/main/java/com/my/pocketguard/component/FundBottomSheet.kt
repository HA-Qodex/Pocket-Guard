package com.my.pocketguard.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundBottomSheet(
    sheetState: SheetState,
    showBottomSheet: MutableState<Boolean>
) {
//    val selectedDate = remember { mutableStateOf("") }
//
//    val datePicker = remember {
//        DatePicker.Builder.datePicker()
//            .setTitleText("Select Date")
//            .build()
//    }
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet.value = false },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            Text(
                text = "Hello from Bottom Sheet!",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
}