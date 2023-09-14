package com.dangjang.android.data.model.dto

import com.dangjang.android.domain.constants.UNKNOWN_INT
import com.dangjang.android.domain.constants.UNKNOWN_STRING
import com.dangjang.android.domain.model.GetGlucoseVO
import com.dangjang.android.domain.model.GuidesVO
import com.dangjang.android.domain.model.TodayGuidesVO
import com.google.gson.annotations.SerializedName

data class GetGlucoseDto(
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("todayGuides") val todayGuides: List<TodayGuidesDto>?,
    @SerializedName("guides") val guides: List<GuidesDto>?
) {
    fun toDomain() = GetGlucoseVO(
        createdAt ?: UNKNOWN_STRING,
        todayGuides?.map { it.toDomain() } ?: listOf(),
        guides?.map { it.toDomain() } ?: listOf()
    )
}

data class TodayGuidesDto(
    @SerializedName("alert") val alert: String,
    @SerializedName("count") val count: Int
) {
    fun toDomain() = TodayGuidesVO(
        alert ?: UNKNOWN_STRING,
        count ?: UNKNOWN_INT
    )
}

data class GuidesDto(
    @SerializedName("type") val type: String,
    @SerializedName("unit") val unit: String,
    @SerializedName("alert") val alert: String,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String
) {
    fun toDomain() = GuidesVO(
        type ?: UNKNOWN_STRING,
        unit ?: UNKNOWN_STRING,
        alert ?: UNKNOWN_STRING,
        title ?: UNKNOWN_STRING,
        content ?: UNKNOWN_STRING
    )
}