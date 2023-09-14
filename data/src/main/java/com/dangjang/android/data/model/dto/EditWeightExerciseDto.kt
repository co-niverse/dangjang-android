package com.dangjang.android.data.model.dto

import com.dangjang.android.domain.constants.UNKNOWN_STRING
import com.dangjang.android.domain.model.EditWeightExerciseVO
import com.dangjang.android.domain.model.WeightExerciseGuideVO
import com.google.gson.annotations.SerializedName

data class EditWeightExerciseDto(
    @SerializedName("type") val type: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("content") val content: String?,
    @SerializedName("guide") val guide: WeightExerciseGuideDto?
) {
    fun toDomain() = EditWeightExerciseVO(
        type ?: UNKNOWN_STRING,
        title ?: UNKNOWN_STRING,
        content ?: UNKNOWN_STRING,
        guide?.toDomain() ?: WeightExerciseGuideVO()
    )
}

data class WeightExerciseGuideDto(
    @SerializedName("type") val type: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("content") val content: String?
) {
    fun toDomain() = WeightExerciseGuideVO(
        type ?: UNKNOWN_STRING,
        title ?: UNKNOWN_STRING,
        content ?: UNKNOWN_STRING
    )
}