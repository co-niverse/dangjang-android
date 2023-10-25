package com.dangjang.android.presentation.mypage

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.dangjang.android.common_ui.BaseFragment
import com.dangjang.android.domain.constants.ACCESS_TOKEN_KEY
import com.dangjang.android.domain.constants.TOKEN_SPF_KEY
import com.dangjang.android.domain.constants.VERSION_SPF_KEY
import com.dangjang.android.domain.constants.VERSION_TOKEN_KEY
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.FragmentMypageBinding
import com.dangjang.android.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MypageFragment : BaseFragment<FragmentMypageBinding>(R.layout.fragment_mypage) {

    private val viewModel by viewModels<MypageViewModel>()
    private var startTime: Double = 0.0
    private var endTime: Double = 0.0

    override fun initView() {
        bind {
            vm = viewModel
        }
    }
    override fun onStart() {
        super.onStart()

        startTime = System.currentTimeMillis().toDouble()

        binding.vm = viewModel
        binding.lifecycleOwner = this

        binding.versionTv.text = "ver " + getVersionName()

        getAccessToken()?.let { viewModel.getMypage(it) }

        lifecycleScope.launchWhenStarted {
            viewModel.goToLoginActivityFlow.collectLatest {
                if (it) {
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
            }
        }

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
        binding.serviceIv.setOnClickListener {
            goToServiceUrl()
        }
        binding.serviceTv.setOnClickListener {
            goToServiceUrl()
        }
        binding.privateIv.setOnClickListener {
            goToPrivateUrl()
        }
        binding.privateTv.setOnClickListener {
            goToPrivateUrl()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        endTime = System.currentTimeMillis().toDouble()
        viewModel.shotMypageExposureLogging(endTime - startTime)
    }
    private fun goToServiceUrl() {
        val intentUrl = Intent(Intent.ACTION_VIEW, Uri.parse("https://co-niverse.notion.site/a3e0b9b54d0f4b3bba174901297ec918?pvs=4"))
        startActivity(intentUrl)
    }

    private fun goToPrivateUrl() {
        val intentUrl = Intent(Intent.ACTION_VIEW, Uri.parse("https://co-niverse.notion.site/cb58f28f2c6e465ea5c596871baaca78?pvs=4"))
        startActivity(intentUrl)
    }

    private fun getAccessToken(): String? {
        val sharedPreferences = requireContext().getSharedPreferences(TOKEN_SPF_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }

    private fun getVersionName(): String? {
        val sharedPreferences = requireContext().getSharedPreferences(VERSION_SPF_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getString(VERSION_TOKEN_KEY, null)
    }
}