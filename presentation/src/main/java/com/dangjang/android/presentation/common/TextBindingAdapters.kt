package com.dangjang.android.presentation.common

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@BindingAdapter("dateToString")
fun dateToString(view: TextView, date: String?) {
    if (date != null) {
        if (date.isNotEmpty() && date.length == 10) {
            view.text = date.substring(5, 7) + "월 " + date.substring(8, 10) + "일"
        }
    } else {
        view.text = ""
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

@BindingAdapter("addWeightUnit")
fun addWeightUnit(view: TextView, weight: String?) {
    if (weight != null) {
        if (weight != "")
            view.text = weight + "KG"
        else
            view.text = weight
    }
}

@BindingAdapter("mypageNickname")
fun mypageNickname(view: TextView, nickname: String?) {
    if (nickname != null) {
        view.text = nickname + "님,"
    }
}

@BindingAdapter("addPointUnit")
fun addPointUnit(view: TextView, point: Int?) {
    if (point != null) {
        view.text = point.toString() + " 포인트"
    }
}

@BindingAdapter("getHomeDate")
fun getHomeDate(view: TextView, date: String?) {
    if (date != null) {
        if (date == getTodayDate()) {
            view.text = "오늘"
        } else if (date.isNotEmpty()) {
            view.text = date.substring(5, 7) + "/" + date.substring(8, 10)
        }
    }
}
private fun getTodayDate(): String {
    val currentTime: Date = Calendar.getInstance().getTime()
    val format = SimpleDateFormat("yyyy-MM-dd")
    return format.format(currentTime)
}