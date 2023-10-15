package com.dangjang.android.data.model.dto

import com.dangjang.android.domain.constants.UNKNOWN_STRING
import com.dangjang.android.domain.model.IntroVO
import com.google.gson.annotations.SerializedName

data class IntroDto(
    @SerializedName("minVersion") val minVersion: String?,
    @SerializedName("latestVersion") val latestVersion: String?
    ) {
    fun toDomain() = IntroVO(
        minVersion ?: UNKNOWN_STRING,
        latestVersion ?: UNKNOWN_STRING
    )
}