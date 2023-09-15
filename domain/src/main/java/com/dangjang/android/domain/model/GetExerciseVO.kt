package com.dangjang.android.domain.model

data class GetExerciseVO(
    val createdAt: String = "",
    val needStepByTTS: Int = 0,
    val needStepByLastWeek: Int = 0,
    val comparedToLastWeek: String = "",
    val content: String = "",
    val stepsCount: Int = 0,
    val exerciseCalories: List<GetExerciseCaloriesVO> = listOf()
)

data class GetExerciseCaloriesVO(
    val type: String = "",
    val calorie: Int = 0,
    val exerciseTime: Int = 0
)