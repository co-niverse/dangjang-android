package com.dangjang.android.presentation.chart

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dangjang.android.domain.constants.ACCESS_TOKEN_KEY
import com.dangjang.android.domain.constants.TOKEN_SPF_KEY
import com.dangjang.android.domain.model.GetChartVO
import com.dangjang.android.domain.usecase.ChartUseCase
import com.dangjang.android.domain.usecase.TokenUseCase
import com.dangjang.android.presentation.login.LoginActivity
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
    private val getTokenUseCase: TokenUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val _getChartFlow = MutableStateFlow(GetChartVO())
    val getChartFlow = _getChartFlow.asStateFlow()

    private val _startDate = MutableStateFlow(String())
    val startDate = _startDate.asStateFlow()

    private val _endDate = MutableStateFlow(String())
    val endDate = _endDate.asStateFlow()

    //토큰 재발급
    private val _reissueTokenFlow = MutableStateFlow(false)
    val reissueTokenFlow = _reissueTokenFlow.asStateFlow()

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
        return formatZeroDate(dateList)
    }

    private fun formatZeroDate(dateList: ArrayList<String>): ArrayList<String> {
        var formattedDateList = ArrayList<String>()
        dateList.forEach {
            if (it.startsWith("0")) {
                formattedDateList.add(it.substring(1))
            } else {
                formattedDateList.add(it)
            }
        }
        return formattedDateList
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

    private fun getAccessToken(): String? {
        val sharedPreferences = getApplication<Application>().applicationContext.getSharedPreferences(
            TOKEN_SPF_KEY, Context.MODE_PRIVATE)

        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }

    private fun <T> Flow<T>.handleErrors(): Flow<T> =
        catch { e ->
            Log.e("error",e.message.toString())
            if (e.message.toString() == "만료된 토큰입니다.") {
                getTokenUseCase.reissueToken(getAccessToken() ?: "")
                    .onEach {
                        _reissueTokenFlow.emit(it)
                    }
                    .handleReissueTokenErrors()
                    .collect()
                Toast.makeText(
                    getApplication<Application>().applicationContext, "로그인이 만료되었습니다. 다시 한번 시도해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
//            Toast.makeText(
//                getApplication<Application>().applicationContext, e.message,
//                Toast.LENGTH_SHORT
//            ).show()
        }

    private fun <T> Flow<T>.handleReissueTokenErrors(): Flow<T> =
        catch { e ->
            Log.e("error",e.message.toString())
            // refreshToken까지 만료된 경우 -> 로그인 화면으로 이동
            if (e.message.toString() == "만료된 토큰입니다.") {
                Intent(getApplication<Application>().applicationContext, LoginActivity::class.java).apply {
                    getApplication<Application>().applicationContext.startActivity(this)
                }
                Toast.makeText(
                    getApplication<Application>().applicationContext, "로그인이 필요합니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
//            Toast.makeText(
//                getApplication<Application>().applicationContext, e.message,
//                Toast.LENGTH_SHORT
//            ).show()
        }
}