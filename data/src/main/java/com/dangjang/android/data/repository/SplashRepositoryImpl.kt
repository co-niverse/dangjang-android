package com.dangjang.android.data.repository

import com.dangjang.android.data.datasource.SplashDataSource
import com.dangjang.android.domain.request.PostHealthConnectRequest
import com.dangjang.android.domain.model.IntroVO
import com.dangjang.android.domain.repository.SplashRepository
import com.dangjang.android.domain.request.PatchHealthConnectRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SplashRepositoryImpl @Inject constructor(
    private val splashDataSource: SplashDataSource
) : SplashRepository {
    override suspend fun getIntroApi(): Flow<IntroVO> = flow {
        val response = splashDataSource.getIntroApi()
        emit(response.data.toDomain())
    }

    override suspend fun postHealthConnect(accessToken: String, postHealthConnectRequest: PostHealthConnectRequest): Flow<Nothing> = flow {
        val response = splashDataSource.postHealthConnect(accessToken, postHealthConnectRequest)
        emit(response.data)
    }

    override suspend fun patchHealthConnectInterlock(accessToken: String, patchHealthConnectRequest: PatchHealthConnectRequest): Flow<Boolean> = flow {
        val response = splashDataSource.patchHealthConnectInterlock(accessToken, patchHealthConnectRequest)
        emit(response.success)
    }
}