package com.dangjang.android.domain.model

data class EditWeightExerciseVO(
    val type: String = "",
    val createdAt: String = "",
    val unit: String = "",
    val guide: WeightExerciseGuideVO = WeightExerciseGuideVO()
)

data class WeightExerciseGuideVO(
    val type: String = "",
    val title: String = "",
    val content: String = ""
)