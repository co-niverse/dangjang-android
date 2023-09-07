package com.dangjang.android.presentation.home

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dangjang.android.domain.model.HealthMetricVO
import com.dangjang.android.domain.request.AddHealthMetricRequest
import com.dangjang.android.domain.usecase.HomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dangjang.android.domain.model.GlucoseListVO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeUseCase: HomeUseCase,
    application: Application
) : AndroidViewModel(application) {
  
  fun getHourSpinnerList(): ArrayList<String> {
        val hourList = arrayListOf<String>()

        for (i in 0..20) {
            hourList.add(i.toString())
        }

        return hourList
    }

    fun getMinuteSpinnerList(): ArrayList<String> {
        val minuteList = arrayListOf<String>()

        for (i in 0..59) {
            minuteList.add(i.toString())
        }

        return minuteList
    }

    private val _addHealthMetricFlow = MutableStateFlow(HealthMetricVO())
    val addHealthMetricFlow = _addHealthMetricFlow.asStateFlow()

    private val _addHealthMetricRequest = MutableStateFlow(AddHealthMetricRequest())
    val addHealthMetricRequest = _addHealthMetricRequest.asStateFlow()

    fun addHealthMetric(
        addHealthMetricRequest: AddHealthMetricRequest
    ) {
        viewModelScope.launch {
            getHomeUseCase.addHealthMetric(addHealthMetricRequest)
                .onEach {
                    _addHealthMetricFlow.emit(it)
                }
                .handleErrors()
                .collect()
        }
    }

    private fun <T> Flow<T>.handleErrors(): Flow<T> =
        catch { e -> Toast.makeText(getApplication<Application>().applicationContext,e.message,
            Toast.LENGTH_SHORT).show() }

    fun setType(type: String) {
        _addHealthMetricRequest.update {
            it.copy(type = type)
        }
    }

    fun setCreatedAt(createdAt: String) {
        _addHealthMetricRequest.update {
            it.copy(createdAt = createdAt)
        }
    }

    fun setUnit(unit: String) {
        _addHealthMetricRequest.update {
            it.copy(unit = unit)

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