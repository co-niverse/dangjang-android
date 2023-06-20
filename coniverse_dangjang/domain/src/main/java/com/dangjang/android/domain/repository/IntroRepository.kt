package com.dangjang.android.domain.repository

import com.dangjang.android.domain.model.IntroVO
import kotlinx.coroutines.flow.Flow

interface IntroRepository {

    //Intro API
    fun getIntroApi(isError: Int): Flow<IntroVO>
}