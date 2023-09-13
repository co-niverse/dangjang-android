package com.dangjang.android.data.repository

import com.dangjang.android.data.datasource.SplashDataSource
import com.dangjang.android.domain.model.IntroVO
import com.dangjang.android.domain.repository.SplashRepository
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
}