package com.dpycb.protracker

import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun formatDateToString(time: Long): String {
        val date = Date(time)
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return formatter.format(date)
    }
}