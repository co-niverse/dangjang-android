package com.dangjang.android.presentation

import android.graphics.Color
import android.util.Log
import androidx.fragment.app.activityViewModels
import com.dangjang.android.common_ui.BaseFragment
import com.dangjang.android.presentation.databinding.FragmentSignupAgreeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupAgreeFragment : BaseFragment<FragmentSignupAgreeBinding>(R.layout.fragment_signup_agree) {

    private val viewModel : SignupViewModel by activityViewModels()

    override fun initView() {
        bind {
            vm = viewModel
        }
    }
    override fun onStart() {
        super.onStart()

        binding.agreeBtn.setOnTouchListener({ v, event -> true })

        var serviceFlag = false
        var personalFlag = false

        binding.serviceCl.setOnClickListener {
            binding.serviceTv.setTextColor((Color.BLACK))
            binding.serviceCheckIv.setImageResource(R.drawable.ic_check_green)
            serviceFlag = true

            if (personalFlag) {
                setBtnGreen()
            }
        }

        binding.personalCl.setOnClickListener {
            binding.personalTv.setTextColor((Color.BLACK))
            binding.personalCheckIv.setImageResource(R.drawable.ic_check_green)
            personalFlag = true

            if (serviceFlag) {
                setBtnGreen()
            }
        }

        binding.agreeBtn.setOnClickListener {
            Log.e("viewmodel",viewModel.signupRequest.value.toString())
            viewModel.signup(viewModel.signupRequest.value)
        }

        binding.backIv.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setBtnGreen() {
        binding.agreeBtn.setBackgroundResource(R.drawable.background_green_gradient)
        binding.agreeBtn.setOnTouchListener({ v, event -> false })
    }

}