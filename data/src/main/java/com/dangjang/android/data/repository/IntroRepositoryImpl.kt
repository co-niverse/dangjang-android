package com.dangjang.android.data.repository

import android.util.Log
import com.dangjang.android.data.datasource.IntroDataSource
import com.dangjang.android.domain.model.IntroVO
import com.dangjang.android.domain.repository.IntroRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class IntroRepositoryImpl @Inject constructor(
    private val introDataSource: IntroDataSource
) : IntroRepository {
    override fun getIntroApi(): Flow<IntroVO> = flow {
        val response = introDataSource.getIntroApi()
        if (response.isSuccessful) {
            if (response.body()!!.success) {
                emit(response.body()!!.data.toDomain())
            }
            Log.e("[SUCCESS]","Intro API" + response.body().toString())
        } else {
            Log.e("[FAIL]","Intro API" + response.errorBody()?.string()!!)
        }
    }

}