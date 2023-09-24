package com.dangjang.android.domain.model

data class PostPatchWeightVO(
    val type: String = "",
    val createdAt: String = "",
    val unit: String = "",
    val guide: WeightGuideVO = WeightGuideVO()
)

data class WeightGuideVO(
    val type: String = "",
    val createdAt: String = "",
    val weightDiff: Int = 0,
    val title: String = "",
    val content: String = "",
    val bmi: Double = 0.0
)