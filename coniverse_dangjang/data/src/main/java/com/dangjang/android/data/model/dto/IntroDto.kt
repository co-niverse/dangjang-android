package com.dangjang.android.data.model.dto

import com.google.gson.annotations.SerializedName

data class IntroDto(
    @SerializedName("minVersion") val minVersion: Int,
    @SerializedName("latestVersion") val latestVersion: Int
    )