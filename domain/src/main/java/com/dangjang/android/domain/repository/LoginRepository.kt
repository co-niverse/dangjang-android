package com.dangjang.android.domain.repository

import com.dangjang.android.domain.model.LoginVO
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    //kakaoLogin API
    fun kakaoLogin(accessToken: String): Flow<LoginVO>
    //naverLogin API
    fun naverLogin(accessToken: String): Flow<LoginVO>
}