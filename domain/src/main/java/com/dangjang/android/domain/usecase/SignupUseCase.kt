package com.dangjang.android.domain.usecase

import com.dangjang.android.domain.model.DuplicateNicknameVO
import com.dangjang.android.domain.model.AuthVO
import com.dangjang.android.domain.repository.SignupRepository
import com.dangjang.android.domain.requestVO.SignupRequestVO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignupUseCase @Inject constructor(
    private val signupRepository: SignupRepository
) {

    suspend fun getDuplicateNickname(nickname: String): Flow<DuplicateNicknameVO> =
        withContext(Dispatchers.IO) {
            signupRepository.getDuplicateNickname(nickname)
        }

    suspend fun signup(
        fcmToken: String,
        data: SignupRequestVO
    ): Flow<AuthVO> =
        withContext(Dispatchers.IO) {
            signupRepository.signup(fcmToken, data)
        }

}