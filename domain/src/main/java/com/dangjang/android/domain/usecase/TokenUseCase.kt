package com.dangjang.android.domain.usecase

import com.dangjang.android.domain.repository.TokenRepository
import com.dangjang.android.domain.request.PostFcmTokenRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {

    suspend fun reissueToken(accessToken: String): Flow<Boolean> =
        withContext(Dispatchers.IO) {
            tokenRepository.reissueToken(accessToken)
        }

    suspend fun postFcmToken(accessToken: String, postFcmTokenRequest: PostFcmTokenRequest): Flow<Boolean> =
        withContext(Dispatchers.IO) {
            tokenRepository.postFcmToken(accessToken, postFcmTokenRequest)
        }

}