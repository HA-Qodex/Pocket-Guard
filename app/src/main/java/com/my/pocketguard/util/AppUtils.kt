package com.my.pocketguard.util

import android.annotation.SuppressLint
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

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

    fun convertTimestampToLocal(timestamp: Timestamp?): String {
        if (timestamp == null) return "Invalid Date"
        val sdf = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault() // local time zone
        return sdf.format(timestamp.toDate())
    }

    const val SUCCESSFUL = "Successful"
    const val FAILED = "Failed"
}

sealed class UIState {
    object Loading : UIState()
    data class Success(val tag: String = "DEFAULT") : UIState()
    data class Error(val message: String) : UIState()
}