package com.dangjang.android.data.model.dto

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
        createdAt ?: UNKNOWN,
        todayGuides?.map { it.toDomain() } ?: listOf(),
        guides?.map { it.toDomain() } ?: listOf()
    )
}

data class TodayGuidesDto(
    @SerializedName("alert") val alert: String,
    @SerializedName("count") val count: Int
) {
    fun toDomain() = TodayGuidesVO(
        alert ?: UNKNOWN,
        count ?: UNKNOWN_COUNT
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
        type ?: UNKNOWN,
        unit ?: UNKNOWN,
        alert ?: UNKNOWN,
        title ?: UNKNOWN,
        content ?: UNKNOWN
    )
}

const val UNKNOWN_COUNT = 0