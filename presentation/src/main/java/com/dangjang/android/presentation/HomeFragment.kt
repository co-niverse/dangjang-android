package com.dangjang.android.presentation

import androidx.fragment.app.viewModels
import com.dangjang.android.common_ui.BaseFragment
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
    }

}