package com.dangjang.android.domain.model

data class GetPointVO(
    val balancedPoint: Int = 0,
    val products: List<ProductVO> = listOf()
)

data class ProductVO(
    val title: String = "",
    val price: Int = 0
)
