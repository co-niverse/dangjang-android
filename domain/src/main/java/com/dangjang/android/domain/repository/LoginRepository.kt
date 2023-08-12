package com.dangjang.android.domain.repository

import com.dangjang.android.domain.model.LoginVO
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    //kakaoLogin API
    suspend fun kakaoLogin(accessToken: String): Flow<LoginVO>
    //naverLogin API
    suspend fun naverLogin(accessToken: String): Flow<LoginVO>
}