package com.dangjang.android.presentation

import android.graphics.Color
import androidx.fragment.app.viewModels
import com.dangjang.android.common_ui.BaseFragment
import com.dangjang.android.presentation.databinding.FragmentSignupDiseaseBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupDiseaseFragment : BaseFragment<FragmentSignupDiseaseBinding>(R.layout.fragment_signup_disease) {

    private val viewModel by viewModels<SignupViewModel>()

    override fun initView() {
        bind {
            vm = viewModel
        }
    }
    override fun onStart() {
        super.onStart()

        var lowBpFlag = false
        var highBpFlag = false
        var hyperFlag = false
        var obesityFlag = false
        var noFlag = false

        binding.lowBpBtn.setOnClickListener {
            if (lowBpFlag) {
                setLowBpGray()
            } else {
                setLowBpGreen()
            }
            lowBpFlag = !lowBpFlag

            setNoGray()
            noFlag = false

            if (lowBpFlag == false && highBpFlag == false && hyperFlag == false && obesityFlag == false && noFlag == false) {
                setBtnGray()
            } else {
                setBtnGreen()
            }
        }

        binding.highBpBtn.setOnClickListener {
            if (highBpFlag) {
                setHighBpGray()
            } else {
                setHighBpGreen()
            }
            highBpFlag = !highBpFlag

            setNoGray()
            noFlag = false

            if (lowBpFlag == false && highBpFlag == false && hyperFlag == false && obesityFlag == false && noFlag == false) {
                setBtnGray()
            } else {
                setBtnGreen()
            }
        }

        binding.hyperlipidemiaBtn.setOnClickListener {
            if (hyperFlag) {
                setHyperGray()
            } else {
                setHyperGreen()
            }
            hyperFlag = !hyperFlag

            setNoGray()
            noFlag = false

            if (lowBpFlag == false && highBpFlag == false && hyperFlag == false && obesityFlag == false && noFlag == false) {
                setBtnGray()
            } else {
                setBtnGreen()
            }
        }

        binding.obesityBtn.setOnClickListener {
            if (obesityFlag) {
                setObesityGray()
            } else {
                setObesityGreen()
            }
            obesityFlag = !obesityFlag

            setNoGray()
            noFlag = false

            if (lowBpFlag == false && highBpFlag == false && hyperFlag == false && obesityFlag == false && noFlag == false) {
                setBtnGray()
            } else {
                setBtnGreen()
            }
        }

        binding.noBtn.setOnClickListener {
            setLowBpGray()
            lowBpFlag = false
            setHighBpGray()
            highBpFlag = false
            setHyperGray()
            hyperFlag = false
            setObesityGray()
            obesityFlag = false
            setNoGreen()
            setBtnGreen()
        }

    }

    private fun setLowBpGreen() {
        binding.lowBpBtn.setTextColor(Color.parseColor("#32CC42"))
        binding.lowBpBtn.setBackgroundResource(R.drawable.background_round_green)
    }

    private fun setLowBpGray() {
        binding.lowBpBtn.setTextColor(Color.parseColor("#878787"))
        binding.lowBpBtn.setBackgroundResource(R.drawable.background_round_gray)
    }

    private fun setHighBpGreen() {
        binding.highBpBtn.setTextColor(Color.parseColor("#32CC42"))
        binding.highBpBtn.setBackgroundResource(R.drawable.background_round_green)
    }

    private fun setHighBpGray() {
        binding.highBpBtn.setTextColor(Color.parseColor("#878787"))
        binding.highBpBtn.setBackgroundResource(R.drawable.background_round_gray)
    }

    private fun setHyperGreen() {
        binding.hyperlipidemiaBtn.setTextColor(Color.parseColor("#32CC42"))
        binding.hyperlipidemiaBtn.setBackgroundResource(R.drawable.background_round_green)
    }

    private fun setHyperGray() {
        binding.hyperlipidemiaBtn.setTextColor(Color.parseColor("#878787"))
        binding.hyperlipidemiaBtn.setBackgroundResource(R.drawable.background_round_gray)
    }

    private fun setObesityGreen() {
        binding.obesityBtn.setTextColor(Color.parseColor("#32CC42"))
        binding.obesityBtn.setBackgroundResource(R.drawable.background_round_green)
    }

    private fun setObesityGray() {
        binding.obesityBtn.setTextColor(Color.parseColor("#878787"))
        binding.obesityBtn.setBackgroundResource(R.drawable.background_round_gray)
    }

    private fun setNoGreen() {
        binding.noBtn.setTextColor(Color.parseColor("#32CC42"))
        binding.noBtn.setBackgroundResource(R.drawable.background_round_green)
    }

    private fun setNoGray() {
        binding.noBtn.setTextColor(Color.parseColor("#878787"))
        binding.noBtn.setBackgroundResource(R.drawable.background_round_gray)
    }

    private fun setBtnGreen() {
        binding.diseaseBtn.setBackgroundResource(R.drawable.background_green_gradient)
    }

    private fun setBtnGray() {
        binding.diseaseBtn.setBackgroundResource(R.drawable.background_round_darkgray)
    }
}