package com.dangjang.android.data.repository

import com.dangjang.android.data.datasource.HealthConnectDataSource
import com.dangjang.android.domain.request.PostHealthConnectRequest
import com.dangjang.android.domain.model.IntroVO
import com.dangjang.android.domain.repository.HealthConnectRepository
import com.dangjang.android.domain.request.PatchHealthConnectRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HealthConnectRepositoryImpl @Inject constructor(
    private val healthConnectDataSource: HealthConnectDataSource
) : HealthConnectRepository {
    override suspend fun getIntroApi(): Flow<IntroVO> = flow {
        val response = healthConnectDataSource.getIntroApi()
        emit(response.data.toDomain())
    }

    override suspend fun postHealthConnect(accessToken: String, postHealthConnectRequest: PostHealthConnectRequest): Flow<Boolean> = flow {
        val response = healthConnectDataSource.postHealthConnect(accessToken, postHealthConnectRequest)
        emit(response.success)
    }

    override suspend fun patchHealthConnectInterlock(accessToken: String, patchHealthConnectRequest: PatchHealthConnectRequest): Flow<Boolean> = flow {
        val response = healthConnectDataSource.patchHealthConnectInterlock(accessToken, patchHealthConnectRequest)
        emit(response.success)
    }
}