package com.dangjang.android.domain.usecase

import com.dangjang.android.domain.model.HealthMetricVO
import com.dangjang.android.domain.repository.HomeRepository
import com.dangjang.android.domain.request.AddHealthMetricRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeUseCase @Inject constructor(
    private val homeRepository: HomeRepository
){
    suspend fun addHealthMetric(
        accessToken: String,
        addHealthMetricRequest: AddHealthMetricRequest
    ): Flow<HealthMetricVO> =
        withContext(Dispatchers.IO) {
        homeRepository.addHealthMetric(accessToken, addHealthMetricRequest)
    }
}