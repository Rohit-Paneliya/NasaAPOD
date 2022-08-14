package com.nasa.apod.presentation.utils

import android.text.TextUtils
import java.text.SimpleDateFormat
import java.util.*

class DateTimeFormatter {

    private val systemDateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val displayDateFormatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

    fun displayDateFromSystemDate(date: String?): String {

        if(TextUtils.isEmpty(date)) return "NA"

        val systemDate = date?.let { systemDateFormatter.parse(it) }
        return systemDate?.let { displayDateFormatter.format(it) } ?: kotlin.run { "NA" }
    }
}