package com.dangjang.android.domain.model

data class SignupVO(
    val nickname: String = "",
    val accessToken: String = "",
    val refreshToken: String = "",
    val dangjangClub: Boolean = false,
    val healthConnect: Boolean = false
    )