package com.dangjang.android.domain.usecase

import com.dangjang.android.domain.model.IntroVO
import com.dangjang.android.domain.repository.SplashRepository
import com.dangjang.android.domain.request.PostHealthConnectRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SplashUseCase @Inject constructor(
    private val splashRepository: SplashRepository
) {
    suspend fun getIntro(): Flow<IntroVO> =
        withContext(Dispatchers.IO) {
            splashRepository.getIntroApi()
        }

    suspend fun postHealthConnect(accessToken: String, postHealthConnectRequest: PostHealthConnectRequest): Flow<Nothing> =
        withContext(Dispatchers.IO) {
            splashRepository.postHealthConnect(accessToken, postHealthConnectRequest)
        }
}