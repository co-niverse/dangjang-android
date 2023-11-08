package com.dangjang.android.presentation.signup

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.ActivitySignupBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupActivity: FragmentActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val viewModel by viewModels<SignupViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val provider = intent.getStringExtra("provider")
        val accessToken = intent.getStringExtra("accessToken")

        viewModel.setProvider("provider!!")
        viewModel.setAccessToken("accessToken!!")

        val signupNicknameFragment = SignupNicknameFragment()
        supportFragmentManager.beginTransaction().add(R.id.fragment_signup_view, signupNicknameFragment).commit()

    }
}