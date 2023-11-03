package com.dangjang.android.domain.request

data class PostFcmTokenRequest(
    val fcmToken: String = "",
    val deviceId: String = ""
)