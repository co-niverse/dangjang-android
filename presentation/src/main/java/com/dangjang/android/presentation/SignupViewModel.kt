package com.dangjang.android.presentation

import android.graphics.Color
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
) : ViewModel() {

    fun getDiagnosisYearList(): ArrayList<String> {
        val yearList = arrayListOf<String>()

        for (i in 1..20) {
            yearList.add(i.toString())
        }
        yearList.add("20년 이상")

        return yearList
    }

    fun setGreenTextColor(): Int {
        return Color.parseColor("#32CC42")
    }

    fun setGreenBackgroundResource(): Int {
        return R.drawable.background_round_green
    }

    fun setGrayTextColor(): Int {
        return Color.parseColor("#878787")
    }

    fun setGrayBackgroundResource(): Int {
        return R.drawable.background_round_gray
    }

    fun setGreenBtnBackgroundResource(): Int {
        return R.drawable.background_green_gradient
    }
}