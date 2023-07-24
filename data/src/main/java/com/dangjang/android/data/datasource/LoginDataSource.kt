package com.dangjang.android.data.datasource

import com.dangjang.android.data.model.dto.LoginDto
import com.dangjang.android.data.model.request.LoginRequest
import com.dangjang.android.data.model.response.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginDataSource {

    //kakao Login API
    @POST("/api/auth/kakao")
    suspend fun kakaoLogin(
        @Body accessToken: LoginRequest
    ) : Response<BaseResponse<LoginDto>>

    //naver Login API
    @POST("/api/auth/naver")
    suspend fun naverLogin(
        @Body accessToken: LoginRequest
    ) : Response<BaseResponse<LoginDto>>

}