package com.dangjang.android.presentation.common

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("dateToString")
fun dateToString(view: TextView, date: String?) {
    if (date != null) {
        if (date.isNotEmpty()) {
            view.text = date.substring(5, 7) + "월 " + date.substring(8, 10) + "일"
        }
    }
}

@BindingAdapter("dateToMonthDay")
fun dateToMonthDay(view: TextView, date: String?) {
    if (date != null) {
        if (date.isNotEmpty()) {
            view.text = date.substring(5, 7) + "/" + date.substring(8, 10)
        }
    }
}

@BindingAdapter("addKcalText")
fun addKcalText(view: TextView, calorie: Int?) {
    if (calorie != null) {
        view.text = calorie.toString() + "kcal"
    }
}

@BindingAdapter("addStepUnit")
fun addStepUnit(view: TextView, stepCount: Int?) {
    if (stepCount != null) {
        view.text = stepCount.toString() + "보"
    }
}