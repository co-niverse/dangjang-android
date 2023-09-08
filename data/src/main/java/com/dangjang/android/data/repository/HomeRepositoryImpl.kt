package com.dangjang.android.data.repository

import com.dangjang.android.data.datasource.HomeDataSource
import com.dangjang.android.domain.model.HealthMetricVO
import com.dangjang.android.domain.repository.HomeRepository
import com.dangjang.android.domain.request.AddHealthMetricRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeDataSource: HomeDataSource
) : HomeRepository {
    override fun addHealthMetric(accessToken: String, addHealthMetricRequest: AddHealthMetricRequest): Flow<HealthMetricVO> = flow {
        val response = homeDataSource.addHealthMetric(accessToken, addHealthMetricRequest)
        emit(response.data.toDomain())
    }
}