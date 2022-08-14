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

    init {
        fetchAllMedias()
    }

    private fun setLoading() {
        state.value = HomeMainFragmentState.IsLoading(true)
    }

    private fun hideLoading() {
        state.value = HomeMainFragmentState.IsLoading(false)
    }

    private fun showToast(message: String) {
        state.value = HomeMainFragmentState.ShowToast(message)
    }

    fun fetchAllMedias() {
        viewModelScope.launch {
            getAllMediasUseCase.invoke()
                .onStart {
                    setLoading()
                }
                .catch { exception ->
                    hideLoading()
                    showToast(exception.message.toString())
                }
                .collect { result ->
                    hideLoading()
                    when (result) {
                        is BaseResult.Success -> {
                            mediaList.value = result.data
                        }
                        is BaseResult.Error -> {
                            showToast(result.rawResponse.message)
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
}