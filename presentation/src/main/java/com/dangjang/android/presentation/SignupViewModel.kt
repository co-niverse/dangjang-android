package com.dangjang.android.presentation

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
}