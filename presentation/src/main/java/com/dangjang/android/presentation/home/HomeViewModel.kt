package com.dangjang.android.presentation.home

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dangjang.android.domain.model.GetGlucoseVO
import com.dangjang.android.domain.model.GlucoseGuideVO
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
import com.dangjang.android.domain.model.EditHealthMetricVO
import com.dangjang.android.domain.model.PostPatchWeightVO
import com.dangjang.android.domain.model.TodayGuidesVO
import com.dangjang.android.domain.request.EditHealthMetricRequest
import com.dangjang.android.domain.request.EditSameHealthMetricRequest
import com.dangjang.android.presentation.R
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeUseCase: HomeUseCase,
    application: Application
) : AndroidViewModel(application) {

    //혈당
    private val _postPatchGlucoseFlow = MutableStateFlow(EditHealthMetricVO())
    val postPatchGlucoseFlow = _postPatchGlucoseFlow.asStateFlow()

    private val _editHealthMetricRequest = MutableStateFlow(EditHealthMetricRequest())
    val editHealthMetricRequest = _editHealthMetricRequest.asStateFlow()

    private val _addHealthMetricRequest = MutableStateFlow(AddHealthMetricRequest())
    val addHealthMetricRequest = _addHealthMetricRequest.asStateFlow()

    private val _glucoseList = MutableStateFlow(ArrayList<GlucoseListVO>())
    val glucoseList = arrayListOf<GlucoseListVO>()

    private val _glucoseTimeList = MutableStateFlow(ArrayList<String>())
    val glucoseTimeList = arrayListOf<String>()

    private val _getGlucoseFlow = MutableStateFlow(GetGlucoseVO())
    val getGlucoseFlow = _getGlucoseFlow.asStateFlow()

    //체중
    private val _addWeightRequest = MutableStateFlow(AddHealthMetricRequest())
    val addWeightRequest = _addWeightRequest.asStateFlow()

    private val _addWeightFlow = MutableStateFlow(PostPatchWeightVO())
    val addWeightFlow = _addWeightFlow.asStateFlow()

    private val _editWeightRequest = MutableStateFlow(PostPatchWeightVO())
    val editWeightRequest = _editWeightRequest.asStateFlow()

    //체중
    fun addWeight(accessToken: String) {
        setWeightTypeAndCreatedAt()
        viewModelScope.launch {
            getHomeUseCase.addWeight("Bearer $accessToken", addWeightRequest.value)
                .onEach {
                    _addWeightFlow.emit(it)
                }
                .handleErrors()
                .collect{
                    //TODO : get Weight
                }
        }
    }

    private fun setWeightTypeAndCreatedAt() {
        _addWeightRequest.update {
            it.copy(type = "체중", createdAt = getTodayDate())
        }
    }

    fun setWeightUnit(weight: String) {
        _addWeightRequest.update {
            it.copy(unit = weight)
        }
    }

    //혈당
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
                    _postPatchGlucoseFlow.emit(it)
                }
                .handleErrors()
                .collect{
                    getGlucose(accessToken)
                }
        }
    }

    fun editGlucose(
        accessToken: String
    ) {
        viewModelScope.launch {
            if (editHealthMetricRequest.value.type == editHealthMetricRequest.value.newType) {
                getHomeUseCase.editSameGlucose("Bearer $accessToken", EditSameHealthMetricRequest(
                    editHealthMetricRequest.value.type,
                    editHealthMetricRequest.value.createdAt,
                    editHealthMetricRequest.value.unit
                ))
                    .onEach {
                        _postPatchGlucoseFlow.emit(it)
                    }
                    .handleErrors()
                    .collect{
                        getGlucose(accessToken)
                    }
            } else {
                getHomeUseCase.editGlucose("Bearer $accessToken", editHealthMetricRequest.value)
                    .onEach {
                        _postPatchGlucoseFlow.emit(it)
                    }
                    .handleErrors()
                    .collect{
                        getGlucose(accessToken)
                    }
            }
        }
    }

    fun setEditGlucoseCreatedAt(createdAt: String) {
        _editHealthMetricRequest.update {
            it.copy(createdAt = createdAt)
        }
    }

    fun setEditGlucoseType(type: String) {
        _editHealthMetricRequest.update {
            it.copy(type = type)
        }
    }

    fun setEditGlucoseNewType(newType: String) {
        _editHealthMetricRequest.update {
            it.copy(newType = newType)
        }
    }

    fun setEditGlucoseValue(value: String) {
        _editHealthMetricRequest.update {
            it.copy(unit = value)
        }
    }

    fun getGlucose(
        accessToken: String
    ) {
        viewModelScope.launch {
            getHomeUseCase.getGlucose("Bearer $accessToken", getTodayDate())
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

    fun getTodayDate(): String {
        val currentTime: Date = Calendar.getInstance().getTime()
        val format = SimpleDateFormat("yyyy-MM-dd")
        return format.format(currentTime)
    }
}