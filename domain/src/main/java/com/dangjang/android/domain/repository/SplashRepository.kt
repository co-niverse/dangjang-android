package com.dangjang.android.domain.repository

import com.dangjang.android.domain.model.IntroVO
import kotlinx.coroutines.flow.Flow

interface SplashRepository {

    //Intro API
    suspend fun getIntroApi(): Flow<IntroVO>
}