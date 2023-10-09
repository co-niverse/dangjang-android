package com.dangjang.android.domain.usecase

import com.dangjang.android.domain.repository.TokenRepository
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

}