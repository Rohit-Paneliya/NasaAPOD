package com.nasa.apod.domain.media

import com.nasa.apod.data.media.remote.dto.MediasList
import com.nasa.apod.data.utils.WrappedListResponse
import com.nasa.apod.domain.base.BaseResult
import com.nasa.apod.domain.media.entity.MediaEntity
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    suspend fun getAllMediasList() : Flow<BaseResult<List<MediaEntity>, WrappedListResponse<MediasList>>>
}