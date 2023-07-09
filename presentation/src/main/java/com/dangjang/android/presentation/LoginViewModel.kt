package com.dangjang.android.presentation

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dangjang.android.domain.model.LoginVO
import com.dangjang.android.domain.usecase.GetLoginUseCase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getLoginUseCase: GetLoginUseCase
) : ViewModel() {

    private val _loginDataFlow = MutableStateFlow(LoginVO())
    val loginDataFlow = _loginDataFlow.asStateFlow()

    fun kakaoLogin() {

        val mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("카카오/로그인 실패", error.toString())
            } else if (token != null) {
                Log.e("카카오/로그인 성공",token.accessToken)
                getKakaoNicknameAndEmail()
                getKakaoLoginData(token.accessToken)
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e("카카오/로그인 실패", error.toString())
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }
                    else {
                        UserApiClient.instance.loginWithKakaoAccount(context, callback = mCallback)
                    }
                }
                else if (token != null) {
                    Log.e("카카오/로그인 성공",token.accessToken)
                    getKakaoNicknameAndEmail()
                    getKakaoLoginData(token.accessToken)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = mCallback)
        }
    }

    private fun getKakaoNicknameAndEmail() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(ContentValues.TAG, "카카오/사용자 정보 요청 실패", error)
            } else if (user != null) {
                Log.i(
                    ContentValues.TAG, "카카오/사용자 정보 요청 성공" +
                            "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                            "\n이메일: ${user.kakaoAccount?.email}" +
                            "\n사진: ${user.kakaoAccount?.profile?.profileImageUrl}"
                )
            }
        }
    }

    private fun getKakaoLoginData(accessToken: String) {
        viewModelScope.launch {
            getLoginUseCase.kakoLogin(accessToken).collect{
                _loginDataFlow.emit(it)
            }
        }
    }

    fun getNaverLoginData(accessToken: String) {
        viewModelScope.launch {
            getLoginUseCase.naverLogin(accessToken).collect{
                _loginDataFlow.emit(it)
            }
        }
    }

}