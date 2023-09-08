package com.dangjang.android.data.datasource

import com.dangjang.android.data.model.dto.IntroDto
import com.dangjang.android.data.model.response.BaseResponse
import retrofit2.Response
import retrofit2.http.GET

interface IntroApiService {
    //Intro API
    @GET("api/intro/prod")
    suspend fun getIntroApi(
    ) : Response<BaseResponse<IntroDto>>
}