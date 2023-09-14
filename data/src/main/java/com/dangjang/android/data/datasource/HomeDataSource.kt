package com.dangjang.android.data.datasource

import com.dangjang.android.data.model.dto.GetGlucoseDto
import com.dangjang.android.data.model.dto.EditHealthMetricDto
import com.dangjang.android.data.model.dto.EditWeightExerciseDto
import com.dangjang.android.data.model.dto.PostPatchWeightDto
import com.dangjang.android.domain.request.AddHealthMetricRequest
import com.dangjang.android.data.model.response.BaseResponse
import com.dangjang.android.domain.request.EditHealthMetricRequest
import com.dangjang.android.domain.request.EditSameHealthMetricRequest
import javax.inject.Inject

class HomeDataSource @Inject constructor(
    private val homeApiService: HomeApiService
): BaseNetworkDataSource() {

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

    suspend fun addWeight(accessToken: String, addHealthMetricRequest: AddHealthMetricRequest): BaseResponse<PostPatchWeightDto> {
        return checkResponse(homeApiService.addWeight(accessToken, addHealthMetricRequest))
    }

    suspend fun editWeight(accessToken: String, editSameHealthMetricRequest: EditSameHealthMetricRequest): BaseResponse<EditWeightExerciseDto> {
        return checkResponse(homeApiService.editWeight(accessToken, editSameHealthMetricRequest))
    }
}