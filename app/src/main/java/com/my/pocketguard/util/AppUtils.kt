package com.my.pocketguard.util

import android.annotation.SuppressLint
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object AppUtils {

    fun getCurrentMonthStartAndEnd(): Pair<Long, Long> {
        val calendar = Calendar.getInstance()

        // Start of current month
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfMonth = calendar.timeInMillis

        // Start of next month
        calendar.add(Calendar.MONTH, 1)
        val startOfNextMonth = calendar.timeInMillis

        return Pair(startOfMonth, startOfNextMonth)
    }

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