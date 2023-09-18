package com.dangjang.android.presentation.chart

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dangjang.android.domain.model.GetChartVO
import com.dangjang.android.domain.usecase.ChartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChartViewModel @Inject constructor(
    private val getChartUseCase: ChartUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val _getChartFlow = MutableStateFlow(GetChartVO())
    val getChartFlow = _getChartFlow.asStateFlow()

    fun getChart(accessToken: String, startDate: String, endDate: String) {
        viewModelScope.launch {
            getChartUseCase.getChart("Bearer $accessToken", startDate, endDate)
                .onEach {
                    _getChartFlow.emit(it)
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
}