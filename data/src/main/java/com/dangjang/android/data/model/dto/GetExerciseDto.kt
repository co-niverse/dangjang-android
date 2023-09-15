package com.dangjang.android.data.model.dto

import com.dangjang.android.domain.constants.UNKNOWN_INT
import com.dangjang.android.domain.constants.UNKNOWN_STRING
import com.dangjang.android.domain.model.GetExerciseCaloriesVO
import com.dangjang.android.domain.model.GetExerciseVO
import com.google.gson.annotations.SerializedName

data class GetExerciseDto(
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("needStepByTTS") val needStepByTTS: Int?,
    @SerializedName("needStepByLastWeek") val needStepByLastWeek: Int?,
    @SerializedName("comparedToLastWeek") val comparedToLastWeek: String?,
    @SerializedName("content") val content: String?,
    @SerializedName("stepsCount") val stepsCount: Int?,
    @SerializedName("exerciseCalories") val exerciseCalories: List<GetExerciseCaloriesDto>?
) {
    fun toDomain() = GetExerciseVO(
        createdAt ?: UNKNOWN_STRING,
        needStepByTTS ?: UNKNOWN_INT,
        needStepByLastWeek ?: UNKNOWN_INT,
        comparedToLastWeek ?: UNKNOWN_STRING,
        content ?: UNKNOWN_STRING,
        stepsCount ?: UNKNOWN_INT,
        exerciseCalories?.map { it.toDomain() } ?: listOf()
    )
}

data class GetExerciseCaloriesDto(
    @SerializedName("type") val type: String?,
    @SerializedName("calorie") val calorie: Int?,
    @SerializedName("exerciseTime") val exerciseTime: Int?
) {
    fun toDomain() = GetExerciseCaloriesVO(
        type ?: UNKNOWN_STRING,
        calorie ?: UNKNOWN_INT,
        exerciseTime ?: UNKNOWN_INT
    )
}
