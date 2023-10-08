package com.dangjang.android.data.model.dto

import com.dangjang.android.domain.constants.UNKNOWN_INT
import com.dangjang.android.domain.constants.UNKNOWN_STRING
import com.dangjang.android.domain.model.GetPointVO
import com.dangjang.android.domain.model.ProductVO
import com.google.gson.annotations.SerializedName

data class GetPointDto(
    @SerializedName("balancedPoint") val balancedPoint: Int?,
    @SerializedName("productList") val products: List<ProductDto>?
) {
    fun toDomain() = GetPointVO(
        balancedPoint ?: UNKNOWN_INT,
        products?.map { it.toDomain() } ?: listOf()
    )
}

data class ProductDto(
    @SerializedName("productName") val title: String?,
    @SerializedName("point") val price: Int?
) {
    fun toDomain() = ProductVO(
        title ?: UNKNOWN_STRING,
        price ?: UNKNOWN_INT
    )
}