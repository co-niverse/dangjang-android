package com.dangjang.android.presentation

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.dangjang.android.common_ui.BaseFragment
import com.dangjang.android.presentation.databinding.FragmentSignupDiagnosisBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupDiagnosisFragment : BaseFragment<FragmentSignupDiagnosisBinding>(R.layout.fragment_signup_diagnosis) {

    private val viewModel by viewModels<SignupViewModel>()

    override fun initView() {
        bind {
            vm = viewModel
        }
    }
    override fun onStart() {
        super.onStart()

        var yesNoFlag = false

        val yearSpinner: Spinner = binding.yearSpinner

        val yearList = arrayListOf<String>()
        for (i in 1..20) {
            yearList.add(i.toString())
        }
        yearList.add("20년 이상")

        val yearAdapter = object : ArrayAdapter<String>(requireContext(), R.layout.custom_spinner_dropdown_item, yearList),
            SpinnerAdapter {
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                // 커스텀한 드롭다운 리스트에 표시할 뷰를 정의합니다.
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(Color.BLACK) // 드롭다운 리스트의 항목 텍스트 색상 설정
                return view
            }
        }

        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if (yesNoFlag == true) {
                    setBtnGreen()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

        yearSpinner.adapter = yearAdapter

        binding.yesBtn.setOnClickListener {
            setYesGreen()
            yesNoFlag = true
        }

        binding.noBtn.setOnClickListener {
            setNoGreen()
            yesNoFlag = true
        }

        binding.diagnosisBtn.setOnClickListener {
            val signupMediFragment = SignupMediFragment()
            parentFragmentManager.beginTransaction().replace(R.id.fragment_signup_view, signupMediFragment).addToBackStack(null).commit()
        }
    }

    private fun setYesGreen() {
        binding.yesBtn.setTextColor(Color.parseColor("#32CC42"))
        binding.yesBtn.setBackgroundResource(R.drawable.background_round_green)
        binding.noBtn.setTextColor(Color.parseColor("#878787"))
        binding.noBtn.setBackgroundResource(R.drawable.background_round_gray)
    }

    private fun setNoGreen() {
        binding.yesBtn.setTextColor(Color.parseColor("#878787"))
        binding.yesBtn.setBackgroundResource(R.drawable.background_round_gray)
        binding.noBtn.setTextColor(Color.parseColor("#32CC42"))
        binding.noBtn.setBackgroundResource(R.drawable.background_round_green)
    }

    private fun setBtnGreen() {
        binding.diagnosisBtn.setBackgroundResource(R.drawable.background_green_gradient)
    }
}