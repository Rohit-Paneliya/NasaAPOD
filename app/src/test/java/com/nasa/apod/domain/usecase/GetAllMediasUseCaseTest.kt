package com.nasa.apod.domain.usecase

import com.nasa.apod.FakeDataGenerator.errorCode
import com.nasa.apod.FakeDataGenerator.errorMessage
import com.nasa.apod.FakeDataGenerator.getDummyData
import com.nasa.apod.FakeDataGenerator.getErrorDummyData
import com.nasa.apod.FakeDataGenerator.mediaEntityList
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

    @Test
    fun checkSuccessInvokeMethod() = runBlocking {
        val list = MutableStateFlow<List<MediaEntity>>(mutableListOf())
        `when`(mediaRepository.getAllMediasList()).thenReturn(getDummyData())
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
        `when`(mediaRepository.getAllMediasList()).thenReturn(getErrorDummyData())
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
}