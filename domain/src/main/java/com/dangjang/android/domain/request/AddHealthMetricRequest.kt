package com.dangjang.android.domain.request

data class AddHealthMetricRequest(
    val type: String = "",
    val createdAt: String = "",
    val unit: String = ""
)
