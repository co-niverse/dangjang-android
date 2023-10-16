package com.dangjang.android.data.datasource

import com.dangjang.android.data.model.dto.IntroDto
import com.dangjang.android.domain.request.PostHealthConnectRequest
import com.dangjang.android.data.model.response.BaseResponse
import com.dangjang.android.domain.request.PatchHealthConnectRequest
import javax.inject.Inject

class SplashDataSource @Inject constructor(
    private val splashApiService: SplashApiService
) : BaseNetworkDataSource() {

    suspend fun getIntroApi(): BaseResponse<IntroDto> {
        return checkResponse(splashApiService.getIntroApi())
    }

    suspend fun postHealthConnect(
        accessToken: String,
        postHealthConnectRequest: PostHealthConnectRequest
    ): BaseResponse<Nothing> {
        return checkResponse(splashApiService.postHealthConnect(accessToken, postHealthConnectRequest))
    }

    suspend fun patchHealthConnectInterlock(
        accessToken: String,
        patchHealthConnectRequest: PatchHealthConnectRequest
    ): BaseResponse<Nothing> {
        return checkResponse(splashApiService.patchHealthConnectInterlock(accessToken, patchHealthConnectRequest))
    }
}