package com.nasa.apod

import com.nasa.apod.data.media.remote.dto.MediasList
import com.nasa.apod.data.utils.WrappedListResponse
import com.nasa.apod.domain.base.BaseResult
import com.nasa.apod.domain.media.entity.MediaEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object FakeDataGenerator {

    val errorCode = 422
    val errorMessage = "JSON Parsing issue"

    val mediaEntityList = mutableListOf(
        MediaEntity(title = "Test"),
        MediaEntity(title = "Test2")
    )

    suspend fun getDummyData(): Flow<BaseResult<List<MediaEntity>, WrappedListResponse<MediasList>>> {
        return flow {
            emit(BaseResult.Success(mediaEntityList))
        }
    }

    suspend fun getErrorDummyData(): Flow<BaseResult<List<MediaEntity>, WrappedListResponse<MediasList>>> {
        return flow {
            val error: WrappedListResponse<MediasList> = WrappedListResponse(
                code = errorCode, message = errorMessage,status = false,
                errors = arrayListOf(),
                data = arrayListOf()
            )
            emit(BaseResult.Error(error))
        }
    }
}