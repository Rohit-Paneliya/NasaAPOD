package com.nasa.apod.presentation.utils

import androidx.appcompat.app.AppCompatDelegate

enum class SetTheme{
    LIGHT,DARK
}

class ThemeHelper {
    fun applyTheme(theme: SetTheme) {
        when (theme) {
            SetTheme.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            SetTheme.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}