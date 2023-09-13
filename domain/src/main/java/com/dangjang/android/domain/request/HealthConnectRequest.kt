package com.dangjang.android.data.model.request

import com.google.gson.annotations.SerializedName

data class HealthConnectRequest (
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("type") val type: String,
    @SerializedName("unit") val unit: String
    )