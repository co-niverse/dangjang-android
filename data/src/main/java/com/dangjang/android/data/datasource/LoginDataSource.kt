package com.dangjang.android.data.datasource

import com.dangjang.android.data.model.dto.LoginDto
import com.dangjang.android.data.model.response.BaseResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface LoginDataSource {

    //Login API
    // TODO : 서버 개발 후 수정
    @GET("/api/login")
    suspend fun login(
        @Query("accessToken") accessToken: String
    ) : ApiResponse<BaseResponse<LoginDto>>
}