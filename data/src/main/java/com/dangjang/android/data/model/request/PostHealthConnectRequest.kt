package com.dangjang.android.data.model.request

import com.google.gson.annotations.SerializedName

data class PostHealthConnectRequest (
    @SerializedName("data") val data: List<HealthConnectRequest>
    )