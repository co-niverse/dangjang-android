package com.dangjang.android.data.repository

import com.dangjang.android.data.datasource.SignupDataSource
import com.dangjang.android.data.model.request.SignupRequest
import com.dangjang.android.domain.model.DuplicateNicknameVO
import com.dangjang.android.domain.model.AuthVO
import com.dangjang.android.domain.repository.SignupRepository
import com.dangjang.android.domain.requestVO.SignupRequestVO
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

    override fun signup(fcmToken: String, data: SignupRequestVO): Flow<AuthVO> = flow {
        val response = signupDataScurce.signup(fcmToken, SignupRequest(
            data.accessToken,
            data.provider,
            data.nickname,
            data.gender,
            data.birthday,
            data.height,
            data.weight,
            data.activityAmount,
            data.diabetes,
            data.diabetes_year,
            data.medicine,
            data.injection,
            data.diseases
        ))
        emit(response.data.toDomain())
    }

}