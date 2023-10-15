package com.dangjang.android.domain.model

data class GlucoseListVO(
    val time: String = "공복",
    val glucose: String = "",
    val alertIcon: Int = 0,
    val feedbackTitle: String = "",
    val feedbackContent: String = ""
)
