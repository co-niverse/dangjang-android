package com.dangjang.android.data.model.dto

import com.dangjang.android.domain.model.GuideVO
import com.dangjang.android.domain.model.HealthMetricVO
import com.google.gson.annotations.SerializedName

data class HealthMetricDto(
    @SerializedName("type") val type: String?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("unit") val unit: String?,
    @SerializedName("guide") val guide: Guide?
) {
    fun toDomain() = HealthMetricVO(
        type ?: UNKNOWN,
        createdAt ?: UNKNOWN,
        unit ?: UNKNOWN,
        guide?.toDomain() ?: GuideVO()
    )
}

data class Guide(
    @SerializedName("type") val type: String?,
    @SerializedName("alert") val alert: String?,
    @SerializedName("content") val content: String?
) {
    fun toDomain() = GuideVO(
        type ?: UNKNOWN,
        alert ?: UNKNOWN,
        content ?: UNKNOWN
    )
}