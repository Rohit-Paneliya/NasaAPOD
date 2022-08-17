package com.nasa.apod.data.utils

import com.nasa.apod.data.media.remote.dto.MediasList
import com.nasa.apod.data.media.remote.dto.MediasListItem
import java.text.SimpleDateFormat
import java.util.*

class MediaUtils {

    fun sortedMediaByLatestDate(data: MediasList): ArrayList<MediasListItem> {
        val dateTimeFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        data.sortByDescending {
            if (it.date.isNullOrBlank()) {
                Date()
            } else {
                dateTimeFormatter.parse(it.date)
            }
        }
        return data
    }

}