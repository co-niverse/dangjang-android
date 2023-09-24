package com.dangjang.android.data.model.dto

import com.dangjang.android.domain.constants.UNKNOWN_BOOLEAN
import com.dangjang.android.domain.model.DuplicateNicknameVO
import com.google.gson.annotations.SerializedName

data class DuplicateNicknameDto(
    @SerializedName("duplicate") val duplicate: Boolean?
) {

    fun toDomain() = DuplicateNicknameVO(
        duplicate ?: UNKNOWN_BOOLEAN
    )
}
