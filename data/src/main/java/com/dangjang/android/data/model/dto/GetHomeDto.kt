package com.dangjang.android.data.model.dto

import com.dangjang.android.domain.constants.UNKNOWN_BOOLEAN
import com.dangjang.android.domain.constants.UNKNOWN_DOUBLE
import com.dangjang.android.domain.constants.UNKNOWN_INT
import com.dangjang.android.domain.constants.UNKNOWN_STRING
import com.dangjang.android.domain.model.GetHomeExerciseVO
import com.dangjang.android.domain.model.GetHomeVO
import com.dangjang.android.domain.model.GetHomeWeightVO
import com.google.gson.annotations.SerializedName

data class GetHomeDto(
    @SerializedName("nickname") val nickname: String?,
    @SerializedName("date") val date: String?,
    @SerializedName("bloodSugars") val bloodSugars: List<TodayGuidesDto>?,
    @SerializedName("weight") val weight: GetHomeWeightDto?,
    @SerializedName("exercise") val exercise: GetHomeExerciseDto?,
    @SerializedName("notification") val notification: Boolean?
) {
    fun toDomain() = GetHomeVO(
        nickname ?: UNKNOWN_STRING,
        date ?: UNKNOWN_STRING,
        bloodSugars?.map { it.toDomain() } ?: listOf(),
        weight?.toDomain() ?: GetHomeWeightVO(),
        exercise?.toDomain() ?: GetHomeExerciseVO(),
        notification ?: UNKNOWN_BOOLEAN
    )
}

data class GetHomeWeightDto(
    @SerializedName("unit") val unit: String?,
    @SerializedName("bmi") val bmi: Double?,
    @SerializedName("title") val title: String?
) {
    fun toDomain() = GetHomeWeightVO(
        unit ?: UNKNOWN_STRING,
        bmi ?: UNKNOWN_DOUBLE,
        title ?: UNKNOWN_STRING
    )
}

data class GetHomeExerciseDto(
    @SerializedName("calorie") val calorie: Int?,
    @SerializedName("stepCount") val stepCount: Int?
) {
    fun toDomain() = GetHomeExerciseVO(
        calorie ?: UNKNOWN_INT,
        stepCount ?: UNKNOWN_INT
    )
}