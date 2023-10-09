package com.dangjang.android.domain.model

data class GetNotificationVO(
    val notificationList: List<AlarmListVO> = listOf()
)

data class AlarmListVO(
    val notificationId: Int = 0,
    val title: String = "",
    val content: String = "",
    val type: String = "",
    val createdAt: String = ""
)