package com.dangjang.android.data.model.dto

import com.dangjang.android.domain.constants.UNKNOWN_STRING
import com.dangjang.android.domain.model.GetLastDateVO
import com.google.gson.annotations.SerializedName

data class GetLastDateDto(
    @SerializedName("date") val date: String?
    ) {
    fun toDomain() = GetLastDateVO(
        date ?: UNKNOWN_STRING
    )
}