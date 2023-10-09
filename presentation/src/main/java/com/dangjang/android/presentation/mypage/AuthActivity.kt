package com.dangjang.android.presentation.mypage

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.ActivityAuthBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : FragmentActivity() {
    private lateinit var binding: ActivityAuthBinding
    private val viewModel by viewModels<MypageViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth)

        binding.backIv.setOnClickListener {
            finish()
        }

        binding.logoutBtn.setOnClickListener {
            val logoutDialogFragment = LogoutDialogFragment()
            logoutDialogFragment.show(supportFragmentManager, "logoutDialog")
        }

        binding.signoutBtn.setOnClickListener {
            val signoutDialogFragment = SignoutDialogFragment()
            signoutDialogFragment.show(supportFragmentManager, "signoutDialog")
        }
    }
}