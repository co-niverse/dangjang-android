package com.dangjang.android.presentation.mypage

import androidx.fragment.app.activityViewModels
import com.dangjang.android.common_ui.BaseFragment
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.FragmentPointCheckBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PointCheckFragment : BaseFragment<FragmentPointCheckBinding>(R.layout.fragment_point_check){

    private val viewModel : MypageViewModel by activityViewModels()

    override fun initView() {
        bind {
            vm = viewModel
        }
    }

    override fun onStart() {
        super.onStart()
    }

}