package com.dangjang.android.domain.model

data class EditHealthMetricVO(
    val type: String = "",
    val createdAt: String = "",
    val unit: String = "",
    val guide: GuideVO = GuideVO()
)

data class GuideVO(
    val type: String = "",
    val alert: String = "",
    val title: String = "",
    val content: String = "",
    val todayGuides: List<TodayGuidesVO> = listOf()
)