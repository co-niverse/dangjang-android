package com.dangjang.android.domain.model

data class PostWeightVO(
    val type: String = "",
    val createdAt: String = "",
    val unit: String = "",
    val guide: GuideVO = GuideVO()
)

data class WeightGuideVO(
    val type: String,
    val createdAt: String,
    val weightDiff: Int,
    val title: String,
    val content: String,
    val bmi: Double
)