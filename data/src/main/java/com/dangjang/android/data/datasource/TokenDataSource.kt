package com.dangjang.android.data.datasource

import com.dangjang.android.data.model.response.BaseResponse
import javax.inject.Inject

class TokenDataSource @Inject constructor(
    private val tokenApiService: TokenApiService
) : BaseNetworkDataSource() {

    suspend fun reissueToken(accessToken: String): BaseResponse<Nothing> {
        return checkResponse(tokenApiService.reissueToken(accessToken))
    }
}