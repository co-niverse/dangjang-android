package com.dangjang.android.presentation.mypage

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.ActivityDeviceBinding

class DeviceActivity : FragmentActivity() {
    private lateinit var binding: ActivityDeviceBinding
    private val viewModel by viewModels<MypageViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_device)

        binding.backIv.setOnClickListener {
            finish()
        }
    }
}