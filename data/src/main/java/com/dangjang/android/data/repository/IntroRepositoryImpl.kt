package com.dangjang.android.data.repository

import android.util.Log
import com.dangjang.android.data.datasource.IntroDataSource
import com.dangjang.android.domain.model.IntroVO
import com.dangjang.android.domain.repository.IntroRepository
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class IntroRepositoryImpl @Inject constructor(
    private val introDataSource: IntroDataSource
) : IntroRepository {

    override fun getIntroApi(): Flow<IntroVO> = flow {
        val response = introDataSource.getIntroApi()
        response.suspendOnSuccess {
                emit(
                    data.data.toDomain()
                )
            Log.e("[SUCCESS]","Intro API")
        }.onFailure { Log.e("[ERROR]","Intro API") }
    }

}