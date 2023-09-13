package com.dangjang.android.data.repository

import com.dangjang.android.data.datasource.HomeDataSource
import com.dangjang.android.domain.model.GetGlucoseVO
import com.dangjang.android.domain.model.PostPatchGlucoseVO
import com.dangjang.android.domain.repository.HomeRepository
import com.dangjang.android.domain.request.AddHealthMetricRequest
import com.dangjang.android.domain.request.EditHealthMetricRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeDataSource: HomeDataSource
) : HomeRepository {
    override fun addHealthMetric(accessToken: String, addHealthMetricRequest: AddHealthMetricRequest): Flow<PostPatchGlucoseVO> = flow {
        val response = homeDataSource.addHealthMetric(accessToken, addHealthMetricRequest)
        emit(response.data.toDomain())
    }

    override fun getGlucose(accessToken: String, date: String): Flow<GetGlucoseVO> = flow {
        val response = homeDataSource.getGlucose(accessToken, date)
        emit(response.data.toDomain())
    }

    override fun editGlucose(
        accessToken: String,
        editHealthMetricRequest: EditHealthMetricRequest
    ): Flow<PostPatchGlucoseVO> = flow {
        val response = homeDataSource.editGlucose(accessToken, editHealthMetricRequest)
        emit(response.data.toDomain())
    }
}