package com.dangjang.android.data.datasource

import com.dangjang.android.data.model.dto.AuthDto
import com.dangjang.android.data.model.dto.DuplicateNicknameDto
import com.dangjang.android.data.model.request.SignupRequest
import com.dangjang.android.data.model.response.BaseResponse
import javax.inject.Inject

class SignupDataSource @Inject constructor(
    private val signupApiService: SignupApiService
) : BaseNetworkDataSource() {

    suspend fun getDuplicateNickname(nickname: String): BaseResponse<DuplicateNicknameDto> {
        return checkResponse(signupApiService.getDuplicateNickname(nickname))
    }

    suspend fun signup(
        fcmToken: String,
        data: SignupRequest
    ): BaseResponse<AuthDto> {
        return checkResponse(signupApiService.signup(fcmToken, data))
    }
}