package com.dangjang.android.domain.request

data class EditHealthMetricRequest(
    val type: String = "",
    val newType: String = "",
    val createdAt: String = "",
    val unit: String = ""
)

data class EditSameHealthMetricRequest(
    val type: String = "",
    val createdAt: String = "",
    val unit: String = ""
)