package com.nasa.apod.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nasa.apod.FakeDataGenerator
import com.nasa.apod.FakeDataGenerator.getDummyData
import com.nasa.apod.FakeDataGenerator.getErrorDummyData
import com.nasa.apod.data.media.remote.dto.MediasList
import com.nasa.apod.data.utils.WrappedListResponse
import com.nasa.apod.domain.base.BaseResult
import com.nasa.apod.domain.media.MediaRepository
import com.nasa.apod.domain.media.entity.MediaEntity
import com.nasa.apod.domain.media.usecase.GetAllMediasUseCase
import com.nasa.apod.presentation.main.home.HomeMainViewModel
import com.nasa.apod.utils.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeMainViewModelTest{

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val mediaRepository = Mockito.mock(MediaRepository::class.java)
    private val getAllMediasUseCase = GetAllMediasUseCase(mediaRepository)
    private val homeMainViewModel = HomeMainViewModel(getAllMediasUseCase)

    @Test
    fun `test success response from JSON`() = runBlocking {
        val list = MutableStateFlow<List<MediaEntity>>(mutableListOf())
        val result: Flow<BaseResult<List<MediaEntity>, WrappedListResponse<MediasList>>> = getDummyData()
        Mockito.`when`(getAllMediasUseCase.invoke()).thenReturn(result)
        result.collect { output ->
            when (output) {
                is BaseResult.Success -> {
                    list.value = output.data
                }
            }
        }
        homeMainViewModel.fetchAllMedias()
        Assert.assertEquals(FakeDataGenerator.mediaEntityList[0].title,list.value[0].title)
        Assert.assertEquals(FakeDataGenerator.mediaEntityList[1].title,list.value[1].title)
    }

    @Test
    fun `test Error response from JSON`() = runBlocking {
        var receivedErrorCode = 0
        var receivedErrorMessage = ""
        val result: Flow<BaseResult<List<MediaEntity>, WrappedListResponse<MediasList>>> = getErrorDummyData()
        Mockito.`when`(getAllMediasUseCase.invoke()).thenReturn(result)
        result.collect { output ->
            when (output) {
                is BaseResult.Error -> {
                    receivedErrorCode = output.rawResponse.code
                    receivedErrorMessage = output.rawResponse.message
                }
            }
        }
        homeMainViewModel.fetchAllMedias()
        Assert.assertEquals(receivedErrorCode, FakeDataGenerator.errorCode)
        Assert.assertEquals(receivedErrorMessage, FakeDataGenerator.errorMessage)
    }
}