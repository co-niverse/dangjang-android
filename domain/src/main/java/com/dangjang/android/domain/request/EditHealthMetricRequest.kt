package com.dangjang.android.domain.request

data class EditHealthMetricRequest (
    val type: String,
    val newType: String,
    val createdAt: String,
    val unit: String
    )