package com.dangjang.android.domain.requestVO

data class SignupRequestVO(
    val accessToken: String = "",
    val provider: String = "",
    val nickname: String = "",
    val gender: Boolean = false,
    val birthday: String = "",
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
