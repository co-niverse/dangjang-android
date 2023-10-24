package com.dangjang.android.data.model.dto

import com.dangjang.android.domain.constants.UNKNOWN_INT
import com.dangjang.android.domain.constants.UNKNOWN_STRING
import com.dangjang.android.domain.model.PostPointVO
import com.google.gson.annotations.SerializedName

data class PostPointDto(
    @SerializedName("phone") val phone: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("changePoint") val changePoint: Int?,
    @SerializedName("balancedPoint") val balancedPoint: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("comment") val comment: String?
) {
    fun toDomain() = PostPointVO(
        phone ?: UNKNOWN_STRING,
        type ?: UNKNOWN_STRING,
        changePoint ?: UNKNOWN_INT,
        balancedPoint ?: UNKNOWN_INT,
        name ?: UNKNOWN_STRING,
        comment ?: UNKNOWN_STRING
    )
}
