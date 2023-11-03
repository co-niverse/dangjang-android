package com.dangjang.android.data.datasource

import com.dangjang.android.data.model.dto.AuthDto
import com.dangjang.android.data.model.dto.DuplicateNicknameDto
import com.dangjang.android.data.model.request.SignupRequest
import com.dangjang.android.data.model.response.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface SignupApiService {

    //Duplicate Nickname API
    @GET("api/user/duplicateNickname")
    suspend fun getDuplicateNickname(
        @Query("nickname") nickname: String
    ) : Response<BaseResponse<DuplicateNicknameDto>>

    //Signup API
    @POST("api/signup")
    suspend fun signup(
        @Body data: SignupRequest
    ) : Response<BaseResponse<AuthDto>>
}