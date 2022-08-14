package com.nasa.apod.data.media.remote.api

import com.nasa.apod.data.media.remote.dto.MediasList
import com.nasa.apod.data.utils.WrappedListResponse
import retrofit2.Response
import retrofit2.http.GET

interface MediaApi {
    @GET("medias/")
    suspend fun getAllMedias() : Response<WrappedListResponse<MediasList>>
}