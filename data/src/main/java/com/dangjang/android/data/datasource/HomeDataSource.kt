package com.dangjang.android.data.datasource

import com.dangjang.android.data.model.dto.GetGlucoseDto
import com.dangjang.android.data.model.dto.EditHealthMetricDto
import com.dangjang.android.data.model.dto.GetExerciseDto
import com.dangjang.android.data.model.dto.GetHomeDto
import com.dangjang.android.data.model.dto.GetNotificationDto
import com.dangjang.android.data.model.dto.GetWeightDto
import com.dangjang.android.data.model.dto.PostPatchExerciseDto
import com.dangjang.android.data.model.dto.PostPatchWeightDto
import com.dangjang.android.domain.request.AddHealthMetricRequest
import com.dangjang.android.data.model.response.BaseResponse
import com.dangjang.android.domain.request.EditHealthMetricRequest
import com.dangjang.android.domain.request.EditSameHealthMetricRequest
import javax.inject.Inject

class HomeDataSource @Inject constructor(
    private val homeApiService: HomeApiService
): BaseNetworkDataSource() {

    suspend fun getHome(accessToken: String, date: String): BaseResponse<GetHomeDto> {
        return checkResponse(homeApiService.getHome(accessToken, date))
    }

    suspend fun getNotification(accessToken: String): BaseResponse<GetNotificationDto> {
        return checkResponse(homeApiService.getNotification(accessToken))
    }
    suspend fun addHealthMetric(accessToken: String, addHealthMetricRequest: AddHealthMetricRequest): BaseResponse<EditHealthMetricDto> {
        return checkResponse(homeApiService.addHealthMetric(accessToken, addHealthMetricRequest))
    }

    suspend fun getGlucose(accessToken: String, date: String): BaseResponse<GetGlucoseDto> {
        return checkResponse(homeApiService.getGlucose(accessToken, date))
    }

    suspend fun editGlucose(accessToken: String, editHealthMetricRequest: EditHealthMetricRequest): BaseResponse<EditHealthMetricDto> {
        return checkResponse(homeApiService.editHealthMetric(accessToken, editHealthMetricRequest))
    }

    suspend fun editSameGlucose(accessToken: String, editSameHealthMetricRequest: EditSameHealthMetricRequest): BaseResponse<EditHealthMetricDto> {
        return checkResponse(homeApiService.editSameGlucose(accessToken, editSameHealthMetricRequest))
    }

    suspend fun getWeight(accessToken: String, date: String): BaseResponse<GetWeightDto> {
        return checkResponse(homeApiService.getWeight(accessToken, date))
    }

    suspend fun addWeight(accessToken: String, addHealthMetricRequest: AddHealthMetricRequest): BaseResponse<PostPatchWeightDto> {
        return checkResponse(homeApiService.addWeight(accessToken, addHealthMetricRequest))
    }

    suspend fun editWeight(accessToken: String, editSameHealthMetricRequest: EditSameHealthMetricRequest): BaseResponse<PostPatchWeightDto> {
        return checkResponse(homeApiService.editWeight(accessToken, editSameHealthMetricRequest))
    }

    suspend fun getExercise(accessToken: String, date: String): BaseResponse<GetExerciseDto> {
        return checkResponse(homeApiService.getExercise(accessToken, date))
    }

    suspend fun addExercise(accessToken: String, addHealthMetricRequest: AddHealthMetricRequest): BaseResponse<PostPatchExerciseDto> {
        return checkResponse(homeApiService.addExercise(accessToken, addHealthMetricRequest))
    }

    suspend fun editExercise(accessToken: String, editSameHealthMetricRequest: EditSameHealthMetricRequest): BaseResponse<PostPatchExerciseDto> {
        return checkResponse(homeApiService.editExercise(accessToken, editSameHealthMetricRequest))
    }
}