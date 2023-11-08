package com.dangjang.android.presentation.intro

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dangjang.android.domain.constants.KAKAO
import com.dangjang.android.domain.constants.NAVER
import com.dangjang.android.presentation.login.LoginActivity
import com.dangjang.android.presentation.signup.SignupActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val viewModel by viewModels<HealthConnectViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        goToMainOrLoginActivity()
    }

    private fun goToMainOrLoginActivity() {
        if (viewModel.getAutoLoginProviderSpf() == KAKAO || viewModel.getAutoLoginProviderSpf() == NAVER) {
            //TODO : go to health connect activity
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }
    }
}