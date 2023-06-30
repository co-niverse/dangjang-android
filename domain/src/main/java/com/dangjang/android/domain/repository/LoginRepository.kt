package com.dangjang.android.domain.repository

import com.dangjang.android.domain.model.LoginVO
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    //Login API
    fun login(accessToken: String): Flow<LoginVO>
}