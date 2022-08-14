package com.nasa.apod.domain.media.usecase

import com.nasa.apod.data.media.remote.dto.MediasList
import com.nasa.apod.data.utils.WrappedListResponse
import com.nasa.apod.domain.common.base.BaseResult
import com.nasa.apod.domain.media.MediaRepository
import com.nasa.apod.domain.media.entity.MediaEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllMediasUseCase @Inject constructor(private val mediaRepository: MediaRepository) {
    suspend fun invoke() : Flow<BaseResult<List<MediaEntity>, WrappedListResponse<MediasList>>> {
        return mediaRepository.getAllMediasList()
    }
}