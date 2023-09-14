package com.dangjang.android.domain.model

data class GetWeightVO(
    val type: String = "",
    val createdAt: String = "",
    val weightDiff: Int = 0,
    val title: String = "",
    val content: String = "",
    val bmi: Double = 0.0,
    val unit: String = ""
)
