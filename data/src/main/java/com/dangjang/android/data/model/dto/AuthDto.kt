package com.dangjang.android.data.model.dto

import com.dangjang.android.domain.model.AuthVO
import com.google.gson.annotations.SerializedName

data class AuthDto(
    @SerializedName("nickname") val nickname: String?,
    @SerializedName("dangjangClub") val dangjangClub: Boolean?,
    @SerializedName("healthConnect") val healthConnect: Boolean?
    ) {

    fun toDomain() = AuthVO(
        nickname ?: UNKNOWN_STRING,
        dangjangClub ?: UNKNOWN_BOOLEAN,
        healthConnect ?: UNKNOWN_BOOLEAN
    )
}

const val UNKNOWN_BOOLEAN = false
const val UNKNOWN_STRING = ""