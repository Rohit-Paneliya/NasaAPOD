package com.nasa.apod.presentation.viewmodel.utils

import com.nasa.apod.presentation.utils.DateTimeFormatter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class DateTimeFormatterTest {

    private val dateTimeFormatter = DateTimeFormatter()

    @Test
    fun testSuccessDisplayDateFromSystemDate() {
        val sourceDate = "2022-08-15"
        val expectedDate = "15 August 2022"
        val result = dateTimeFormatter.displayDateFromSystemDate(sourceDate)
        Assert.assertEquals(result,expectedDate)
    }

    @Test
    fun checkEmptyStringDate() {
        val sourceDate = ""
        val expectedDate = "NA"
        val result = dateTimeFormatter.displayDateFromSystemDate(sourceDate)
        Assert.assertEquals(result,expectedDate)
    }

    @Test
    fun checkNullDate() {
        val sourceDate:String? = null
        val expectedDate = "NA"
        val result = dateTimeFormatter.displayDateFromSystemDate(sourceDate)
        Assert.assertEquals(result,expectedDate)
    }

    @Test
    fun checkWrongDateFormat() {
        val sourceDate = "15 Jan 2022"
        val expectedDate = "NA"
        val result = dateTimeFormatter.displayDateFromSystemDate(sourceDate)
        Assert.assertEquals(result,expectedDate)
    }
}