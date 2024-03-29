package com.dangjang.android.presentation.signup

import android.graphics.Color
import androidx.fragment.app.activityViewModels
import com.dangjang.android.common_ui.BaseFragment
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.FragmentSignupMediBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupMediFragment : BaseFragment<FragmentSignupMediBinding>(R.layout.fragment_signup_medi) {

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

        binding.mediBtn.setOnTouchListener({ v, event -> true })

        var medicineFlag = false
        var injectionFlag = false
        var noFlag = false

        binding.noBtn.setOnClickListener {
            setBtnGreen()
            setMedicineGray()
            setInjectionGray()
            setNoGreen()
            noFlag = true
            medicineFlag = false
            injectionFlag = false
        }

        binding.medicineBtn.setOnClickListener {
            noFlag = false
            if (medicineFlag) {
                setMedicineGray()
            } else {
                setMedicineGreen()
            }
            medicineFlag = !medicineFlag
            setBtnGreen()
            setNoGray()

            if (medicineFlag == false && injectionFlag == false && noFlag == false) {
                setBtnGray()
            }
        }

        binding.injectionBtn.setOnClickListener {
            noFlag = false
            if (injectionFlag) {
                setInjectionGray()
            } else {
                setInjectionGreen()
            }
            injectionFlag = !injectionFlag
            setBtnGreen()
            setNoGray()

            if (medicineFlag == false && injectionFlag == false && noFlag == false) {
                setBtnGray()
            }

        }

        binding.mediBtn.setOnClickListener {
            viewModel.setMedi(medicineFlag, injectionFlag)

            val signupDiseaseFragment = SignupDiseaseFragment()
            parentFragmentManager.beginTransaction().replace(R.id.fragment_signup_view, signupDiseaseFragment).addToBackStack(null).commit()
        }

        binding.backIv.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        endTime = System.currentTimeMillis().toDouble()
        viewModel.shotSignupMediLogging(endTime- startTime)
    }

    private fun setMedicineGreen() {
        binding.medicineBtn.setTextColor(Color.parseColor("#32CC42"))
        binding.medicineBtn.setBackgroundResource(R.drawable.background_round_green)
    }

    private fun setMedicineGray() {
        binding.medicineBtn.setTextColor(Color.parseColor("#878787"))
        binding.medicineBtn.setBackgroundResource(R.drawable.background_round_gray)
    }

    private fun setInjectionGreen() {
        binding.injectionBtn.setTextColor(Color.parseColor("#32CC42"))
        binding.injectionBtn.setBackgroundResource(R.drawable.background_round_green)
    }

    private fun setInjectionGray() {
        binding.injectionBtn.setTextColor(Color.parseColor("#878787"))
        binding.injectionBtn.setBackgroundResource(R.drawable.background_round_gray)
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
        binding.mediBtn.setBackgroundResource(R.drawable.background_green_gradient)
        binding.mediBtn.setOnTouchListener({ v, event -> false })
    }

    private fun setBtnGray() {
        binding.mediBtn.setBackgroundResource(R.drawable.background_round_darkgray)
        binding.mediBtn.setOnTouchListener({ v, event -> true })
    }
}