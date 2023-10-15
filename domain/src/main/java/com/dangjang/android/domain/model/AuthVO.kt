package com.dangjang.android.domain.model

data class AuthVO(
    val nickname: String = "",
    val dangjangClub: Boolean = false,
    val healthConnect: Boolean = false
)