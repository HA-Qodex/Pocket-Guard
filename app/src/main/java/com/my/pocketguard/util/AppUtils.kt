package com.my.pocketguard.util

import android.annotation.SuppressLint
import androidx.navigation.NavController
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
}

sealed class UIState{
    object Loading: UIState()
    object Success: UIState()
    data class Error(val message: String): UIState()
}