package com.dangjang.android.data.repository

import com.dangjang.android.data.datasource.LoginDataSource
import com.dangjang.android.domain.model.AuthVO
import com.dangjang.android.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginDataSource: LoginDataSource
) : LoginRepository {

    override suspend fun kakaoLogin(fcmToken: String, accessToken: String): Flow<AuthVO> = flow {
        val response = loginDataSource.kakaoLogin(fcmToken, accessToken)
        emit(response.data.toDomain())
    }

    override suspend fun naverLogin(fcmToken: String, accessToken: String): Flow<AuthVO> = flow {
        val response = loginDataSource.naverLogin(fcmToken, accessToken)
        emit(response.data.toDomain())
    }

}