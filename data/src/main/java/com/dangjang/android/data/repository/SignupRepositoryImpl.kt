package com.dangjang.android.data.repository

import com.dangjang.android.data.datasource.SignupDataSource
import com.dangjang.android.domain.model.DuplicateNicknameVO
import com.dangjang.android.domain.repository.SignupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignupRepositoryImpl @Inject constructor(
    private val signupDataScurce: SignupDataSource
) : SignupRepository {

    override fun getDuplicateNickname(nickname: String): Flow<DuplicateNicknameVO> = flow {
        val response = signupDataScurce.getDuplicateNickname(nickname)
        emit(response.data.toDomain())
    }
}