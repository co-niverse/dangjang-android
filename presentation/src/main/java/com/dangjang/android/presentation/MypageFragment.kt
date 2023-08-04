package com.dangjang.android.presentation

import androidx.fragment.app.viewModels
import com.dangjang.android.common_ui.BaseFragment
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
    }

}