package com.dangjang.android.data.datasource

import com.dangjang.android.data.model.dto.AuthDto
import com.dangjang.android.data.model.request.LoginRequest
import com.dangjang.android.data.model.response.BaseResponse
import javax.inject.Inject

class LoginDataSource @Inject constructor(
    private val loginApiService: LoginApiService
) : BaseNetworkDataSource() {

    suspend fun kakaoLogin(accessToken: String): BaseResponse<AuthDto> {
        return checkResponse(loginApiService.kakaoLogin(LoginRequest(accessToken)))
    }

    suspend fun naverLogin(accessToken: String): BaseResponse<AuthDto> {
        return checkResponse(loginApiService.naverLogin(LoginRequest(accessToken)))
    }
}