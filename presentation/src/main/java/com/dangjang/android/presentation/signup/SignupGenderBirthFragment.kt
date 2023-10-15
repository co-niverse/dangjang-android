package com.dangjang.android.presentation.signup

import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.dangjang.android.common_ui.BaseFragment
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.FragmentSignupGenderBirthBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class SignupGenderBirthFragment : BaseFragment<FragmentSignupGenderBirthBinding>(R.layout.fragment_signup_gender_birth) {

    private val viewModel : SignupViewModel by activityViewModels()

    var manFlag = false
    var womanFlag = false
    private var birthday = ""
    private var year = ""
    private var month = ""
    private var day = ""

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
                manFlag = false
                womanFlag = true
            } else {
                setManBtnGreen()
                setWomanBtnGray()
                manFlag = true
                womanFlag = false
            }
            setBtnGreen()
        }

        binding.womanBtn.setOnClickListener {
            if (womanFlag) {
                setWomanBtnGray()
                setManBtnGreen()
                womanFlag = false
                manFlag = true
            } else {
                setWomanBtnGreen()
                setManBtnGray()
                womanFlag = true
                manFlag = false
            }
            setBtnGreen()
        }

        binding.genderBirthBtn.setOnClickListener {
            if (manFlag) {
                viewModel.setGender(false)
            }
            if (womanFlag) {
                viewModel.setGender(true)
            }

            birthday = "$year-"
            if (month.toInt() < 10) {
                birthday += "0$month-"
            } else {
                birthday += "$month-"
            }

            if (day.toInt() < 10) {
                birthday += "0$day"
            } else {
                birthday += day
            }

            viewModel.setBirthday(birthday)

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

        val yearAdapter = object : ArrayAdapter<String>(requireContext(),
            R.layout.custom_spinner_dropdown_item, yearList),
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

        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d("로그", "년: ${yearList[position]}")
                year = yearList[position].split(" ")[0]
            }
        }
    }

    private fun setMonthSpinner() {
        val monthSpinner: Spinner = binding.monthSpinner

        val monthList = arrayListOf<String>()
        for (i in 1..12) {
            monthList.add(i.toString()+" 월")
        }

        val monthAdapter = object : ArrayAdapter<String>(requireContext(),
            R.layout.custom_spinner_dropdown_item, monthList),
            SpinnerAdapter {
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                // 커스텀한 드롭다운 리스트에 표시할 뷰를 정의합니다.
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(Color.BLACK) // 드롭다운 리스트의 항목 텍스트 색상 설정
                return view
            }
        }

        monthSpinner.adapter = monthAdapter

        monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d("로그", "월: ${monthList[position]}")
                month = monthList[position].split(" ")[0]
            }
        }
    }

    private fun setDaySpinner() {
        val daySpinner: Spinner = binding.daySpinner

        val dayList = arrayListOf<String>()
        for (i in 1..31) {
            dayList.add(i.toString()+" 일")
        }

        val dayAdapter = object : ArrayAdapter<String>(requireContext(),
            R.layout.custom_spinner_dropdown_item, dayList),
            SpinnerAdapter {
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                // 커스텀한 드롭다운 리스트에 표시할 뷰를 정의합니다.
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(Color.BLACK) // 드롭다운 리스트의 항목 텍스트 색상 설정
                return view
            }
        }

        daySpinner.adapter = dayAdapter

        daySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d("로그", "일: ${dayList[position]}")
                day = dayList[position].split(" ")[0]
            }
        }
    }
}