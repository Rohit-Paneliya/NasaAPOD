package com.nasa.apod.presentation.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateTimeFormatter {

    private val systemDateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val displayDateFormatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

    fun displayDateFromSystemDate(date: String?): String {

        return when {
            date.isNullOrBlank() -> "NA"
            else -> {
                try{
                    val systemDate = date.let { systemDateFormatter.parse(it) }
                    systemDate?.let { displayDateFormatter.format(it) } ?: kotlin.run { "NA" }
                } catch (exception: ParseException){
                    return "NA"
                }
            }
        }

    }
}