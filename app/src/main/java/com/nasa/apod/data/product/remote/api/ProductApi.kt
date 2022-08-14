package com.nasa.apod.data.product.remote.api

import com.nasa.apod.data.utils.WrappedListResponse
import com.nasa.apod.data.product.remote.dto.ProductResponse
import retrofit2.Response
import retrofit2.http.*

interface ProductApi {
    @GET("product/")
    suspend fun getAllMyProducts() : Response<WrappedListResponse<ProductResponse>>
}