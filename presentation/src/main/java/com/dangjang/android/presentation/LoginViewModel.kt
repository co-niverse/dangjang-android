package com.dangjang.android.presentation

import android.app.Application
import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dangjang.android.domain.HttpResponseException
import com.dangjang.android.domain.model.LoginVO
import com.dangjang.android.domain.usecase.LoginUseCase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val _loginDataFlow = MutableStateFlow(LoginVO())
    val loginDataFlow = _loginDataFlow.asStateFlow()

    private val _signupStartActivity = MutableStateFlow(false)
    val signupStartActivity = _signupStartActivity.asStateFlow()

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

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(getApplication<Application>().applicationContext)) {
            UserApiClient.instance.loginWithKakaoTalk(getApplication<Application>().applicationContext) { token, error ->
                if (error != null) {
                    Log.e("카카오/로그인 실패", error.toString())
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }
                    else {
                        UserApiClient.instance.loginWithKakaoAccount(getApplication<Application>().applicationContext, callback = mCallback)
                    }
                }
                else if (token != null) {
                    Log.e("카카오/로그인 성공",token.accessToken)
                    getKakaoNicknameAndEmail()
                    getKakaoLoginData(token.accessToken)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(getApplication<Application>().applicationContext, callback = mCallback)
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
            loginUseCase.kakoLogin(accessToken)
                .onEach {
                    _loginDataFlow.emit(it)
                }
                .handleErrors()
                .collect()
        }
    }

    fun getNaverLoginData(accessToken: String) {
        viewModelScope.launch {
            loginUseCase.naverLogin(accessToken)
                .onEach {
                    _loginDataFlow.emit(it)
                }
                .handleErrors()
                .collect()
        }
    }

    private fun <T> Flow<T>.handleErrors(): Flow<T> =
        catch { e ->

            val error: HttpResponseException = e as HttpResponseException

            Toast.makeText(getApplication<Application>().applicationContext,e.message,
            Toast.LENGTH_SHORT).show()

            Log.e("error", error.httpCode.toString())

            if (e.httpCode == 404) {
                _signupStartActivity.value = true
            }
        }


}