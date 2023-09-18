package com.dangjang.android.presentation.chart

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dangjang.android.domain.model.GetChartVO
import com.dangjang.android.domain.usecase.ChartUseCase
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ChartViewModel @Inject constructor(
    private val getChartUseCase: ChartUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val _getChartFlow = MutableStateFlow(GetChartVO())
    val getChartFlow = _getChartFlow.asStateFlow()

    private val _startDate = MutableStateFlow(String())
    val startDate = _startDate.asStateFlow()

    private val _endDate = MutableStateFlow(String())
    val endDate = _endDate.asStateFlow()

    fun getGlucoseMinList(): MutableList<BarEntry> {
        var glucoseMinList = mutableListOf<BarEntry>()

        for (i in 0..6) {
            var date = getAmountDate(startDate.value, i)
            getChartFlow.value.bloodSugars.forEach {
                if (it.date == date) {
                    glucoseMinList.add(BarEntry(i.toFloat(), it.minUnit.toFloat()))
                } else {
                    glucoseMinList.add(BarEntry(i.toFloat(), 0f))
                }
            }
        }
        return glucoseMinList
    }

    fun getGlucoseMaxList(): MutableList<BarEntry> {
        var glucoseMaxList = mutableListOf<BarEntry>()

        for (i in 0..6) {
            var date = getAmountDate(startDate.value, i)
            getChartFlow.value.bloodSugars.forEach {
                if (it.date == date) {
                    glucoseMaxList.add(BarEntry(i.toFloat(), it.maxUnit.toFloat()))
                } else {
                    glucoseMaxList.add(BarEntry(i.toFloat(), 0f))
                }
            }
        }
        return glucoseMaxList
    }

    fun getWeightList(): MutableList<Entry> {
        var weightList = mutableListOf<Entry>()

        for (i in 0..6) {
            var date = getAmountDate(startDate.value, i)
            getChartFlow.value.weights.forEach {
                if (it.date == date) {
                    weightList.add(Entry(i.toFloat(), it.unit.toFloat()))
                } else {
                    //TODO : 추후 UI 상 더 좋은 것 선택
                    //weightList.add(Entry(i.toFloat(), Float.NaN))
                }
            }
        }
        return weightList
    }

    fun getStepList(): MutableList<Entry> {
        var stepList = mutableListOf<Entry>()

        for (i in 0..6) {
            var date = getAmountDate(startDate.value, i)
            getChartFlow.value.stepCounts.forEach {
                if (it.date == date) {
                    stepList.add(Entry(i.toFloat(), it.unit.toFloat()))
                } else {
                    //TODO : 추후 UI 상 더 좋은 것 선택
                    //stepList.add(Entry(i.toFloat(), Float.NaN))
                }
            }
        }
        return stepList
    }

    fun getExerciseList(): MutableList<Entry> {
        var exerciseList = mutableListOf<Entry>()

        for (i in 0..6) {
            var date = getAmountDate(startDate.value, i)
            getChartFlow.value.exerciseCalories.forEach {
                if (it.date == date) {
                    exerciseList.add(Entry(i.toFloat(), it.unit.toFloat()))
                } else {
                    //TODO : 추후 UI 상 더 좋은 것 선택
                    //exerciseList.add(Entry(i.toFloat(), Float.NaN))
                }
            }
        }
        return exerciseList
    }

    fun getDateList(): ArrayList<String> {
        var dateList = ArrayList<String>()
        var index = 0
        var date = getAmountDate(startDate.value, index)
        while (date != endDate.value) {
            dateList.add(date.substring(8,10)+"일")
            index++
            date = getAmountDate(startDate.value, index)
        }
        dateList.add(date.substring(8,10)+"일")
        return dateList
    }

    fun getChart(accessToken: String) {
        viewModelScope.launch {
            getChartUseCase.getChart("Bearer $accessToken", startDate.value, endDate.value)
                .onEach {
                    _getChartFlow.emit(it)
                }
                .handleErrors()
                .collect()
        }
    }

    fun setStartAndEndDate() {
        _startDate.update {
            getAmountDate(getTodayDate(), -6)
        }
        _endDate.update {
            getTodayDate()
        }
    }

    fun addStartAndEndDate() {
        if (_endDate.value == getTodayDate()) return
        else {
            _startDate.update {
                getAmountDate(it, 7)
            }
            _endDate.update {
                getAmountDate(it, 7)
            }
        }
    }

    fun subtractStartAndEndDate() {
        _startDate.update {
            getAmountDate(it, -7)
        }
        _endDate.update {
            getAmountDate(it, -7)
        }
    }

    private fun getTodayDate(): String {
        val currentTime: Date = Calendar.getInstance().getTime()
        val format = SimpleDateFormat("yyyy-MM-dd")
        return format.format(currentTime)
    }

    private fun getAmountDate(originDate: String, amount: Int) : String {
        val format = SimpleDateFormat("yyyy-MM-dd")
        val date = format.parse(originDate)
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DATE, amount)
        return format.format(calendar.time)
    }

    private fun <T> Flow<T>.handleErrors(): Flow<T> =
        catch { e ->
            Log.e("error",e.message.toString())
            Toast.makeText(
                getApplication<Application>().applicationContext, e.message,
                Toast.LENGTH_SHORT
            ).show()
        }
}