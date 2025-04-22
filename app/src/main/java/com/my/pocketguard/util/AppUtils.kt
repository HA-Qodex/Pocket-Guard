package com.my.pocketguard.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

object AppUtils {
    @SuppressLint("SimpleDateFormat")
    fun convertMillisToDate(millis: Long): String {
        val formatter = SimpleDateFormat("dd MMM, yyyy")
        return formatter.format(Date(millis))
    }
}