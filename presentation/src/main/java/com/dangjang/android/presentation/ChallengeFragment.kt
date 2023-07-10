package com.dangjang.android.presentation

import androidx.fragment.app.viewModels
import com.dangjang.android.common_ui.BaseFragment
import com.dangjang.android.presentation.databinding.FragmentChallengeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChallengeFragment : BaseFragment<FragmentChallengeBinding>(R.layout.fragment_challenge) {

    private val viewModel by viewModels<ChallengeViewModel>()

    override fun initView() {
        bind {
            vm = viewModel
        }
    }
    override fun onStart() {
        super.onStart()
    }

}