package com.dangjang.android.domain.model

data class PostPatchExerciseVO(
    val type: String = "",
    val createdAt: String = "",
    val unit: String = "",
    val guide: ExerciseGuideVO = ExerciseGuideVO()
)

data class ExerciseGuideVO(
    val createdAt: String = "",
    val needStepByTTS: Int = 0,
    val needStepByLastWeek: Int = 0,
    val comparedToLastWeek: String = "",
    val content: String = "",
    val exerciseCalories: List<ExerciseCaloriesVO> = listOf()
)

data class ExerciseCaloriesVO(
    val type: String = "",
    val calorie: Int = 0
)
