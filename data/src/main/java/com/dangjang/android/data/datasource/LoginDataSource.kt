package com.dangjang.android.data.datasource

import com.dangjang.android.data.model.dto.AuthDto
import com.dangjang.android.data.model.request.LoginRequest
import com.dangjang.android.data.model.response.BaseResponse
import javax.inject.Inject

class LoginDataSource @Inject constructor(
    private val loginApiService: LoginApiService
) : LoginNetworkDataSource() {

    suspend fun kakaoLogin(fcmToken: String, accessToken: String): BaseResponse<AuthDto> {
        return checkResponse(loginApiService.kakaoLogin(fcmToken, LoginRequest(accessToken)))
    }

    suspend fun naverLogin(fcmToken: String, accessToken: String): BaseResponse<AuthDto> {
        return checkResponse(loginApiService.naverLogin(fcmToken, LoginRequest(accessToken)))
    }
}