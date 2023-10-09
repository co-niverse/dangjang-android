package com.dangjang.android.data.datasource

import com.dangjang.android.data.model.dto.EditHealthMetricDto
import com.dangjang.android.data.model.dto.GetMypageDto
import com.dangjang.android.data.model.dto.GetPointDto
import com.dangjang.android.data.model.dto.PostPointDto
import com.dangjang.android.data.model.response.BaseResponse
import com.dangjang.android.domain.request.AddHealthMetricRequest
import com.dangjang.android.domain.request.PostPointRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface MypageApiService {

    //마이페이지 조회 API
    @GET("api/user/mypage")
    suspend fun getMypage(
        @Header("Authorization") accessToken: String
    ) : Response<BaseResponse<GetMypageDto>>

    //포인트 조회 API
    @GET("api/point")
    suspend fun getPoint(
        @Header("Authorization") accessToken: String
    ) : Response<BaseResponse<GetPointDto>>

    //포인트 상품 구매 API
    @POST("api/point")
    suspend fun postPoint(
        @Header("Authorization") accessToken: String,
        @Body postPointRequest: PostPointRequest
    ) : Response<BaseResponse<PostPointDto>>

    //로그아웃
    @GET("api/auth/logout")
    suspend fun logout(
        @Header("Authorization") accessToken: String,
        @Header("FcmToken") fcmToken: String
    ) : Response<BaseResponse<Nothing>>

    //회원탈퇴
    @DELETE("api/user/withdrawal")
    suspend fun signout(
        @Header("Authorization") accessToken: String
    ) : Response<BaseResponse<Nothing>>

}