package com.nasa.apod.data.utils

import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor constructor(private val pref: SharedPrefs) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
//        val token = pref.getToken()
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiNDEyIiwiZXhwIjoxNjkyMDAyNzk2LCJpYXQiOjE2NjA0NjY3OTYsImlzcyI6ImFkbWluIn0.3vEwY-hi1h1GDk3xdTTbuXNWMmOaCYPovoIE8Ov2f-o"
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", token)
            .build()
        return chain.proceed(newRequest)
    }
}