package com.dangjang.android.data.datasource

import com.dangjang.android.data.model.dto.IntroDto
import com.dangjang.android.domain.request.PostHealthConnectRequest
import com.dangjang.android.data.model.response.BaseResponse
import com.dangjang.android.domain.request.PatchHealthConnectRequest
import javax.inject.Inject

class HealthConnectDataSource @Inject constructor(
    private val healthConnectApiService: HealthConnectApiService
) : BaseNetworkDataSource() {

    suspend fun getIntroApi(): BaseResponse<IntroDto> {
        return checkResponse(healthConnectApiService.getIntroApi())
    }

    suspend fun postHealthConnect(
        accessToken: String,
        postHealthConnectRequest: PostHealthConnectRequest
    ): BaseResponse<Nothing> {
        return checkResponse(healthConnectApiService.postHealthConnect(accessToken, postHealthConnectRequest))
    }

    suspend fun patchHealthConnectInterlock(
        accessToken: String,
        patchHealthConnectRequest: PatchHealthConnectRequest
    ): BaseResponse<Nothing> {
        return checkResponse(healthConnectApiService.patchHealthConnectInterlock(accessToken, patchHealthConnectRequest))
    }
}