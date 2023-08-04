package com.dangjang.android.data.model.request

import com.google.gson.annotations.SerializedName

data class DuplicateNicknameRequest (
    @SerializedName("nickname") val nickname: String
)