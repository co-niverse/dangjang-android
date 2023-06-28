package com.dangjang.android.data.model.dto

import com.dangjang.android.domain.model.IntroVO
import com.google.gson.annotations.SerializedName

data class IntroDto(
    @SerializedName("minVersion") val minVersion: Int?,
    @SerializedName("latestVersion") val latestVersion: Int?
    ) {
    fun toDomain() = IntroVO(
        minVersion ?: UNKNOWN,
        latestVersion ?: UNKNOWN
    )
}

const val UNKNOWN = -1