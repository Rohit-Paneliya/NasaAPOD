package com.nasa.apod.data.utils

import com.nasa.apod.data.media.remote.dto.MediasList
import com.nasa.apod.data.media.remote.dto.MediasListItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MediaUtilsTest {

    private val mediaUtils = MediaUtils()

    private val mediaList: MediasList = MediasList()
    private val wrongFormatDateMediaList: MediasList = MediasList()

    @Before
    fun setUp() {
        mediaList.add(MediasListItem(date = "2019-12-10"))
        mediaList.add(MediasListItem(date = "2019-12-30"))
        mediaList.add(MediasListItem(date = "2019-12-20"))

        wrongFormatDateMediaList.add(MediasListItem(date = "10-12-2021"))
        wrongFormatDateMediaList.add(MediasListItem(date = "30-12-2021"))
        wrongFormatDateMediaList.add(MediasListItem(date = "20-12-2021"))
        wrongFormatDateMediaList.add(MediasListItem(date = ""))
    }

    @Test
    fun testSortedMediaByLatestDate() {
        val sortedList = mediaUtils.sortedMediaByLatestDate(mediaList)
        Assert.assertEquals(sortedList[0].date, "2019-12-30")
    }

    @Test
    fun testEmptyDateMediaItem() {
        val sortedList = mediaUtils.sortedMediaByLatestDate(wrongFormatDateMediaList)
        Assert.assertEquals(sortedList[0].date, "")
    }
}