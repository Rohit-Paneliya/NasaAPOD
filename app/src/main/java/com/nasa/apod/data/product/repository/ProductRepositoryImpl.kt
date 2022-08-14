package com.nasa.apod.data.product.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nasa.apod.data.utils.WrappedListResponse
import com.nasa.apod.data.product.remote.api.ProductApi
import com.nasa.apod.data.product.remote.dto.ProductResponse
import com.nasa.apod.domain.common.base.BaseResult
import com.nasa.apod.domain.product.ProductRepository
import com.nasa.apod.domain.product.entity.ProductEntity
import com.nasa.apod.domain.product.entity.ProductUserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(private val productApi: ProductApi) : ProductRepository {
    override suspend fun getAllMyProducts(): Flow<BaseResult<List<ProductEntity>, WrappedListResponse<ProductResponse>>> {
        return flow {
            val response = productApi.getAllMyProducts()
            if (response.isSuccessful){
                val body = response.body()!!
                val products = mutableListOf<ProductEntity>()
                var user: ProductUserEntity?
                body.data?.forEach { productResponse ->
                    user = ProductUserEntity(productResponse.user.id, productResponse.user.name, productResponse.user.email)
                    products.add(ProductEntity(
                        productResponse.id,
                        productResponse.name,
                        productResponse.price,
                        user!!
                    ))
                }
                emit(BaseResult.Success(products))
            }else{
                val type = object : TypeToken<WrappedListResponse<ProductResponse>>(){}.type
                val err = Gson().fromJson<WrappedListResponse<ProductResponse>>(response.errorBody()!!.charStream(), type)!!
                err.code = response.code()
                emit(BaseResult.Error(err))
            }
        }
    }
}