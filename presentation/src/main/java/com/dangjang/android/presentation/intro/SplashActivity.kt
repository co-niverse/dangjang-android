package com.dangjang.android.presentation.intro

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
import com.dangjang.android.presentation.MainActivity
import com.dangjang.android.presentation.login.LoginActivity
import com.dangjang.android.presentation.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        val sp: SharedPreferences = getSharedPreferences(AUTO_LOGIN_SPF_KEY, MODE_PRIVATE)
        val provider = sp.getString(AUTO_LOGIN_EDITOR_KEY, "null")
        Log.e("sp",provider.toString())

        if (provider == KAKAO) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else if (provider == NAVER) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}