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

        binding.diagnosisBtn.setOnTouchListener({ v, event -> true })

        setYearSpinner()
        binding.yearCl.visibility = View.INVISIBLE

        binding.yesBtn.setOnClickListener {
            viewModel.setDiagnosisFlag(true)
            setYesGreen()
            setBtnGreen()
            binding.yearCl.visibility = View.VISIBLE
        }

        binding.noBtn.setOnClickListener {
            viewModel.setDiagnosisFlag(false)
            setNoGreen()
            setBtnGreen()
            binding.yearCl.visibility = View.INVISIBLE
        }

        binding.diagnosisBtn.setOnClickListener {
            if (viewModel.diagnosisFlag.value!!) {
                val signupMediFragment = SignupMediFragment()
                parentFragmentManager.beginTransaction().replace(R.id.fragment_signup_view, signupMediFragment).addToBackStack(null).commit()
            } else {
                val signupDiseaseFragment = SignupDiseaseFragment()
                parentFragmentManager.beginTransaction().replace(R.id.fragment_signup_view, signupDiseaseFragment).addToBackStack(null).commit()
            }
        }

        binding.backIv.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setYearSpinner() {
        val yearSpinner: Spinner = binding.yearSpinner
        val yearList = viewModel.getDiagnosisYearList()

        val yearAdapter = object : ArrayAdapter<String>(requireContext(), R.layout.custom_spinner_dropdown_item, yearList),
            SpinnerAdapter {
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                // 커스텀한 드롭다운 리스트에 표시할 뷰를 정의합니다.
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(Color.BLACK) // 드롭다운 리스트의 항목 텍스트 색상 설정
                return view
            }
        }

        yearSpinner.adapter = yearAdapter

    }

    private fun setYesGreen() {
        binding.yesBtn.setTextColor(viewModel.setGreenTextColor())
        binding.yesBtn.setBackgroundResource(viewModel.setGreenBackgroundResource())
        binding.noBtn.setTextColor(viewModel.setGrayTextColor())
        binding.noBtn.setBackgroundResource(viewModel.setGrayBackgroundResource())
    }

    private fun setNoGreen() {
        binding.yesBtn.setTextColor(viewModel.setGrayTextColor())
        binding.yesBtn.setBackgroundResource(viewModel.setGrayBackgroundResource())
        binding.noBtn.setTextColor(viewModel.setGreenTextColor())
        binding.noBtn.setBackgroundResource(viewModel.setGreenBackgroundResource())
    }

    private fun setBtnGreen() {
        binding.diagnosisBtn.setBackgroundResource(viewModel.setGreenBtnBackgroundResource())
        binding.diagnosisBtn.setOnTouchListener({ v, event -> false })
    }
}