package com.dangjang.android.presentation

import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dangjang.android.domain.constants.AUTO_LOGIN_EDITOR_KEY
import com.dangjang.android.domain.constants.AUTO_LOGIN_SPF_KEY
import com.dangjang.android.domain.constants.KAKAO
import com.dangjang.android.domain.constants.NAVER
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        //TODO: Check if user is logged in
        val sp: SharedPreferences = getSharedPreferences(AUTO_LOGIN_SPF_KEY, MODE_PRIVATE)
        val provider = sp.getString(AUTO_LOGIN_EDITOR_KEY, "null")
        Log.e("sp",provider.toString())

        if (provider == KAKAO) {
            viewModel.kakaoLogin()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else if (provider == NAVER) {
            naverLogin()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun naverLogin() {
        lateinit var naverToken: String

        val profileCallback = object : NidProfileCallback<NidProfileResponse> {
            override fun onSuccess(result: NidProfileResponse) {
                Log.e("네이버/로그인 성공", naverToken)
                Log.i(
                    ContentValues.TAG, "네이버/사용자 정보 요청 성공" +
                            "\n닉네임: ${result.profile?.nickname}" +
                            "\n이메일: ${result.profile?.email}" +
                            "\n사진: ${result.profile?.profileImage}"
                )
                viewModel.getNaverLoginData(naverToken)
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