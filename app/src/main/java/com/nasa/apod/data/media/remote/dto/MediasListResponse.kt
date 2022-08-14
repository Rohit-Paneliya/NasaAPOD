package com.nasa.apod.data.media.remote.dto


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

class MediasList : ArrayList<MediasListItem>()

@Parcelize
data class MediasListItem(
    val copyright: String? = "",
    val date: String? = "",
    val explanation: String? = "",
    val hdurl: String? = "",
    val media_type: String? = "",
    val service_version: String? = "",
    val title: String? = "",
    val url: String? = ""
) : Parcelable
