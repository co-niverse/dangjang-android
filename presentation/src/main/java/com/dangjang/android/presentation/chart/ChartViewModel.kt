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

    fun getGlucoseMinList(): ArrayList<BarEntry> {
        var glucoseMinList = ArrayList<BarEntry>()
        var index = 0f
        getChartFlow.value.bloodSugars.forEach {
            var date = getAmountDate(startDate.value, (index).toInt())
            if (it.date == date) {
                glucoseMinList.add(BarEntry(index, it.minUnit.toFloat()))
            } else {
                glucoseMinList.add(BarEntry(index, 0f))
            }
            index++
        }
        return glucoseMinList
    }

    fun getGlucoseMaxList(): ArrayList<BarEntry> {
        var glucoseMaxList = ArrayList<BarEntry>()
        var index = 0f
        getChartFlow.value.bloodSugars.forEach {
            var date = getAmountDate(startDate.value, (index).toInt())
            if (it.date == date) {
                glucoseMaxList.add(BarEntry(index, it.maxUnit.toFloat()))
            } else {
                glucoseMaxList.add(BarEntry(index, 0f))
            }
            index++
        }
        return glucoseMaxList
    }

    fun getWeightList(): ArrayList<Entry> {
        var weightList = ArrayList<Entry>()
        var index = 0f
        getChartFlow.value.weights.forEach {
            var date = getAmountDate(startDate.value, (index).toInt())
            if (it.date == date) {
                weightList.add(Entry(index, it.unit.toFloat()))
            } else {
                weightList.add(Entry(index, 0f))
            }
            index++
        }
        return weightList
    }

    fun getStepList(): ArrayList<Entry> {
        var stepList = ArrayList<Entry>()
        var index = 0f
        getChartFlow.value.stepCounts.forEach {
            var date = getAmountDate(startDate.value, (index).toInt())
            if (it.date == date) {
                stepList.add(Entry(index, it.unit.toFloat()))
            } else {
                stepList.add(Entry(index, 0f))
            }
            index++
        }
        return stepList
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