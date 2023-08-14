package com.dangjang.android.presentation.home

import android.content.Intent
import androidx.fragment.app.viewModels
import com.dangjang.android.common_ui.BaseFragment
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel by viewModels<HomeViewModel>()

    override fun initView() {
        bind {
            vm = viewModel
        }
    }
    override fun onStart() {
        super.onStart()

        binding.glucoseSeekbar.setOnTouchListener({ v, event -> true })
        binding.weightSeekbar.setOnTouchListener({ v, event -> true })

        binding.glucoseCl.setOnClickListener {
            Intent(activity, GlucoseActivity::class.java).apply {
                startActivity(this)
            }
        }

        binding.weightCl.setOnClickListener {
            Intent(activity, WeightActivity::class.java).apply {
                startActivity(this)
            }
        }

        binding.bloodPressureCl.setOnClickListener {
            Intent(activity, BloodPressureActivity::class.java).apply {
                startActivity(this)
            }
        }
    }

}