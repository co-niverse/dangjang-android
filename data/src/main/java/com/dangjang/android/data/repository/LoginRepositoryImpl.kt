package com.dangjang.android.data.repository

import android.util.Log
import com.dangjang.android.data.datasource.LoginDataSource
import com.dangjang.android.domain.model.LoginVO
import com.dangjang.android.domain.repository.LoginRepository
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginDataSource: LoginDataSource
) : LoginRepository {

    override fun login(accessToken: String): Flow<LoginVO> = flow {
        val response = loginDataSource.login(accessToken)
        response.suspendOnSuccess {
            emit(
                data.data.toDomain()
            )
            Log.e("[SUCCESS]","Login 성공")
        }.onFailure { Log.e("[ERROR]","Login 실패") }
    }

}