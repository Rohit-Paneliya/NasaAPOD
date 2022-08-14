package com.nasa.apod.presentation.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasa.apod.domain.common.base.BaseResult
import com.nasa.apod.domain.media.entity.MediaEntity
import com.nasa.apod.domain.media.usecase.GetAllMediasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeMainViewModel @Inject constructor(private val getAllMediasUseCase: GetAllMediasUseCase) :
    ViewModel() {
    private val state = MutableStateFlow<HomeMainFragmentState>(HomeMainFragmentState.Init)
    val mState: StateFlow<HomeMainFragmentState> get() = state

    private val mediaList = MutableStateFlow<List<MediaEntity>>(mutableListOf())
    val mMediaList: StateFlow<List<MediaEntity>> get() = mediaList

    private fun setLoading(isLoading: Boolean) {
        state.value = HomeMainFragmentState.IsLoading(isLoading)
    }

    private fun showToast(message: String) {
        state.value = HomeMainFragmentState.ShowToast(message)
    }

    private fun showError(message: String) {
        state.value = HomeMainFragmentState.ShowError(message)
    }

    fun fetchAllMedias() {
        viewModelScope.launch {
            getAllMediasUseCase.invoke()
                .onStart {
                    setLoading(true)
                }
                .catch { exception ->
                    setLoading(false)
                    showToast(exception.message.toString())
                }
                .collect { result ->
                    setLoading(false)
                    when (result) {
                        is BaseResult.Success -> {
                            mediaList.value = result.data
                        }
                        is BaseResult.Error -> {
                            showError(result.rawResponse.message)
                        }
                    }
                }
        }
    }

}

sealed class HomeMainFragmentState {
    object Init : HomeMainFragmentState()
    data class IsLoading(val isLoading: Boolean) : HomeMainFragmentState()
    data class ShowToast(val message: String) : HomeMainFragmentState()
    data class ShowError(val message: String) : HomeMainFragmentState()
}