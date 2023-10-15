package com.dangjang.android.data.model.dto

import com.dangjang.android.domain.constants.UNKNOWN_INT
import com.dangjang.android.domain.constants.UNKNOWN_STRING
import com.dangjang.android.domain.model.GetMypageVO
import com.google.gson.annotations.SerializedName

data class GetMypageDto(
    @SerializedName("nickname") val nickname: String?,
    @SerializedName("point") val point: Int?
) {
    fun toDomain() = GetMypageVO(
        nickname ?: UNKNOWN_STRING,
        point ?: UNKNOWN_INT
    )
}
