package com.dangjang.android.presentation.signup

import android.graphics.Color
import androidx.fragment.app.activityViewModels
import com.dangjang.android.common_ui.BaseFragment
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.FragmentSignupDiseaseBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupDiseaseFragment : BaseFragment<FragmentSignupDiseaseBinding>(R.layout.fragment_signup_disease) {

    private val viewModel : SignupViewModel by activityViewModels()
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

        binding.diseaseBtn.setOnTouchListener({ v, event -> true })

        var lowBpFlag = false
        var highBpFlag = false
        var hyperFlag = false
        var obesityFlag = false
        var noFlag = false

        var diseasesList: ArrayList<String> = ArrayList()

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

        binding.diseaseBtn.setOnClickListener {
            if (lowBpFlag) {
                diseasesList.apply {
                    add("저혈압")
                }
            }
            if (highBpFlag) {
                diseasesList.apply {
                    add("고혈압")
                }
            }
            if (hyperFlag) {
                diseasesList.apply {
                    add("고지혈증")
                }
            }
            if (obesityFlag) {
                diseasesList.apply {
                    add("비만")
                }
            }

            viewModel.setDiseases(diseasesList)

            val signupAgreeFragment = SignupAgreeFragment()
            parentFragmentManager.beginTransaction().replace(R.id.fragment_signup_view, signupAgreeFragment).addToBackStack(null).commit()
        }

        binding.backIv.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        endTime = System.currentTimeMillis().toDouble()
        viewModel.shotSignupDiseaseLogging(endTime- startTime)
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
        binding.diseaseBtn.setOnTouchListener({ v, event -> false })
    }

    private fun setBtnGray() {
        binding.diseaseBtn.setBackgroundResource(R.drawable.background_round_darkgray)
        binding.diseaseBtn.setOnTouchListener({ v, event -> true })
    }
}