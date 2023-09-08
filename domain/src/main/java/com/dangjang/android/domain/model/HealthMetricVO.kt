package com.dangjang.android.domain.model

data class HealthMetricVO(
    val type: String = "",
    val createdAt: String = "",
    val unit: String = "",
    val guide: GuideVO = GuideVO()
)

data class GuideVO(
    val type: String = "",
    val alert: String = "",
    val content: String = ""
)