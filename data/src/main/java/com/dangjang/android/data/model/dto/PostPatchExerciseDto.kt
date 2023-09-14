package com.dangjang.android.data.model.dto

import com.dangjang.android.domain.constants.UNKNOWN_INT
import com.dangjang.android.domain.constants.UNKNOWN_STRING
import com.dangjang.android.domain.model.ExerciseCaloriesVO
import com.dangjang.android.domain.model.ExerciseGuideVO
import com.dangjang.android.domain.model.PostPatchExerciseVO
import com.google.gson.annotations.SerializedName

data class PostPatchExerciseDto(
    @SerializedName("type") val type: String?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("unit") val unit: String?,
    @SerializedName("guide") val guide: ExerciseGuideDto?
) {
    fun toDomain() = PostPatchExerciseVO(
        type ?: UNKNOWN_STRING,
        createdAt ?: UNKNOWN_STRING,
        unit ?: UNKNOWN_STRING,
        guide?.toDomain() ?: ExerciseGuideVO()
    )
}

data class ExerciseGuideDto(
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("needStepByTTS") val needStepByTTS: Int?,
    @SerializedName("needStepByLastWeek") val needStepByLastWeek: Int?,
    @SerializedName("comparedToLastWeek") val comparedToLastWeek: String?,
    @SerializedName("content") val content: String?,
    @SerializedName("exerciseCalories") val exerciseCalories: List<ExerciseCaloriesDto>?
) {
    fun toDomain() = ExerciseGuideVO(
        createdAt ?: UNKNOWN_STRING,
        needStepByTTS ?: UNKNOWN_INT,
        needStepByLastWeek ?: UNKNOWN_INT,
        comparedToLastWeek ?: UNKNOWN_STRING,
        content ?: UNKNOWN_STRING,
        exerciseCalories?.map { it.toDomain() } ?: listOf()
    )
}

data class ExerciseCaloriesDto(
    @SerializedName("type") val type: String?,
    @SerializedName("calorie") val calorie: Int?
) {
    fun toDomain() = ExerciseCaloriesVO(
        type ?: UNKNOWN_STRING,
        calorie ?: UNKNOWN_INT
    )
}