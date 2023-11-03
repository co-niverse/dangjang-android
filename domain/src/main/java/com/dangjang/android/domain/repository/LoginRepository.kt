package com.dangjang.android.domain.repository

import com.dangjang.android.domain.model.AuthVO
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    //kakaoLogin API
    suspend fun kakaoLogin(accessToken: String): Flow<AuthVO>
    //naverLogin API
    suspend fun naverLogin(accessToken: String): Flow<AuthVO>
}