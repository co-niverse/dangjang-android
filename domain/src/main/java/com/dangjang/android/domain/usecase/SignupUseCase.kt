package com.dangjang.android.domain.usecase

import com.dangjang.android.domain.model.DuplicateNicknameVO
import com.dangjang.android.domain.repository.SignupRepository
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

}