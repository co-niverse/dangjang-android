package com.dangjang.android.presentation

import androidx.fragment.app.viewModels
import com.dangjang.android.common_ui.BaseFragment
import com.dangjang.android.presentation.databinding.FragmentChartBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChartFragment : BaseFragment<FragmentChartBinding>(R.layout.fragment_chart) {

    private val viewModel by viewModels<ChartViewModel>()

    override fun initView() {
        bind {
            vm = viewModel
        }
    }
    override fun onStart() {
        super.onStart()
    }

}