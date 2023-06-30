package com.dangjang.android.domain.usecase

import com.dangjang.android.domain.model.LoginVO
import com.dangjang.android.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class GetLoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(accessToken: String): Flow<LoginVO> = channelFlow {
        loginRepository.login(accessToken).collectLatest {
            LoginVO(it.token)
            send(it)
        }
    }
}