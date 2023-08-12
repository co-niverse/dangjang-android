package com.dangjang.android.presentation

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dangjang.android.presentation.databinding.ActivityLoginBinding
import com.dangjang.android.presentation.signup.SignupActivity
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity: FragmentActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.btnKakaoLogin.setOnClickListener {
            viewModel.kakaoLogin()
        }
        binding.btnNaverLogin.setOnClickListener {
            naverLogin()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.signupStartActivity.collect {
                if (it) {
                    startActivity(Intent(applicationContext, SignupActivity::class.java))
                    finish()
                }
            }
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