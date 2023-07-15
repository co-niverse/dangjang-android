package com.dangjang.android.data.repository

import com.dangjang.android.data.datasource.IntroDataSource
import com.dangjang.android.domain.repository.IntroRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class IntroRepositoryImpl @Inject constructor(
    private val introDataSource: IntroDataSource
) : IntroRepository {
    override suspend fun getIntroApi(): Flow<Any> = flow {
        try {
            val response = introDataSource.getIntroApi()
            if (response.success)
                emit(response.data.toDomain())
        } catch (e:Exception) {
            emit(e)
        }
    }

}