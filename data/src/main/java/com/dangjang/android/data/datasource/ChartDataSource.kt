package com.dangjang.android.data.datasource

import com.dangjang.android.data.model.dto.GetChartDto
import com.dangjang.android.data.model.response.BaseResponse
import javax.inject.Inject

class ChartDataSource @Inject constructor(
    private val chartApiService: ChartApiService
): BaseNetworkDataSource() {

    suspend fun getChart(accessToken: String, startDate: String, endDate: String): BaseResponse<GetChartDto> {
        return checkResponse(chartApiService.getChart(accessToken, startDate, endDate))
    }

}