package com.dangjang.android.domain.repository

import com.dangjang.android.domain.model.GetMypageVO
import com.dangjang.android.domain.model.GetPointVO
import com.dangjang.android.domain.model.PostPointVO
import com.dangjang.android.domain.request.LogoutRequest
import com.dangjang.android.domain.request.PostPointRequest
import kotlinx.coroutines.flow.Flow

interface MypageRepository {

    //마이페이지 조회
    fun getMypage(accessToken: String): Flow<GetMypageVO>

    //포인트 조회
    fun getPoint(accessToken: String): Flow<GetPointVO>

    //포인트 상품 구매
    fun postPoint(accessToken: String, postPointRequest: PostPointRequest): Flow<PostPointVO>

    //로그아웃
    fun logout(accessToken: String, logoutRequest: LogoutRequest): Flow<Boolean>

    //회원탈퇴
    fun signout(accessToken: String): Flow<Boolean>
}