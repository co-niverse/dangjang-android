package com.dangjang.android.data.datasource

import com.dangjang.android.data.model.dto.IntroDto
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface IntroDataSource {

    //Intro API
    @GET("api/v1/intro")
    suspend fun getIntroApi(
        @Header("isError") isError: Int
    ) : ApiResponse<IntroDto>
}