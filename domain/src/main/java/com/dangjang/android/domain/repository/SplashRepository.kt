package com.dangjang.android.domain.repository

import com.dangjang.android.domain.model.IntroVO
import com.dangjang.android.domain.request.PostHealthConnectRequest
import kotlinx.coroutines.flow.Flow

interface SplashRepository {

    //Intro API
    suspend fun getIntroApi(): Flow<IntroVO>

    //Health Connect API
    suspend fun postHealthConnect(accessToken: String, postHealthConnectRequest: PostHealthConnectRequest): Flow<Nothing>
}