package com.nasa.apod.presentation.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasa.apod.domain.base.BaseResult
import com.nasa.apod.domain.media.entity.MediaEntity
import com.nasa.apod.domain.media.usecase.GetAllMediasUseCase
import com.nasa.apod.presentation.utils.UiStateHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeMainViewModel @Inject constructor(private val getAllMediasUseCase: GetAllMediasUseCase) :
    ViewModel() {

    private val handleResponse = MutableStateFlow<UiStateHandler<List<MediaEntity>>>(UiStateHandler.Init())
    val mHandleResponse: StateFlow<UiStateHandler<List<MediaEntity>>> get() = handleResponse

    fun fetchAllMedias() {
        viewModelScope.launch {
            getAllMediasUseCase.invoke()
                .onStart {
                    handleResponse.value = UiStateHandler.Loading(true)
                }
                .onCompletion {
                    handleResponse.value = UiStateHandler.Loading(false)
                }
                .catch { exception ->
                    handleResponse.value = UiStateHandler.Loading(false)
                    handleResponse.value = UiStateHandler.ShowMessage(exception.message.toString())
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> {
                            handleResponse.value = UiStateHandler.Success(result.data)
                        }
                        is BaseResult.Error -> {
                            handleResponse.value = UiStateHandler.Error(
                                result.rawResponse.code,
                                result.rawResponse.message
                            )
                        }
                    }
                }
        }
    }
}