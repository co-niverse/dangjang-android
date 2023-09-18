package com.dangjang.android.data.datasource

import com.dangjang.android.data.model.dto.GetChartDto
import com.dangjang.android.data.model.response.BaseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ChartApiService {

    //건강차트 조회 API
    @GET("/api/health-metric")
    suspend fun getChart(
        @Header("Authorization") accessToken: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ) : Response<BaseResponse<GetChartDto>>

}