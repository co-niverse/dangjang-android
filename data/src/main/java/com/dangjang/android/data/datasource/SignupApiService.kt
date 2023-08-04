package com.dangjang.android.data.datasource

import com.dangjang.android.data.model.dto.DuplicateNicknameDto
import com.dangjang.android.data.model.request.DuplicateNicknameRequest
import com.dangjang.android.data.model.response.BaseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SignupApiService {

    //Duplicate Nickname API
    @GET("api/duplicateNickname")
    suspend fun getDuplicateNickname(
        @Query("nickname") nickname: DuplicateNicknameRequest
    ) : Response<BaseResponse<DuplicateNicknameDto>>
}