package com.dangjang.android.data.repository

import android.util.Log
import com.dangjang.android.data.datasource.LoginDataSource
import com.dangjang.android.data.model.request.LoginRequest
import com.dangjang.android.domain.model.LoginVO
import com.dangjang.android.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginDataSource: LoginDataSource
) : LoginRepository {

    override suspend fun kakaoLogin(accessToken: String): Flow<LoginVO> = flow {
        val response = loginDataSource.kakaoLogin(accessToken)
        emit(response.data.toDomain())
    }

    override suspend fun naverLogin(accessToken: String): Flow<LoginVO> = flow {
        val response = loginDataSource.naverLogin(accessToken)
        emit(response.data.toDomain())
    }

}