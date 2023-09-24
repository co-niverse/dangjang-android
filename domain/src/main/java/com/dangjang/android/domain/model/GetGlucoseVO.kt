package com.dangjang.android.domain.model

data class GetGlucoseVO(
    val createdAt: String = "",
    val todayGuides: List<TodayGuidesVO> = listOf(),
    val guides: List<GuidesVO> = listOf()
)

data class TodayGuidesVO(
    val alert: String = "",
    val count: Int = 0
)

data class GuidesVO(
    val type: String = "",
    val unit: String = "",
    val alert: String = "",
    val title: String = "",
    val content: String = ""
)
