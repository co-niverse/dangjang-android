package com.dangjang.android.domain.repository

import com.dangjang.android.domain.model.GetMypageVO
import kotlinx.coroutines.flow.Flow

interface MypageRepository {

    //마이페이지 조회
    fun getMypage(accessToken: String): Flow<GetMypageVO>
}