package com.my.pocketguard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.my.pocketguard.ui.theme.ButtonColor
import com.my.pocketguard.ui.theme.DialogColor
import com.my.pocketguard.ui.theme.TextColor
import com.my.pocketguard.ui.theme.appTextStyle
import com.my.pocketguard.util.AppUtils.convertMillisToDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppCalender(
    selectedDate: Long?,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val calenderState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate,
        yearRange = ZonedDateTime.now().minusYears(10).year..ZonedDateTime.now().year,
        initialDisplayMode = DisplayMode.Picker,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val date = ZonedDateTime.ofInstant(
                    Date(utcTimeMillis).toInstant(),
                    ZoneId.systemDefault()
                )
                return date in ZonedDateTime.now().minusYears(10)..ZonedDateTime.now()
            }
        },
    )

        Dialog(
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false,
            ),
            onDismissRequest = onDismiss
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .background(DialogColor, shape = RoundedCornerShape(8.dp))
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DatePicker(
                    state = calenderState,
                    showModeToggle = false,
                    title = {
                        Text(
                            "Select a date",
                            modifier = Modifier.padding(start = 15.dp, top = 15.dp)
                        )
                    },
                    headline = {
                        Text(
                            convertMillisToDate(
                                calenderState.selectedDateMillis ?: System.currentTimeMillis()
                            ), modifier = Modifier.padding(start = 15.dp)
                        )
                    },
                    colors = DatePickerDefaults.colors(
                        titleContentColor = TextColor,
                        containerColor = DialogColor,
                        dayContentColor = TextColor,
                        headlineContentColor = TextColor,
                        selectedDayContainerColor = ButtonColor,
                        dividerColor = ButtonColor,
                        weekdayContentColor = TextColor,
                        navigationContentColor = ButtonColor,
                        yearContentColor = TextColor,
                        selectedYearContainerColor = ButtonColor,
                        currentYearContentColor = ButtonColor,
                        selectedYearContentColor = TextColor,
                        todayContentColor = ButtonColor,
                        todayDateBorderColor = ButtonColor
                    )
                )
                Row(modifier = Modifier.fillMaxWidth().height(60.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss
                    ) {
                        Text(
                            text = "CANCEL",
                            style = appTextStyle.copy(
                                color = Color.LightGray,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W200
                            ),
                        )
                    }
                    TextButton(onClick = {onDateSelected(calenderState.selectedDateMillis)}) {
                        Text(
                            text = "OK",
                            style = appTextStyle.copy(
                                color = ButtonColor,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W600
                            ),
                        )
                    }
                }
            }
        }
}