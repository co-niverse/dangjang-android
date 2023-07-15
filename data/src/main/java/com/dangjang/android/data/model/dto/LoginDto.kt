package com.dangjang.android.data.model.dto

import com.dangjang.android.domain.model.LoginVO
import com.google.gson.annotations.SerializedName

data class ContentDto(
    @SerializedName("content") val content: LoginDto
)

data class LoginDto(
    @SerializedName("nickname") val nickname: String?,
    @SerializedName("accessToken") val accessToken: String?,
    @SerializedName("refreshToken") val refreshToken: String?,
    @SerializedName("expiresIn") val expiresIn: Long?,
    @SerializedName("oauthId") val oauthId: Long?
    ) {

    fun toDomain() = LoginVO(
        nickname ?: UNKNOWN_STRING,
        accessToken ?: UNKNOWN_STRING,
        refreshToken ?: UNKNOWN_STRING,
        expiresIn ?: UNKNOWN_LONG,
        oauthId ?: UNKNOWN_LONG
    )
}

const val UNKNOWN_STRING = ""
const val UNKNOWN_LONG = 0L