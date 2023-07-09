package com.dangjang.android.data.datasource

import com.dangjang.android.data.model.dto.ContentDto
import com.dangjang.android.data.model.response.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query

interface LoginDataSource {

    //kakao Login API
    @GET("/api/auth/kakao")
    suspend fun kakaoLogin(
        @Query("accessToken") accessToken: String
    ) : Response<BaseResponse<ContentDto>>

    //naver Login API
    @GET("/api/auth/naver")
    suspend fun naverLogin(
        @Query("accessToken") accessToken: String
    ) : Response<BaseResponse<ContentDto>>

}