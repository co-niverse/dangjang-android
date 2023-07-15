package com.dangjang.android.domain.repository

import kotlinx.coroutines.flow.Flow

interface IntroRepository {

    //Intro API
    suspend fun getIntroApi(): Flow<Any>
}