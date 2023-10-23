package com.dangjang.android.presentation.signup

import android.graphics.Color
import androidx.fragment.app.activityViewModels
import com.dangjang.android.common_ui.BaseFragment
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.FragmentSignupActiveBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupActiveFragment : BaseFragment<FragmentSignupActiveBinding>(R.layout.fragment_signup_active) {

    private val viewModel : SignupViewModel by activityViewModels()
    private var activityAmount: String = ""
    private var startTime: Double = 0.0
    private var endTime: Double = 0.0

    override fun initView() {
        bind {
            vm = viewModel
        }
    }
    override fun onStart() {
        super.onStart()
        startTime = System.currentTimeMillis().toDouble()

        binding.activeBtn.setOnTouchListener({ v, event -> true })

        binding.activeSmallCl.setOnClickListener {
            setSmallClGreen()
            setNormalClGray()
            setLargeClGray()
            setBtnGreen()
            activityAmount = "LOW"
        }

        binding.activeNormalCl.setOnClickListener {
            setSmallClGray()
            setNormalClGreen()
            setLargeClGray()
            setBtnGreen()
            activityAmount = "MEDIUM"
        }

        binding.activeLargeCl.setOnClickListener {
            setSmallClGray()
            setNormalClGray()
            setLargeClGreen()
            setBtnGreen()
            activityAmount = "HIGH"
        }

        binding.activeBtn.setOnClickListener {
            viewModel.setActivityAmount(activityAmount)

            val signupDiagnosisFragment = SignupDiagnosisFragment()
            parentFragmentManager.beginTransaction().replace(R.id.fragment_signup_view, signupDiagnosisFragment).addToBackStack(null).commit()
        }

        binding.backIv.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        endTime = System.currentTimeMillis().toDouble()
        viewModel.shotSignupNicknameLogging(endTime- startTime)
    }

    private fun setSmallClGray() {
        binding.activeSmallCl.background = null
        binding.activeSmallCl.setBackgroundResource(R.drawable.background_white_gradient)
        binding.activeSmallTv.setTextColor(Color.BLACK)
        binding.smallIv1.setImageResource(R.drawable.ic_lightning_gray)
    }

    private fun setSmallClGreen() {
        binding.activeSmallCl.setBackgroundResource(R.drawable.background_border_green)
        binding.activeSmallTv.setTextColor(Color.parseColor("#3B9C44"))
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
        binding.activeNormalTv.setTextColor(Color.parseColor("#3B9C44"))
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
        binding.activeLargeTv.setTextColor(Color.parseColor("#3B9C44"))
        binding.largeIv1.setImageResource(R.drawable.ic_lightning_green)
        binding.largeIv2.setImageResource(R.drawable.ic_lightning_green)
        binding.largeIv3.setImageResource(R.drawable.ic_lightning_green)
    }

    private fun setBtnGreen() {
        binding.activeBtn.setBackgroundResource(R.drawable.background_green_gradient)
        binding.activeBtn.setOnTouchListener({ v, event -> false })
    }

}