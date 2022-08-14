package com.nasa.apod.domain.product

import com.nasa.apod.data.utils.WrappedListResponse
import com.nasa.apod.data.product.remote.dto.ProductResponse
import com.nasa.apod.domain.common.base.BaseResult
import com.nasa.apod.domain.product.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getAllMyProducts() : Flow<BaseResult<List<ProductEntity>, WrappedListResponse<ProductResponse>>>
}