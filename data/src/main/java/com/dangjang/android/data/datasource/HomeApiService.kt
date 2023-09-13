package com.dangjang.android.data.datasource

import com.dangjang.android.data.model.dto.GetGlucoseDto
import com.dangjang.android.data.model.dto.PostPatchGlucoseDto
import com.dangjang.android.domain.request.AddHealthMetricRequest
import com.dangjang.android.data.model.response.BaseResponse
import com.dangjang.android.domain.request.EditHealthMetricRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface HomeApiService {

    //혈당 등록 API
    @POST("/api/health-metric")
    suspend fun addHealthMetric(
        @Header("Authorization") accessToken: String,
        @Body addHealthMetricRequest: AddHealthMetricRequest
    ) : Response<BaseResponse<PostPatchGlucoseDto>>

    //혈당 전체 조회 API
    @GET("/api/guide/blood-sugar")
    suspend fun getGlucose(
        @Header("Authorization") accessToken: String,
        @Query("date") date: String
    ) : Response<BaseResponse<GetGlucoseDto>>

    //혈당 수정 API
    @PATCH("api/health-metric")
    suspend fun editHealthMetric(
        @Header("Authorization") accessToken: String,
        @Body editHealthMetricRequest: EditHealthMetricRequest
    ) : Response<BaseResponse<PostPatchGlucoseDto>>
}