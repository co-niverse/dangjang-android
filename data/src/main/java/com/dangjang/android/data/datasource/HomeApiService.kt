package com.dangjang.android.data.datasource

import com.dangjang.android.data.model.dto.HealthMetricDto
import com.dangjang.android.domain.request.AddHealthMetricRequest
import com.dangjang.android.data.model.response.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface HomeApiService {

    //건강지표 등록 API
    @POST("/api/health-metric")
    suspend fun addHealthMetric(
        @Header("Authorization") accessToken: String,
        @Body addHealthMetricRequest: AddHealthMetricRequest
    ) : Response<BaseResponse<HealthMetricDto>>
}