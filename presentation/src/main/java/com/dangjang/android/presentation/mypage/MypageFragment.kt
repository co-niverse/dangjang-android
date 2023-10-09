package com.dangjang.android.presentation.mypage

import android.content.Context
import android.content.Intent
import androidx.fragment.app.viewModels
import com.dangjang.android.common_ui.BaseFragment
import com.dangjang.android.domain.constants.ACCESS_TOKEN_KEY
import com.dangjang.android.domain.constants.TOKEN_SPF_KEY
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.FragmentMypageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MypageFragment : BaseFragment<FragmentMypageBinding>(R.layout.fragment_mypage) {

    private val viewModel by viewModels<MypageViewModel>()

    override fun initView() {
        bind {
            vm = viewModel
        }
    }
    override fun onStart() {
        super.onStart()

        binding.vm = viewModel
        binding.lifecycleOwner = this

        getAccessToken()?.let { viewModel.getMypage(it) }

        binding.deviceIv.setOnClickListener {
            startActivity(Intent(context, DeviceActivity::class.java))
        }
        binding.deviceTv.setOnClickListener {
            startActivity(Intent(context, DeviceActivity::class.java))
        }
        binding.coinCl.setOnClickListener {
            startActivity(Intent(context, PointActivity::class.java))
        }
        binding.pointBtn.setOnClickListener {
            startActivity(Intent(context, PointActivity::class.java))
        }
        binding.authIv.setOnClickListener {
            startActivity(Intent(context, AuthActivity::class.java))
        }
        binding.authTv.setOnClickListener {
            startActivity(Intent(context, AuthActivity::class.java))
        }
    }

    private fun getAccessToken(): String? {
        val sharedPreferences = requireContext().getSharedPreferences(TOKEN_SPF_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }
}