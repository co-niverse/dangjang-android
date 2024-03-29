package com.dangjang.android.presentation.mypage

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.dangjang.android.common_ui.BaseFragment
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.FragmentDeviceSettingBinding
import com.dangjang.android.presentation.intro.HealthConnectViewModel
import com.dangjang.android.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DeviceSettingFragment : BaseFragment<FragmentDeviceSettingBinding>(R.layout.fragment_device_setting) {

    private val viewModel : MypageViewModel by activityViewModels()
    private val healthConnectViewModel: HealthConnectViewModel by activityViewModels()

    private var startTime: Double = 0.0
    private var endTime: Double = 0.0

        override fun initView() {
            bind {
                vm = viewModel
            }
        }


    override fun onStart() {
        super.onStart()

        healthConnectViewModel.checkAvailability()
        healthConnectViewModel.checkHealthConnectInterlock()
        healthConnectViewModel.getAllHealthConnectData()

        lifecycleScope.launch {
            healthConnectViewModel.patchHealthConnectInterlockFlow.collectLatest {
                viewModel.setHealthConnectManualJoined(it.toBoolean())
            }
        }

        startTime = System.currentTimeMillis().toDouble()

        lifecycleScope.launchWhenStarted {
            viewModel.goToLoginActivityFlow.collectLatest {
                if (it) {
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
            }
        }

        binding.backIv.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.deviceSettingInstallHealthConnectBtn.setOnClickListener {
            clickHealthConnectUrl()
        }

        binding.deviceSettingGoHealthConnectBtn.setOnClickListener {
            clickHealthConnectUrl()
        }

        binding.deviceSettingSv.viewTreeObserver.addOnScrollChangedListener {
            val scrollY = binding.deviceSettingSv.scrollY

            if (scrollY + binding.deviceSettingSv.height >= binding.deviceSettingSv.getChildAt(0).height) {
                viewModel.setHealthConnectManualScrolled(true)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        endTime = System.currentTimeMillis().toDouble()
        viewModel.shotHealthConnectManualStayLogging(endTime - startTime)
    }

    private fun clickHealthConnectUrl() {
        val intentUrl = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.healthdata"))
        startActivity(intentUrl)
    }

}