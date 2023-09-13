package com.dangjang.android.presentation.home

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dangjang.android.domain.model.GetGlucoseVO
import com.dangjang.android.domain.model.GlucoseGuideVO
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
import com.dangjang.android.domain.model.GlucoseListVO
import com.dangjang.android.domain.model.GuidesVO
import com.dangjang.android.domain.model.TodayGuidesVO
import com.dangjang.android.presentation.R
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeUseCase: HomeUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val _addHealthMetricFlow = MutableStateFlow(HealthMetricVO())
    val addHealthMetricFlow = _addHealthMetricFlow.asStateFlow()

    private val _addHealthMetricRequest = MutableStateFlow(AddHealthMetricRequest())
    val addHealthMetricRequest = _addHealthMetricRequest.asStateFlow()

    private val _glucoseList = MutableStateFlow(ArrayList<GlucoseListVO>())
    val glucoseList = arrayListOf<GlucoseListVO>()

    private val _glucoseTimeList = MutableStateFlow(ArrayList<String>())
    val glucoseTimeList = arrayListOf<String>()

    private val _getGlucoseFlow = MutableStateFlow(GetGlucoseVO())
    val getGlucoseFlow = _getGlucoseFlow.asStateFlow()

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

    fun addHealthMetric(
        accessToken: String
    ) {
        viewModelScope.launch {
            getHomeUseCase.addHealthMetric("Bearer $accessToken", addHealthMetricRequest.value)
                .onEach {
                    _addHealthMetricFlow.emit(it)
                }
                .handleErrors()
                .collect()
        }
    }

    fun getGlucose(
        accessToken: String, date: String
    ) {
        viewModelScope.launch {
            getHomeUseCase.getGlucose("Bearer $accessToken", date)
                .onEach {
                    _getGlucoseFlow.emit(it)
                }
                .handleErrors()
                .collect()
        }
    }

    private fun <T> Flow<T>.handleErrors(): Flow<T> =
        catch { e ->
            Log.e("error",e.message.toString())
            Toast.makeText(
                getApplication<Application>().applicationContext, e.message,
                Toast.LENGTH_SHORT
            ).show()
        }

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

    fun addBackgroundToTodayGuides(todayGuidesVO: List<TodayGuidesVO>): List<GlucoseGuideVO> {
        var glucoseGuides : MutableList<GlucoseGuideVO> = mutableListOf()
        todayGuidesVO.map {
            var name = it.alert
            var background = when(name) {
                "저혈당","경고" -> {
                    R.drawable.background_circle_red
                }
                "저혈당 의심","주의" -> {
                    R.drawable.background_circle_orange
                }
                "정상" -> {
                    R.drawable.background_circle_green
                }
                else -> {
                    R.drawable.background_circle_green
                }
            }
            background = if (it.count == 0) {
                R.drawable.background_circle_gray
            } else {
                background
            }
            if (name == "저혈당 의심") {
                name = "저혈당\n의심"
            }
            glucoseGuides.add(GlucoseGuideVO(name,it.count.toString() + "번", background))
        }
        return glucoseGuides
    }

    fun addIconToGuides(guidesVO: List<GuidesVO>): List<GlucoseListVO> {
        var glucoseGuides : MutableList<GlucoseListVO> = mutableListOf()
        guidesVO.map {
            var name = it.alert
            var tag = when(name) {
                "저혈당" -> {
                    R.drawable.ic_tag_low
                }
                "저혈당 의심" -> {
                    R.drawable.ic_tag_lower
                }
                "정상" -> {
                    R.drawable.ic_tag_normal
                }
                "주의" -> {
                    R.drawable.ic_tag_caution
                }
                "경고" -> {
                    R.drawable.ic_tag_warning
                }
                else -> {
                    R.drawable.ic_tag_normal
                }
            }
            glucoseGuides.add(GlucoseListVO(it.type, it.unit, tag, it.title, it.content))
        }
        return glucoseGuides
    }
}