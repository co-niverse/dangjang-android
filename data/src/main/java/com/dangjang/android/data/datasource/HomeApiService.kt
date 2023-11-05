package com.dangjang.android.data.datasource

import com.dangjang.android.data.model.dto.GetGlucoseDto
import com.dangjang.android.data.model.dto.EditHealthMetricDto
import com.dangjang.android.data.model.dto.EditWeightExerciseDto
import com.dangjang.android.data.model.dto.GetExerciseDto
import com.dangjang.android.data.model.dto.GetHomeDto
import com.dangjang.android.data.model.dto.GetNotificationDto
import com.dangjang.android.data.model.dto.GetWeightDto
import com.dangjang.android.data.model.dto.PostPatchExerciseDto
import com.dangjang.android.data.model.dto.PostPatchWeightDto
import com.dangjang.android.domain.request.AddHealthMetricRequest
import com.dangjang.android.data.model.response.BaseResponse
import com.dangjang.android.domain.request.CheckNotificationRequest
import com.dangjang.android.domain.request.EditHealthMetricRequest
import com.dangjang.android.domain.request.EditSameHealthMetricRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface HomeApiService {

    //홈 조회 API
    @GET("/api/guide")
    suspend fun getHome(
        @Header("Authorization") accessToken: String,
        @Query("date") date: String
    ) : Response<BaseResponse<GetHomeDto>>

    //알람 목록 조회 API
    @GET("/api/notification")
    suspend fun getNotification(
        @Header("Authorization") accessToken: String
    ) : Response<BaseResponse<GetNotificationDto>>

    //알람 확인 체크 API
    @PATCH("/api/notification")
    suspend fun checkNotification(
        @Header("Authorization") accessToken: String,
        @Body checkNotificationRequest: CheckNotificationRequest
    ): Response<BaseResponse<Nothing>>

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

    //체중 조회 API
    @GET("api/guide/weight")
    suspend fun getWeight(
        @Header("Authorization") accessToken: String,
        @Query("date") date: String
    ) : Response<BaseResponse<GetWeightDto>>

    // 체중 추가 API
    @POST("/api/health-metric")
    suspend fun addWeight(
        @Header("Authorization") accessToken: String,
        @Body addHealthMetricRequest: AddHealthMetricRequest
    ) : Response<BaseResponse<PostPatchWeightDto>>

    //체중 수정 API
    @PATCH("api/health-metric")
    suspend fun editWeight(
        @Header("Authorization") accessToken: String,
        @Body editSameHealthMetricRequest: EditSameHealthMetricRequest
    ) : Response<BaseResponse<PostPatchWeightDto>>

    // 운동 조회 API
    @GET("api/guide/exercise")
    suspend fun getExercise(
        @Header("Authorization") accessToken: String,
        @Query("date") date: String
    ) : Response<BaseResponse<GetExerciseDto>>

    // 운동 추가 API
    @POST("/api/health-metric")
    suspend fun addExercise(
        @Header("Authorization") accessToken: String,
        @Body addHealthMetricRequest: AddHealthMetricRequest
    ) : Response<BaseResponse<PostPatchExerciseDto>>

    //운동 수정 API
    @PATCH("/api/health-metric")
    suspend fun editExercise(
        @Header("Authorization") accessToken: String,
        @Body editSameHealthMetricRequest: EditSameHealthMetricRequest
    ) : Response<BaseResponse<PostPatchExerciseDto>>

    //건강지표 삭제 API
    @DELETE("/api/health-metric")
    suspend fun deleteHealthMetric(
        @Header("Authorization") accessToken: String,
        @Query("date") date: String,
        @Query("type") type: String
    ) : Response<BaseResponse<Nothing>>
}