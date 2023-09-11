package com.dangjang.android.presentation.mypage

import androidx.fragment.app.activityViewModels
import com.dangjang.android.common_ui.BaseFragment
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.FragmentDeviceSettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeviceSettingFragment : BaseFragment<FragmentDeviceSettingBinding>(R.layout.fragment_device_setting) {

        private val viewModel : MypageViewModel by activityViewModels()

        override fun initView() {
            bind {
                vm = viewModel
            }
        }


    override fun onStart() {
        super.onStart()

        binding.backIv.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

}