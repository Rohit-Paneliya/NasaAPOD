package com.nasa.apod.presentation.utils

import android.text.TextUtils
import java.text.SimpleDateFormat
import java.util.*

class DateTimeFormatter {

    private val systemDateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val displayDateFormatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

    fun displayDateFromSystemDate(date: String?): String {

        return when {
            TextUtils.isEmpty(date) -> "NA"
            else -> {
                val systemDate = date?.let { systemDateFormatter.parse(it) }
                systemDate?.let { displayDateFormatter.format(it) } ?: kotlin.run { "NA" }
            }
        }

    }
}