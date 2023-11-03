package com.dangjang.android.domain.usecase

import com.dangjang.android.domain.model.GetMypageVO
import com.dangjang.android.domain.model.GetPointVO
import com.dangjang.android.domain.model.PostPointVO
import com.dangjang.android.domain.repository.MypageRepository
import com.dangjang.android.domain.request.LogoutRequest
import com.dangjang.android.domain.request.PostPointRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MypageUseCase @Inject constructor(
    private val mypageRepository: MypageRepository
) {

    suspend fun getMypage(accessToken: String): Flow<GetMypageVO> =
        withContext(Dispatchers.IO) {
            mypageRepository.getMypage(accessToken)
        }

    suspend fun getPoint(accessToken: String): Flow<GetPointVO> =
        withContext(Dispatchers.IO) {
            mypageRepository.getPoint(accessToken)
        }

    suspend fun postPoint(accessToken: String, postPointRequest: PostPointRequest): Flow<PostPointVO> =
        withContext(Dispatchers.IO) {
            mypageRepository.postPoint(accessToken, postPointRequest)
        }

    suspend fun logout(accessToken: String, logoutRequest: LogoutRequest): Flow<Boolean> =
        withContext(Dispatchers.IO) {
            mypageRepository.logout(accessToken, logoutRequest)
        }

    suspend fun signout(accessToken: String): Flow<Boolean> =
        withContext(Dispatchers.IO) {
            mypageRepository.signout(accessToken)
        }
}