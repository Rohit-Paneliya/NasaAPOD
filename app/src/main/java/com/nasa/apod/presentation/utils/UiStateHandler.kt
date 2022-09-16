package com.nasa.apod.presentation.utils

sealed class UiStateHandler<T : Any> {
    class Init<T : Any> : UiStateHandler<T>()
    data class Loading<T : Any>(val isLoading: Boolean) : UiStateHandler<T>()
    data class Success<T : Any>(val data: T) : UiStateHandler<T>()
    data class Error<T : Any>(val code: Int, val message: String?) : UiStateHandler<T>()
    data class ShowMessage<T : Any>(val message: String) : UiStateHandler<T>()
}