package com.dangjang.android.data.model.response

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("errorCode")
    val errorCode: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val `data`: T
)