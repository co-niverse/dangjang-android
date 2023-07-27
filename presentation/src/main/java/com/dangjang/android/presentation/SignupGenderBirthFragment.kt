package com.dangjang.android.presentation

import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.dangjang.android.common_ui.BaseFragment
import com.dangjang.android.presentation.databinding.FragmentSignupGenderBirthBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupGenderBirthFragment : BaseFragment<FragmentSignupGenderBirthBinding>(R.layout.fragment_signup_gender_birth) {

    private val viewModel by viewModels<SignupViewModel>()

    var manFlag = false
    var womanFlag = false

    override fun initView() {
        bind {
            vm = viewModel
        }
    }
    override fun onStart() {
        super.onStart()

        binding.genderBirthBtn.setOnTouchListener({ v, event -> true })

        setYearSpinner()
        setMonthSpinner()
        setDaySpinner()

        binding.manBtn.setOnClickListener {
            if (manFlag) {
                setManBtnGray()
                setWomanBtnGreen()
            } else {
                setManBtnGreen()
                setWomanBtnGray()
            }
            manFlag = !manFlag
            womanFlag = manFlag
            setBtnGreen()
        }

        binding.womanBtn.setOnClickListener {
            if (womanFlag) {
                setWomanBtnGray()
                setManBtnGreen()
            } else {
                setWomanBtnGreen()
                setManBtnGray()
            }
            manFlag = !manFlag
            womanFlag = manFlag
            setBtnGreen()
        }

        binding.genderBirthBtn.setOnClickListener {
            val signupBodyFragment = SignupBodyFragment()
            parentFragmentManager.beginTransaction().replace(R.id.fragment_signup_view, signupBodyFragment).addToBackStack(null).commit()

        }

        binding.backIv.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

    }

    private fun setWomanBtnGreen() {
        binding.womanBtn.setTextColor(Color.parseColor("#32CC42"))
        binding.womanBtn.setBackgroundResource(R.drawable.background_round_green)
    }

    private fun setWomanBtnGray() {
        binding.womanBtn.setTextColor(Color.parseColor("#878787"))
        binding.womanBtn.setBackgroundResource(R.drawable.background_round_gray)
    }

    private fun setManBtnGreen() {
        binding.manBtn.setTextColor(Color.parseColor("#32CC42"))
        binding.manBtn.setBackgroundResource(R.drawable.background_round_green)
    }

    private fun setManBtnGray() {
        binding.manBtn.setTextColor(Color.parseColor("#878787"))
        binding.manBtn.setBackgroundResource(R.drawable.background_round_gray)
    }

    private fun setBtnGreen() {
        binding.genderBirthBtn.setBackgroundResource(R.drawable.background_green_gradient)
        binding.genderBirthBtn.setOnTouchListener({ v, event -> false })
    }

    private fun setYearSpinner() {
        val yearSpinner: Spinner = binding.yearSpinner

        val yearList = arrayListOf<String>()
        for (i in 1940..2021) {
            yearList.add(i.toString()+" 년")
        }

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
        yearSpinner.setSelection(30)

    }

    private fun setMonthSpinner() {
        val monthSpinner: Spinner = binding.monthSpinner

        val monthList = arrayListOf<String>()
        for (i in 1..12) {
            monthList.add(i.toString()+" 월")
        }

        val monthAdapter = object : ArrayAdapter<String>(requireContext(), R.layout.custom_spinner_dropdown_item, monthList),
            SpinnerAdapter {
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                // 커스텀한 드롭다운 리스트에 표시할 뷰를 정의합니다.
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(Color.BLACK) // 드롭다운 리스트의 항목 텍스트 색상 설정
                return view
            }
        }

        monthSpinner.adapter = monthAdapter
    }

    private fun setDaySpinner() {
        val daySpinner: Spinner = binding.daySpinner

        val dayList = arrayListOf<String>()
        for (i in 1..31) {
            dayList.add(i.toString()+" 일")
        }

        val dayAdapter = object : ArrayAdapter<String>(requireContext(), R.layout.custom_spinner_dropdown_item, dayList),
            SpinnerAdapter {
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                // 커스텀한 드롭다운 리스트에 표시할 뷰를 정의합니다.
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(Color.BLACK) // 드롭다운 리스트의 항목 텍스트 색상 설정
                return view
            }
        }

        daySpinner.adapter = dayAdapter
    }
}