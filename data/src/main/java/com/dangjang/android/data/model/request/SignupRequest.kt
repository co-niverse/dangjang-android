package com.dangjang.android.data.model.request

import com.google.gson.annotations.SerializedName
import java.util.Date

data class SignupRequest (
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("provider") val provider: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("gender") val gender: Boolean,
    @SerializedName("birthday") val birthday: Date,
    @SerializedName("height") val height: Int,
    @SerializedName("weight") val weight: Int,
    @SerializedName("activityAmount") val activityAmount: String,
    @SerializedName("diabetes") val diabetes: Boolean,
    @SerializedName("diabetes_year") val diabetes_year: Int,
    @SerializedName("medicine") val medicine: Boolean,
    @SerializedName("injection") val injection: Boolean,
    @SerializedName("diseases") val diseases: List<String>
    )