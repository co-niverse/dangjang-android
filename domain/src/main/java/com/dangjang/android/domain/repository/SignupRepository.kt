package com.dangjang.android.domain.repository

import com.dangjang.android.domain.model.DuplicateNicknameVO
import kotlinx.coroutines.flow.Flow

interface SignupRepository {

    //닉네임 중복 확인
    fun getDuplicateNickname(nickname: String): Flow<DuplicateNicknameVO>
}