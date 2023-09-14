package com.dangjang.android.presentation.intro

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dangjang.android.domain.constants.AUTO_LOGIN_EDITOR_KEY
import com.dangjang.android.domain.constants.AUTO_LOGIN_SPF_KEY
import com.dangjang.android.domain.constants.HEALTH_CONNECT_INSTALLED
import com.dangjang.android.domain.constants.HEALTH_CONNECT_NOT_INSTALLED
import com.dangjang.android.domain.constants.HEALTH_CONNECT_TOKEN_KEY
import com.dangjang.android.domain.constants.KAKAO
import com.dangjang.android.domain.constants.NAVER
import com.dangjang.android.presentation.MainActivity
import com.dangjang.android.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val viewModel by viewModels<SplashViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sp: SharedPreferences = getSharedPreferences(AUTO_LOGIN_SPF_KEY, MODE_PRIVATE)
        val provider = sp.getString(AUTO_LOGIN_EDITOR_KEY, "null")
        Log.e("sp",provider.toString())

        if (provider == KAKAO) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else if (provider == NAVER) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        viewModel.getIntroData()

        viewModel.checkAvailability()

        val healthConnect = sp.getString(HEALTH_CONNECT_TOKEN_KEY, "null")

        if (healthConnect == "true") {
            if (viewModel.healthConnectFlow.value.isAvaiable == HEALTH_CONNECT_INSTALLED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    viewModel.getHealthConnect()
                }
            }
        }
        else if (healthConnect == "false") {
            if (viewModel.healthConnectFlow.value.isAvaiable == HEALTH_CONNECT_NOT_INSTALLED) {
                //TODO : 헬스커넥트 팝업 띄우기
            }
        }
    }
}