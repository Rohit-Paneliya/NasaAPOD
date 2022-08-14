package com.nasa.apod.presentation.interfaces

interface OnItemClickListener<T> {
    fun onListItemClicked(item: T)
}