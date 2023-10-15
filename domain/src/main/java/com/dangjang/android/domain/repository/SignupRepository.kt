package com.dangjang.android.domain.repository

import com.dangjang.android.domain.model.DuplicateNicknameVO
import com.dangjang.android.domain.model.AuthVO
import com.dangjang.android.domain.requestVO.SignupRequestVO
import kotlinx.coroutines.flow.Flow

interface SignupRepository {

    //닉네임 중복 확인
    fun getDuplicateNickname(nickname: String): Flow<DuplicateNicknameVO>

    //회원가입
    fun signup(fcmToken: String, data: SignupRequestVO): Flow<AuthVO>
}