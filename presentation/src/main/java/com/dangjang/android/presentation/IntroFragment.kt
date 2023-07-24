package com.dangjang.android.presentation

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View.VISIBLE
import androidx.fragment.app.viewModels
import com.dangjang.android.common_ui.BaseFragment
import com.dangjang.android.presentation.databinding.FragmentIntroBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroFragment : BaseFragment<FragmentIntroBinding>(R.layout.fragment_intro) {

    private val viewModel by viewModels<IntroViewModel>()

    override fun initView() {
        bind {
            vm = viewModel
        }
    }
    override fun onStart() {
        super.onStart()

        viewModel.getIntroData()

        viewModel.checkAvailability()

        if (viewModel.healthConnectFlow.value.isAvaiable == 3) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                viewModel.getHealthConnect()
            }
        }
        if (viewModel.healthConnectFlow.value.isAvaiable == 2) {
            binding.goHealthConnectTv.visibility = VISIBLE
            binding.goHealthConnectTv.setOnClickListener {
                clickHealthConnectUrl()
            }
        }

    }

    private fun clickHealthConnectUrl() {
        val intentUrl = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.healthdata"))
        startActivity(intentUrl)
    }

}