package com.dangjang.android.data.model.dto

import com.dangjang.android.domain.constants.UNKNOWN_DOUBLE
import com.dangjang.android.domain.constants.UNKNOWN_INT
import com.dangjang.android.domain.constants.UNKNOWN_STRING
import com.dangjang.android.domain.model.PostPatchWeightVO
import com.dangjang.android.domain.model.WeightGuideVO
import com.google.gson.annotations.SerializedName

data class PostPatchWeightDto(
    @SerializedName("type") val type: String?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("unit") val unit: String?,
    @SerializedName("guide") val guide: WeightGuideDto?
) {
    fun toDomain() = PostPatchWeightVO(
        type ?: UNKNOWN_STRING,
        createdAt ?: UNKNOWN_STRING,
        unit ?: UNKNOWN_STRING,
        guide?.toDomain() ?: WeightGuideVO()
    )
}

data class WeightGuideDto(
    @SerializedName("type") val type: String?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("weightDiff") val weightDiff: Int?,
    @SerializedName("title") val title: String?,
    @SerializedName("content") val content: String?,
    @SerializedName("bmi") val bmi: Double?
) {
    fun toDomain() = WeightGuideVO(
        type ?: UNKNOWN_STRING,
        createdAt ?: UNKNOWN_STRING,
        weightDiff ?: UNKNOWN_INT,
        title ?: UNKNOWN_STRING,
        content ?: UNKNOWN_STRING,
        bmi ?: UNKNOWN_DOUBLE
    )
}