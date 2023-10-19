package com.dangjang.android.data.datasource

import com.dangjang.android.data.model.dto.IntroDto
import com.dangjang.android.domain.request.PostHealthConnectRequest
import com.dangjang.android.data.model.response.BaseResponse
import com.dangjang.android.domain.request.PatchHealthConnectRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

interface HealthConnectApiService {
    //Intro API
    @GET("api/intro/prod")
    suspend fun getIntroApi(
    ) : Response<BaseResponse<IntroDto>>

    //헬스커넥트 데이터 입력 API
    @POST("api/health-connect")
    suspend fun postHealthConnect(
        @Header("Authorization") accessToken: String,
        @Body postHealthConnectRequest: PostHealthConnectRequest
    ): Response<BaseResponse<Nothing>>

    //헬스커넥트 연동 여부 등록 API
    @PATCH("api/health-connect/interlock")
    suspend fun patchHealthConnectInterlock(
        @Header("Authorization") accessToken: String,
        @Body patchHealthConnectRequest: PatchHealthConnectRequest
    ): Response<BaseResponse<Nothing>>
}