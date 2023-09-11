package com.dangjang.android.presentation.common

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("dateToString")
fun dateToString(view: TextView, date: String?) {
    if (date != null) {
        view.text = date.substring(5, 7) + "월 " + date.substring(8, 10) + "일"
    }
}