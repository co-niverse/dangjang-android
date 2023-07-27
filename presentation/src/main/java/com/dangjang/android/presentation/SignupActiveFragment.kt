package com.dangjang.android.presentation

import android.graphics.Color
import androidx.fragment.app.viewModels
import com.dangjang.android.common_ui.BaseFragment
import com.dangjang.android.presentation.databinding.FragmentSignupActiveBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupActiveFragment : BaseFragment<FragmentSignupActiveBinding>(R.layout.fragment_signup_active) {

    private val viewModel by viewModels<SignupViewModel>()

    override fun initView() {
        bind {
            vm = viewModel
        }
    }
    override fun onStart() {
        super.onStart()

        binding.activeSmallCl.setOnClickListener {
            setSmallClGreen()
            setNormalClGray()
            setLargeClGray()
            setBtnGreen()
        }

        binding.activeNormalCl.setOnClickListener {
            setSmallClGray()
            setNormalClGreen()
            setLargeClGray()
            setBtnGreen()
        }

        binding.activeLargeCl.setOnClickListener {
            setSmallClGray()
            setNormalClGray()
            setLargeClGreen()
            setBtnGreen()
        }

        binding.activeBtn.setOnClickListener {
            val signupDiagnosisFragment = SignupDiagnosisFragment()
            parentFragmentManager.beginTransaction().replace(R.id.fragment_signup_view, signupDiagnosisFragment).addToBackStack(null).commit()
        }

        binding.backIv.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

    }

    private fun setSmallClGray() {
        binding.activeSmallCl.background = null
        binding.activeSmallCl.setBackgroundResource(R.drawable.background_white_gradient)
        binding.activeSmallTv.setTextColor(Color.BLACK)
        binding.smallIv1.setImageResource(R.drawable.ic_lightning_gray)
    }

    private fun setSmallClGreen() {
        binding.activeSmallCl.setBackgroundResource(R.drawable.background_border_green)
        binding.activeSmallTv.setTextColor(Color.parseColor("#41E551"))
        binding.smallIv1.setImageResource(R.drawable.ic_lightning_green)
    }

    private fun setNormalClGray() {
        binding.activeNormalCl.setBackgroundResource(R.drawable.background_white_gradient)
        binding.activeNormalTv.setTextColor(Color.BLACK)
        binding.normalIv1.setImageResource(R.drawable.ic_lightning_gray)
        binding.normalIv2.setImageResource(R.drawable.ic_lightning_gray)
    }

    private fun setNormalClGreen() {
        binding.activeNormalCl.setBackgroundResource(R.drawable.background_border_green)
        binding.activeNormalTv.setTextColor(Color.parseColor("#41E551"))
        binding.normalIv1.setImageResource(R.drawable.ic_lightning_green)
        binding.normalIv2.setImageResource(R.drawable.ic_lightning_green)
    }

    private fun setLargeClGray() {
        binding.activeLargeCl.setBackgroundResource(R.drawable.background_white_gradient)
        binding.activeLargeTv.setTextColor(Color.BLACK)
        binding.largeIv1.setImageResource(R.drawable.ic_lightning_gray)
        binding.largeIv2.setImageResource(R.drawable.ic_lightning_gray)
        binding.largeIv3.setImageResource(R.drawable.ic_lightning_gray)
    }

    private fun setLargeClGreen() {
        binding.activeLargeCl.setBackgroundResource(R.drawable.background_border_green)
        binding.activeLargeTv.setTextColor(Color.parseColor("#41E551"))
        binding.largeIv1.setImageResource(R.drawable.ic_lightning_green)
        binding.largeIv2.setImageResource(R.drawable.ic_lightning_green)
        binding.largeIv3.setImageResource(R.drawable.ic_lightning_green)
    }

    private fun setBtnGreen() {
        binding.activeBtn.setBackgroundResource(R.drawable.background_green_gradient)
    }

}