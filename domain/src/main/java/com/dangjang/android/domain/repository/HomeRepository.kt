package com.dangjang.android.domain.repository

import com.dangjang.android.domain.model.HealthMetricVO
import com.dangjang.android.domain.request.AddHealthMetricRequest
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    //건강지표 등록
    fun addHealthMetric(addHealthMetricRequest: AddHealthMetricRequest): Flow<HealthMetricVO>
}