package com.nasa.apod.data.di

import android.content.Context
import com.nasa.apod.data.media.remote.api.MediaApi
import com.nasa.apod.data.media.repository.MediaRepositoryImpl
import com.nasa.apod.domain.media.MediaRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class MediaModule {
    @Singleton
    @Provides
    fun provideMediaApi(retrofit: Retrofit) : MediaApi {
        return retrofit.create(MediaApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMediaRepository(mediaApi: MediaApi, @ApplicationContext context: Context) : MediaRepository {
        return MediaRepositoryImpl(mediaApi,context)
    }
}