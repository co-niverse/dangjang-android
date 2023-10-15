package com.dangjang.android.data.datasource

import com.dangjang.android.data.model.response.BaseResponse
import javax.inject.Inject

class TokenDataSource @Inject constructor(
    private val tokenApiService: TokenApiService
) : BaseNetworkDataSource() {

    suspend fun reissueToken(accessToken: String): BaseResponse<Nothing> {
        return checkResponse(tokenApiService.reissueToken(accessToken))
    }

    suspend fun postFcmToken(accessToken: String, fcmToken: String): BaseResponse<Nothing> {
        return checkResponse(tokenApiService.postFcmToken("Bearer $accessToken", fcmToken))
    }
}