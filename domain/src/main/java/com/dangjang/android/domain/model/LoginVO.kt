package com.dangjang.android.domain.model

data class LoginVO(
    val nickname: String = "",
    val accessToken: String = "",
    val refreshToken: String = "",
    val expiresIn: Long = 0,
    val oauthId: Long = 0
    )