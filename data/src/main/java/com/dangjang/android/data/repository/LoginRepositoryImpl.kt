package com.dangjang.android.data.repository

import android.util.Log
import com.dangjang.android.data.datasource.LoginDataSource
import com.dangjang.android.domain.model.LoginVO
import com.dangjang.android.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginDataSource: LoginDataSource
) : LoginRepository {

    override fun kakaoLogin(accessToken: String): Flow<LoginVO> = flow {
        val response = loginDataSource.kakaoLogin(accessToken)
        if (response.isSuccessful) {
            if (response.body()!!.success) {
                emit(response.body()!!.data.content.toDomain())
                Log.e("[SUCCESS]","kakaoLogin" + response.body().toString())
            }
            else {
                if (response.body()!!.errorCode == 404) {
                    //TODO : 회원가입
                }
                Log.e("[FAIL]","kakaoLogin" + response.body().toString())
            }
        } else {
            Log.e("[FAIL]","kakaoLogin" + response.errorBody()?.string()!!)
        }
    }

    override fun naverLogin(accessToken: String): Flow<LoginVO> = flow {
        val response = loginDataSource.naverLogin(accessToken)
        if (response.isSuccessful) {
            if (response.body()!!.success) {
                emit(response.body()!!.data.content.toDomain())
                Log.e("[SUCCESS]","naverLogin" + response.body().toString())
            }
            else {
                if (response.body()!!.errorCode == 404) {
                    //TODO : 회원가입
                }
                Log.e("[FAIL]","naverLogin" + response.body().toString())
            }
        } else {
            Log.e("[FAIL]","naverLogin" + response.errorBody()?.string()!!)
        }
    }

}