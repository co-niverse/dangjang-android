package com.dangjang.android.domain.requestVO

import java.util.Date

data class SignupRequestVO(
    val accessToken: String = "",
    val provider: String = "",
    val nickname: String = "",
    val gender: Boolean = false,
    val birthday: Date = Date(),
    val height: Int = 0,
    val weight: Int = 0,
    val activityAmount: String = "",
    val diabetes: Boolean = false,
    val diabetes_year: Int = 0,
    val medicine: Boolean = false,
    val injection: Boolean = false,
    val diseases: List<String> = listOf()
    ) {
}
