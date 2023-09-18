package com.dangjang.android.domain.model

data class GetChartVO(
    val startDate: String = "",
    val endDate: String = "",
    val bloodSugars: List<MinMaxChartVO> = listOf(),
    val weights: List<SingleChartVO> = listOf(),
    val stepCounts: List<SingleChartVO> = listOf(),
    val exerciseCalories: List<SingleChartVO> = listOf()
)

data class MinMaxChartVO(
    val date: String = "",
    val minUnit: Int = 0,
    val maxUnit: Int = 0
)

data class SingleChartVO(
    val date: String = "",
    val unit: Int = 0
)