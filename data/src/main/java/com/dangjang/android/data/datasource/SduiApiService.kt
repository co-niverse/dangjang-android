package com.dangjang.android.data.datasource

import com.dangjang.android.data.model.response.BaseResponse
import com.dangjang.android.data.sdui.SduiSignupDto
import com.dangjang.android.domain.sdui.SduiCommonVO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface SduiApiService {

    //Sdui 회원가입
    @GET("/api/sdui/signup")
    suspend fun getSduiSignup() : Response<BaseResponse<SduiSignupDto>>
}