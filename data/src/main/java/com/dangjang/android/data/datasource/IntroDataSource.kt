package com.dangjang.android.data.datasource

import com.dangjang.android.data.model.dto.IntroDto
import com.dangjang.android.data.model.response.BaseResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET

interface IntroDataSource {

    //Intro API
    @GET("api/v1/intro")
    suspend fun getIntroApi(
    ) : ApiResponse<BaseResponse<IntroDto>>
}