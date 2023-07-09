package com.dangjang.android.domain.usecase

import com.dangjang.android.domain.model.LoginVO
import com.dangjang.android.domain.repository.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetLoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    suspend fun kakoLogin(accessToken: String): Flow<LoginVO> =
        withContext(Dispatchers.IO) {
            loginRepository.kakaoLogin(accessToken)
        }

    suspend fun naverLogin(accessToken: String): Flow<LoginVO> =
        withContext(Dispatchers.IO) {
            loginRepository.naverLogin(accessToken)
        }

}