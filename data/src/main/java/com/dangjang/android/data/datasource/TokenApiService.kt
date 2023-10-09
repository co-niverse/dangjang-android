package com.dangjang.android.data.datasource

import com.dangjang.android.data.model.response.BaseResponse
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST

interface TokenApiService {
    //자동로그인 - 토큰 재발급
    @POST("/api/auth/reissue")
    suspend fun reissueToken(
        @Header("Authorization") accessToken: String
    ) : Response<BaseResponse<Nothing>>
}