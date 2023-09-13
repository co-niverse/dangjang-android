package com.dangjang.android.data.datasource

import com.dangjang.android.data.model.dto.GetGlucoseDto
import com.dangjang.android.data.model.dto.HealthMetricDto
import com.dangjang.android.domain.request.AddHealthMetricRequest
import com.dangjang.android.data.model.response.BaseResponse
import javax.inject.Inject

class HomeDataSource @Inject constructor(
    private val homeApiService: HomeApiService
): BaseNetworkDataSource() {

    suspend fun addHealthMetric(accessToken: String, addHealthMetricRequest: AddHealthMetricRequest): BaseResponse<HealthMetricDto> {
        return checkResponse(homeApiService.addHealthMetric(accessToken, addHealthMetricRequest))
    }

    suspend fun getGlucose(accessToken: String, date: String): BaseResponse<GetGlucoseDto> {
        return checkResponse(homeApiService.getGlucose(accessToken, date))
    }
}