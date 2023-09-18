package com.dangjang.android.data.model.dto

import com.dangjang.android.domain.constants.UNKNOWN_INT
import com.dangjang.android.domain.constants.UNKNOWN_STRING
import com.dangjang.android.domain.model.GetChartVO
import com.dangjang.android.domain.model.MinMaxChartVO
import com.dangjang.android.domain.model.SingleChartVO
import com.google.gson.annotations.SerializedName

data class GetChartDto(
    @SerializedName("startDate") val startDate: String?,
    @SerializedName("endDate") val endDate: String?,
    @SerializedName("bloodSugars") val bloodSugars: List<MinMaxChartDto>?,
    @SerializedName("weights") val weights: List<SingleChartDto>?,
    @SerializedName("stepCounts") val stepCounts: List<SingleChartDto>?,
    @SerializedName("exerciseCalories") val exerciseCalories: List<SingleChartDto>?
) {
    fun toDomain() = GetChartVO(
        startDate ?: UNKNOWN_STRING,
        endDate ?: UNKNOWN_STRING,
        bloodSugars?.map { it.toDomain() } ?: listOf(),
        weights?.map { it.toDomain() } ?: listOf(),
        stepCounts?.map { it.toDomain() } ?: listOf(),
        exerciseCalories?.map { it.toDomain() } ?: listOf()
    )
}

data class MinMaxChartDto(
    @SerializedName("date") val date: String?,
    @SerializedName("minUnit") val minUnit: Int?,
    @SerializedName("maxUnit") val maxUnit: Int?
) {
    fun toDomain() = MinMaxChartVO(
        date ?: UNKNOWN_STRING,
        minUnit ?: UNKNOWN_INT,
        maxUnit ?: UNKNOWN_INT
    )
}

data class SingleChartDto(
    @SerializedName("date") val date: String?,
    @SerializedName("unit") val unit: Int?
) {
    fun toDomain() = SingleChartVO(
        date ?: UNKNOWN_STRING,
        unit ?: UNKNOWN_INT
    )
}