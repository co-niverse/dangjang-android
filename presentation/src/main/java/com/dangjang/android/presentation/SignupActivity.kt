package com.dangjang.android.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import com.dangjang.android.presentation.databinding.ActivitySignupBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupActivity: FragmentActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val viewModel by viewModels<SignupViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val signupNicknameFragment = SignupNicknameFragment()
        supportFragmentManager.beginTransaction().add(R.id.fragment_signup_view, signupNicknameFragment).commit()

    }
}