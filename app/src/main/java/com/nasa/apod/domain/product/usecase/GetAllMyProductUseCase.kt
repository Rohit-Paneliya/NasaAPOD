package com.nasa.apod.domain.product.usecase

import com.nasa.apod.data.utils.WrappedListResponse
import com.nasa.apod.data.product.remote.dto.ProductResponse
import com.nasa.apod.domain.common.base.BaseResult
import com.nasa.apod.domain.product.ProductRepository
import com.nasa.apod.domain.product.entity.ProductEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllMyProductUseCase @Inject constructor(private val productRepository: ProductRepository) {
    suspend fun invoke() : Flow<BaseResult<List<ProductEntity>, WrappedListResponse<ProductResponse>>> {
        return productRepository.getAllMyProducts()
    }
}