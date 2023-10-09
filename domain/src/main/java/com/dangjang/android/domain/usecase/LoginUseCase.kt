package com.dangjang.android.domain.usecase

import com.dangjang.android.domain.model.AuthVO
import com.dangjang.android.domain.repository.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    suspend fun kakoLogin(fcmToken: String, accessToken: String): Flow<AuthVO> =
        withContext(Dispatchers.IO) {
            loginRepository.kakaoLogin(fcmToken, accessToken)
        }

    suspend fun naverLogin(fcmToken: String, accessToken: String): Flow<AuthVO> =
        withContext(Dispatchers.IO) {
            loginRepository.naverLogin(fcmToken, accessToken)
        }

}