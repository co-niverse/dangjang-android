package com.dangjang.android.data.model.dto

import com.dangjang.android.domain.constants.UNKNOWN_INT
import com.dangjang.android.domain.constants.UNKNOWN_STRING
import com.dangjang.android.domain.model.AlarmListVO
import com.dangjang.android.domain.model.GetNotificationVO
import com.google.gson.annotations.SerializedName

data class GetNotificationDto(
    @SerializedName("notificationResponseList") val notificationList: List<NotificationDto>?
) {
    fun toDomain() = GetNotificationVO(
        notificationList?.map { it.toDomain() } ?: listOf()
    )
}

data class NotificationDto(
    @SerializedName("notificationId") val notificationId: Int?,
    @SerializedName("title") val title: String?,
    @SerializedName("content") val content: String?,
    @SerializedName("type") val type: String,
    @SerializedName("createdAt") val createdAt: String?
) {
    fun toDomain() = AlarmListVO(
        notificationId ?: UNKNOWN_INT,
        title ?: UNKNOWN_STRING,
        content ?: UNKNOWN_STRING,
        type ?: UNKNOWN_STRING,
        createdAt ?: UNKNOWN_STRING
    )
}
