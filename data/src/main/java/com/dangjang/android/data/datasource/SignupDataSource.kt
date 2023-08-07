package com.dangjang.android.data.datasource

import com.dangjang.android.data.model.dto.DuplicateNicknameDto
import com.dangjang.android.data.model.dto.SignupDto
import com.dangjang.android.data.model.request.DuplicateNicknameRequest
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
        data: SignupRequest
    ): BaseResponse<SignupDto> {
        return checkResponse(signupApiService.signup(data))
    }
}