package com.dangjang.android.data.datasource

import com.dangjang.android.data.model.dto.AuthDto
import com.dangjang.android.data.model.request.LoginRequest
import com.dangjang.android.data.model.response.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginApiService {
    //kakao Login API
    @POST("/api/auth/kakao")
    suspend fun kakaoLogin(
        @Header("FcmToken") fcmToken: String,
        @Body accessToken: LoginRequest
    ) : Response<BaseResponse<AuthDto>>

    //naver Login API
    @POST("/api/auth/naver")
    suspend fun naverLogin(
        @Header("FcmToken") fcmToken: String,
        @Body accessToken: LoginRequest
    ) : Response<BaseResponse<AuthDto>>
}