package com.dangjang.android.data.model.dto

import com.dangjang.android.domain.constants.UNKNOWN_STRING
import com.dangjang.android.domain.model.GuideVO
import com.dangjang.android.domain.model.PostPatchGlucoseVO
import com.google.gson.annotations.SerializedName

data class PostPatchGlucoseDto(
    @SerializedName("type") val type: String?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("unit") val unit: String?,
    @SerializedName("guide") val guide: Guide?
) {
    fun toDomain() = PostPatchGlucoseVO(
        type ?: UNKNOWN_STRING,
        createdAt ?: UNKNOWN_STRING,
        unit ?: UNKNOWN_STRING,
        guide?.toDomain() ?: GuideVO()
    )
}

data class Guide(
    @SerializedName("type") val type: String?,
    @SerializedName("alert") val alert: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("content") val content: String?,
    @SerializedName("todayGuides") val todayGuides: List<TodayGuidesDto>?,
) {
    fun toDomain() = GuideVO(
        type ?: UNKNOWN_STRING,
        alert ?: UNKNOWN_STRING,
        title ?: UNKNOWN_STRING,
        content ?: UNKNOWN_STRING,
        todayGuides?.map { it.toDomain() } ?: listOf()
    )
}