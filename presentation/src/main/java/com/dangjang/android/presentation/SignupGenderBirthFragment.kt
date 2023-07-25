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

    override fun initView() {
        bind {
            vm = viewModel
        }
    }
    override fun onStart() {
        super.onStart()

        var manFlag = false
        var womanFlag = false
        var birthFlag = 0

        binding.manBtn.setOnClickListener {
            if (manFlag) {
                binding.manBtn.setTextColor(Color.parseColor("#878787"))
                binding.manBtn.setBackgroundResource(R.drawable.background_round_gray)
                binding.womanBtn.setTextColor(Color.parseColor("#32CC42"))
                binding.womanBtn.setBackgroundResource(R.drawable.background_round_green)
            } else {
                binding.manBtn.setTextColor(Color.parseColor("#32CC42"))
                binding.manBtn.setBackgroundResource(R.drawable.background_round_green)
                binding.womanBtn.setTextColor(Color.parseColor("#878787"))
                binding.womanBtn.setBackgroundResource(R.drawable.background_round_gray)
            }
            manFlag = !manFlag
            womanFlag = manFlag
        }

        binding.womanBtn.setOnClickListener {
            if (womanFlag) {
                binding.womanBtn.setTextColor(Color.parseColor("#878787"))
                binding.womanBtn.setBackgroundResource(R.drawable.background_round_gray)
                binding.manBtn.setTextColor(Color.parseColor("#32CC42"))
                binding.manBtn.setBackgroundResource(R.drawable.background_round_green)
            } else {
                binding.womanBtn.setTextColor(Color.parseColor("#32CC42"))
                binding.womanBtn.setBackgroundResource(R.drawable.background_round_green)
                binding.manBtn.setTextColor(Color.parseColor("#878787"))
                binding.manBtn.setBackgroundResource(R.drawable.background_round_gray)
            }
            manFlag = !manFlag
            womanFlag = manFlag
        }

        val yearSpinner: Spinner = binding.yearSpinner
        val monthSpinner: Spinner = binding.monthSpinner
        val daySpinner: Spinner = binding.daySpinner

        val yearList = arrayListOf<String>()
        for (i in 1940..2021) {
            yearList.add(i.toString()+" 년")
        }

        val monthList = arrayListOf<String>()
        for (i in 1..12) {
            monthList.add(i.toString()+" 월")
        }

        val dayList = arrayListOf<String>()
        for (i in 1..31) {
            dayList.add(i.toString()+" 일")
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

        val monthAdapter = object : ArrayAdapter<String>(requireContext(), R.layout.custom_spinner_dropdown_item, monthList),
            SpinnerAdapter {
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                // 커스텀한 드롭다운 리스트에 표시할 뷰를 정의합니다.
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(Color.BLACK) // 드롭다운 리스트의 항목 텍스트 색상 설정
                return view
            }
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

        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                birthFlag += 1
                Log.d("birthFlag", birthFlag.toString())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

        monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                birthFlag += 1
                Log.d("birthFlag", birthFlag.toString())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

        daySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                birthFlag += 1
                Log.d("birthFlag", birthFlag.toString())
                if ((manFlag || womanFlag) && (birthFlag == 6)) {
                    binding.genderBirthBtn.setBackgroundResource(R.drawable.background_green_gradient)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

        yearSpinner.adapter = yearAdapter
        yearSpinner.setSelection(30)
        monthSpinner.adapter = monthAdapter
        daySpinner.adapter = dayAdapter

        binding.genderBirthBtn.setOnClickListener {
            val signupBodyFragment = SignupBodyFragment()
            parentFragmentManager.beginTransaction().add(R.id.fragment_signup_view, signupBodyFragment).commit()

        }

    }
}