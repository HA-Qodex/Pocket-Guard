package com.my.pocketguard.util

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.my.pocketguard.ui.theme.Dimension.SizeXS
import com.my.pocketguard.ui.theme.Dimension.SmallText
import com.my.pocketguard.ui.theme.RedColor
import com.my.pocketguard.ui.theme.appTextStyle
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

object AppUtils {

    private val calendar: Calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
    val currentMonth = calendar.get(Calendar.MONTH)

    @SuppressLint("SimpleDateFormat")
    fun convertMillisToDate(millis: Long): String {
        val formatter = SimpleDateFormat("dd MMM, yyyy")
        return formatter.format(Date(millis))
    }

    const val SUCCESSFUL = "Successful"
    const val FAILED = "Failed"
}

sealed class UIState{
    object Loading: UIState()
    data class Success(val tag: String = "DEFAULT"): UIState()
    data class Error(val message: String): UIState()
}