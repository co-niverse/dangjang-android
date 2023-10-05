package com.dangjang.android.data.datasource

import com.dangjang.android.data.model.dto.GetMypageDto
import com.dangjang.android.data.model.response.BaseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface MypageApiService {

    //마이페이지 조회 API
    @GET("api/user/mypage")
    suspend fun getMypage(
        @Header("Authorization") accessToken: String
    ) : Response<BaseResponse<GetMypageDto>>
}