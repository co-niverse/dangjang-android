package com.dangjang.android.domain.repository

import kotlinx.coroutines.flow.Flow

interface TokenRepository {
    //자동 로그인 - 토큰 재발급
    fun reissueToken(accessToken: String): Flow<Boolean>

    //FCM 토큰 재발급
    fun postFcmToken(accessToken: String, fcmToken: String): Flow<Boolean>
}