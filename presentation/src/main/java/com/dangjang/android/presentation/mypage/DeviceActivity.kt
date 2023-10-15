package com.dangjang.android.presentation.mypage

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.ActivityDeviceBinding
import com.dangjang.android.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class DeviceActivity : FragmentActivity() {
    private lateinit var binding: ActivityDeviceBinding
    private val viewModel by viewModels<MypageViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_device)

        binding.backIv.setOnClickListener {
            finish()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.goToLoginActivityFlow.collectLatest {
                if (it) {
                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        binding.deviceGoSettingBtn.setOnClickListener {
            val deviceSettingFragment = DeviceSettingFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.device_cl, deviceSettingFragment).addToBackStack(null)
                .commit()
        }
    }
}