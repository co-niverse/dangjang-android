package com.dangjang.android.domain.request

data class PostPointRequest(
    val type: String = "",
    val phone: String = "",
    val name: String = "",
    val comment: String = ""
)
