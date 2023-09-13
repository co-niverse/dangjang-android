package com.dangjang.android.domain.repository

import com.dangjang.android.domain.model.GetGlucoseVO
import com.dangjang.android.domain.model.HealthMetricVO
import com.dangjang.android.domain.request.AddHealthMetricRequest
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    //건강지표 등록
    fun addHealthMetric(accessToken: String, addHealthMetricRequest: AddHealthMetricRequest): Flow<HealthMetricVO>

    //혈당 조회
    fun getGlucose(accessToken: String, date: String): Flow<GetGlucoseVO>
}