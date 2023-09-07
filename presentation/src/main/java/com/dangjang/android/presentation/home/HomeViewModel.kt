package com.dangjang.android.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dangjang.android.domain.model.GlucoseListVO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
) : ViewModel() {

    private val _glucoseList = MutableStateFlow(ArrayList<GlucoseListVO>())
    val glucoseList = arrayListOf<GlucoseListVO>()

    private val _glucoseTimeList = MutableStateFlow(ArrayList<String>())
    val glucoseTimeList = arrayListOf<String>()

    fun getGlucoseList() {
        viewModelScope.launch {
            glucoseList.add(
                GlucoseListVO(
                    "공복",
                    100,
                    "전반적으로 혈당이 높습니다! 조치가 필요해요",
                    "먹은 과일이 혈당을 높였어요\n운동을 하지 않아 혈당이 높아졌어요"))
            glucoseList.add(
                GlucoseListVO(
                    "아침식전",
                    80,
                    "전반적으로 혈당이 높습니다! 조치가 필요해요",
                    "먹은 과일이 혈당을 높였어요\n" +
                            "운동을 하지 않아 혈당이 높아졌어요"))
            glucoseList.add(
                GlucoseListVO(
                    "취침전",
                    120,
                    "전반적으로 혈당이 높습니다! 조치가 필요해요",
                    "먹은 과일이 혈당을 높였어요\n" +
                            "운동을 하지 않아 혈당이 높아졌어요")
            )
            _glucoseList.emit(glucoseList)
        }
    }

    fun getGlucoseTimeList() {
        viewModelScope.launch {
            glucoseTimeList.add("공복")
            glucoseTimeList.add("아침식전")
            glucoseTimeList.add("아침식후")
            glucoseTimeList.add("점심식전")
            glucoseTimeList.add("점심식후")
            glucoseTimeList.add("저녁식전")
            glucoseTimeList.add("저녁식후")
            glucoseTimeList.add("취침전")
            glucoseTimeList.add("기타")
            _glucoseTimeList.emit(glucoseTimeList)
        }
    }
}