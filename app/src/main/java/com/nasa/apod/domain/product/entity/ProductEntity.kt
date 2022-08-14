package com.nasa.apod.domain.product.entity

data class ProductEntity(
    var id: Int,
    var name: String,
    var price: Int,
    var user: ProductUserEntity
)