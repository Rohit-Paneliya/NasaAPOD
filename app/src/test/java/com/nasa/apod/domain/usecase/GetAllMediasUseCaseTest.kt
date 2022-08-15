package com.nasa.apod.domain.usecase

import com.nasa.apod.data.media.remote.dto.MediasList
import com.nasa.apod.data.utils.WrappedListResponse
import com.nasa.apod.domain.base.BaseResult
import com.nasa.apod.domain.media.MediaRepository
import com.nasa.apod.domain.media.entity.MediaEntity
import com.nasa.apod.domain.media.usecase.GetAllMediasUseCase
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class GetAllMediasUseCaseTest : TestCase() {

    private val mediaRepository = mock(MediaRepository::class.java)
    private val getAllMediasUseCase = GetAllMediasUseCase(mediaRepository)
    private val mediaEntityList = mutableListOf(
        MediaEntity(title = "Test"),
        MediaEntity(title = "Test2")
    )

    private val errorCode = 422
    private val errorMessage = "JSON Parsing issue"

    @Test
    fun checkSuccessInvokeMethod() = runBlocking {
        val list = MutableStateFlow<List<MediaEntity>>(mutableListOf())
        `when`(getAllMediasUseCase.invoke()).thenReturn(getDummyData())
        val result: Flow<BaseResult<List<MediaEntity>, WrappedListResponse<MediasList>>> = getAllMediasUseCase.invoke()
        result.collect { output ->
                when (output) {
                    is BaseResult.Success -> {
                        list.value = output.data
                    }
                }
            }
        Assert.assertEquals(mediaEntityList[0].title,list.value[0].title)
        Assert.assertEquals(mediaEntityList[1].title,list.value[1].title)
    }

    @Test
    fun checkErrorInvokeMethod() = runBlocking {

        var receivedErrorCode = 0
        var receivedErrorMessage = ""
        `when`(getAllMediasUseCase.invoke()).thenReturn(getErrorDummyData())
        val result: Flow<BaseResult<List<MediaEntity>, WrappedListResponse<MediasList>>> = getAllMediasUseCase.invoke()
        result.collect { output ->
            when (output) {
                is BaseResult.Error -> {
                    receivedErrorCode = output.rawResponse.code
                    receivedErrorMessage = output.rawResponse.message
                }
            }
        }
        Assert.assertEquals(receivedErrorCode,errorCode)
        Assert.assertEquals(receivedErrorMessage,errorMessage)
    }

    private suspend fun getDummyData(): Flow<BaseResult<List<MediaEntity>, WrappedListResponse<MediasList>>> {
        return flow {
            emit(BaseResult.Success(mediaEntityList))
        }
    }

    private suspend fun getErrorDummyData(): Flow<BaseResult<List<MediaEntity>, WrappedListResponse<MediasList>>> {
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