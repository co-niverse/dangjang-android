package com.dangjang.android.presentation

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.dangjang.android.presentation.databinding.ActivityLoginBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity: FragmentActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.btnKakaoLogin.setOnClickListener {
            kakaoLogin()
        }
        binding.btnNaverLogin.setOnClickListener {
            naverLogin()
        }
    }
    private fun kakaoLogin() {

        val mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("카카오/로그인 실패", error.toString())
            } else if (token != null) {
                Log.e("카카오/로그인 성공",token.accessToken)
                getKakaoNicknameAndEmail()
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e("카카오/로그인 실패", error.toString())
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }
                    else {
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = mCallback)
                    }
                }
                else if (token != null) {
                    Log.e("카카오/로그인 성공",token.accessToken)
                    getKakaoNicknameAndEmail()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = mCallback)
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
                //TODO: 서버 연동
            }
        }
    }

    private fun naverLogin() {
        lateinit var naverToken: String

        val profileCallback = object : NidProfileCallback<NidProfileResponse> {
            override fun onSuccess(result: NidProfileResponse) {
                Log.e("네이버/로그인 성공", naverToken)
                //TODO: 서버 연동
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Log.e("네이버/로그인 실패", "errorCode: ${errorCode}, errorDescription: $errorDescription")
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        val oAuthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                naverToken = NaverIdLoginSDK.getAccessToken().toString()
                NidOAuthLogin().callProfileApi(profileCallback)
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Log.e("네이버/로그인 실패", "errorCode: ${errorCode}, errorDescription: $errorDescription")
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }
        NaverIdLoginSDK.authenticate(this, oAuthLoginCallback)
    }
}