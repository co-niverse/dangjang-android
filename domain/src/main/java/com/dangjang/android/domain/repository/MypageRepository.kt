package com.dangjang.android.domain.repository

import com.dangjang.android.domain.model.GetMypageVO
import com.dangjang.android.domain.model.GetPointVO
import com.dangjang.android.domain.model.PostPointVO
import com.dangjang.android.domain.request.PostPointRequest
import kotlinx.coroutines.flow.Flow

interface MypageRepository {

    //마이페이지 조회
    fun getMypage(accessToken: String): Flow<GetMypageVO>

    //포인트 조회
    fun getPoint(accessToken: String): Flow<GetPointVO>

    //포인트 상품 구매
    fun postPoint(accessToken: String, postPointRequest: PostPointRequest): Flow<PostPointVO>
}