package com.dangjang.android.domain.model

data class GetHomeVO(
    val nickname: String = "",
    val date: String = "",
    val bloodSugars: List<TodayGuidesVO> = listOf(),
    val weight: GetHomeWeightVO = GetHomeWeightVO(),
    val exercise: GetHomeExerciseVO = GetHomeExerciseVO(),
    val notification: Boolean = false,
    val userLog: UserLogVO = UserLogVO()
)

data class GetHomeWeightVO(
    val unit: String = "",
    val bmi: Double = 0.0,
    val title: String = ""
)

data class GetHomeExerciseVO(
    val calorie: Int = 0,
    val stepCount: Int = 0
)

data class UserLogVO(
    val gender: Boolean = false,
    val birthYear: Int = 0,
    val diabetic: Boolean = false,
    val diabetesYear: Int = 0,
    val weightAlert: String = ""
)