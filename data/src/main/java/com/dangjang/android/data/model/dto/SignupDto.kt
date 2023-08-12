package com.dangjang.android.data.model.dto

import com.dangjang.android.domain.model.SignupVO
import com.google.gson.annotations.SerializedName

data class SignupDto(
    @SerializedName("nickname") val nickname: String?,
    @SerializedName("accessToken") val accessToken: String?,
    @SerializedName("refreshToken") val refreshToken: String?,
    @SerializedName("dangjangClub") val dangjangClub: Boolean?,
    @SerializedName("healthConnect") val healthConnect: Boolean?
    ) {

    fun toDomain() = SignupVO(
        nickname ?: UNKNOWN_STRING,
        accessToken ?: UNKNOWN_STRING,
        refreshToken ?: UNKNOWN_STRING,
        dangjangClub ?: UNKNOWN_BOOLEAN,
        healthConnect ?: UNKNOWN_BOOLEAN
    )
}

const val UNKNOWN_BOOLEAN = false