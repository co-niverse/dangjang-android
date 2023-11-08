package com.dangjang.android.data.sdui

import com.dangjang.android.domain.constants.UNKNOWN_INT
import com.dangjang.android.domain.constants.UNKNOWN_STRING
import com.dangjang.android.domain.sdui.SduiSignupVO
import com.google.gson.annotations.SerializedName

data class SduiSignupDto(
    @SerializedName("textId") val textId: Int?,
    @SerializedName("text") val text: String?
) {
    fun toDomain() = SduiSignupVO(
        textId ?: UNKNOWN_INT,
        text ?: UNKNOWN_STRING
    )
}
