package com.dangjang.android.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.dangjang.android.common_ui.BaseFragment
import com.dangjang.android.presentation.databinding.FragmentIntroBinding


class IntroFragment : BaseFragment<FragmentIntroBinding>(R.layout.fragment_intro) {
    private val viewModel by viewModels<IntroViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchIntroData()
    }

}