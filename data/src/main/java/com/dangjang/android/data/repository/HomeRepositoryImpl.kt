package com.dangjang.android.data.repository

import com.dangjang.android.data.datasource.HomeDataSource
import com.dangjang.android.domain.model.GetGlucoseVO
import com.dangjang.android.domain.model.EditHealthMetricVO
import com.dangjang.android.domain.model.EditWeightExerciseVO
import com.dangjang.android.domain.model.PostWeightVO
import com.dangjang.android.domain.repository.HomeRepository
import com.dangjang.android.domain.request.AddHealthMetricRequest
import com.dangjang.android.domain.request.EditHealthMetricRequest
import com.dangjang.android.domain.request.EditSameHealthMetricRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeDataSource: HomeDataSource
) : HomeRepository {
    override fun addHealthMetric(accessToken: String, addHealthMetricRequest: AddHealthMetricRequest): Flow<EditHealthMetricVO> = flow {
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
    ): Flow<EditHealthMetricVO> = flow {
        val response = homeDataSource.editGlucose(accessToken, editHealthMetricRequest)
        emit(response.data.toDomain())
    }

    override fun editSameGlucose(
        accessToken: String,
        editSameHealthMetricRequest: EditSameHealthMetricRequest
    ): Flow<EditHealthMetricVO> = flow {
        val response = homeDataSource.editSameGlucose(accessToken, editSameHealthMetricRequest)
        emit(response.data.toDomain())
    }

    override fun addWeight(accessToken: String, addHealthMetricRequest: AddHealthMetricRequest): Flow<PostWeightVO> = flow {
        val response = homeDataSource.addWeight(accessToken, addHealthMetricRequest)
        emit(response.data.toDomain())
    }

    override fun editWeight(accessToken: String, editSameHealthMetricRequest: EditSameHealthMetricRequest): Flow<EditWeightExerciseVO> = flow {
        val response = homeDataSource.editWeight(accessToken, editSameHealthMetricRequest)
        emit(response.data.toDomain())
    }
}