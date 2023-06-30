package com.dangjang.android.data.model.dto

import com.dangjang.android.domain.model.IntroVO
import com.dangjang.android.domain.model.LoginVO
import com.google.gson.annotations.SerializedName

data class LoginDto(
    @SerializedName("token") val token: String?,
    ) {

    fun toDomain() = LoginVO(
        token ?: UNKNOWN_STRING
    )
}

const val UNKNOWN_STRING = ""