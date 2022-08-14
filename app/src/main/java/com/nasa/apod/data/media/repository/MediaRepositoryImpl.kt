package com.nasa.apod.data.media.repository

import android.content.Context
import com.google.gson.Gson
import com.nasa.apod.R
import com.nasa.apod.data.media.remote.api.MediaApi
import com.nasa.apod.data.media.remote.dto.MediasList
import com.nasa.apod.data.utils.JsonParserHelper
import com.nasa.apod.data.utils.MediaUtils
import com.nasa.apod.data.utils.WrappedListResponse
import com.nasa.apod.domain.common.base.BaseResult
import com.nasa.apod.domain.media.MediaRepository
import com.nasa.apod.domain.media.entity.MediaEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    private val mediaApi: MediaApi,
    private val applicationContext: Context
) : MediaRepository {
    override suspend fun getAllMediasList(): Flow<BaseResult<List<MediaEntity>, WrappedListResponse<MediasList>>> {
        return flow {

            val parsedJson = JsonParserHelper().inputStreamToString(
                applicationContext.resources.openRawResource(R.raw.list_data)
            )

            if (parsedJson.isNullOrEmpty().not()) {
                val data = Gson().fromJson(parsedJson, MediasList::class.java)
                val sortedMediaList = MediaUtils().sortedMediaByLatestDate(data)
                val imageEntityList = mutableListOf<MediaEntity>()
                sortedMediaList.forEach { listItem ->
                    imageEntityList.add(
                        MediaEntity(
                            copyright = listItem.copyright,
                            date = listItem.date,
                            explanation = listItem.explanation,
                            hdurl = listItem.hdurl,
                            title = listItem.title,
                            url = listItem.url,
                        )
                    )
                }
                emit(BaseResult.Success(imageEntityList))
            } else {
                val error: WrappedListResponse<MediasList> = WrappedListResponse(
                    code = 422, message = "Json Parsing Error", status = false,
                    errors = arrayListOf(),
                    data = arrayListOf()
                )
                emit(BaseResult.Error(error))
            }
        }
    }
}