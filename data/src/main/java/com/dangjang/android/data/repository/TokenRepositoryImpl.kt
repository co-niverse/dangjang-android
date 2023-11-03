package com.dangjang.android.data.repository

import com.dangjang.android.data.datasource.TokenDataSource
import com.dangjang.android.domain.repository.TokenRepository
import com.dangjang.android.domain.request.PostFcmTokenRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val tokenDataSource: TokenDataSource
) : TokenRepository {

    override fun reissueToken(accessToken: String): Flow<Boolean> = flow {
        val response = tokenDataSource.reissueToken(accessToken)
        emit(response.success)
    }

    override fun postFcmToken(accessToken: String, postFcmTokenRequest: PostFcmTokenRequest): Flow<Boolean> = flow {
        val response = tokenDataSource.postFcmToken(accessToken, postFcmTokenRequest)
        emit(response.success)
    }
}