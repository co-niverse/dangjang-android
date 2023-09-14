package com.dangjang.android.data.datasource

import com.dangjang.android.data.model.dto.GetGlucoseDto
import com.dangjang.android.data.model.dto.EditHealthMetricDto
import com.dangjang.android.data.model.dto.EditWeightExerciseDto
import com.dangjang.android.data.model.dto.PostWeightDto
import com.dangjang.android.domain.request.AddHealthMetricRequest
import com.dangjang.android.data.model.response.BaseResponse
import com.dangjang.android.domain.request.EditHealthMetricRequest
import com.dangjang.android.domain.request.EditSameHealthMetricRequest
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
    ) : Response<BaseResponse<EditHealthMetricDto>>

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
    ) : Response<BaseResponse<EditHealthMetricDto>>

    //혈당 수정 API (같은 시간 type일 경우)
    @PATCH("api/health-metric")
    suspend fun editSameGlucose(
        @Header("Authorization") accessToken: String,
        @Body editSameHealthMetricRequest: EditSameHealthMetricRequest
    ) : Response<BaseResponse<EditHealthMetricDto>>

    // 체중 추가 API
    @POST("/api/health-metric")
    suspend fun addWeight(
        @Header("Authorization") accessToken: String,
        @Body addHealthMetricRequest: AddHealthMetricRequest
    ) : Response<BaseResponse<PostWeightDto>>

    //체중 수정 API
    @PATCH("api/health-metric")
    suspend fun editWeight(
        @Header("Authorization") accessToken: String,
        @Body editSameHealthMetricRequest: EditSameHealthMetricRequest
    ) : Response<BaseResponse<EditWeightExerciseDto>>

}