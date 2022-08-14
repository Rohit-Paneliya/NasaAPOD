package com.nasa.apod.domain.media.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaEntity(
    val copyright: String? = "",
    val date: String? = "",
    val explanation: String? = "",
    val hdurl: String? = "",
    val title: String? = "",
    val url: String? = ""
) : Parcelable